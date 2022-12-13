/*
 * MIT License
 *
 * Copyright (c) 2022-present Alan Yeh <alan@yeh.cn>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package central.android.bridge.core

import android.net.Uri
import central.android.bridge.BridgeContext
import central.android.bridge.core.filter.PermissionFilter
import central.android.bridge.core.filter.ResponseFilter
import central.android.bridge.core.filter.invoke.InvokeFilter
import central.pattern.chain.FilterChain
import org.json.JSONObject

/**
 * 请求分发
 *
 * @author Alan Yeh
 * @since 2022/12/09
 */
object BridgeDispatcher {

    /**
     * 过滤器
     */
    private val filters = listOf(
//        InjectionFilter(),
        PermissionFilter(),
        InvokeFilter()
    )

    /**
     * 用于监时保存分片消息
     */
    private val messages = mutableMapOf<String, StringBuilder>()

    /**
     * 分发消息
     *
     * 获取组件协议: bridge://require:{name}
     * 发送消息协议: bridge://message:{messageId}:{content}
     * 分片消息协议: bridge://message:segment:{messageId}:{append|complete}:{content}
     */
    fun dispatch(message: String?, context: BridgeContext, completionHandler: (String?) -> Unit): Boolean {
        if (message?.startsWith("bridge://") != true) return false

        var content = message.substring("bridge://".length)

        // 判断是否分段请求
        if (content.startsWith("segment_message:")) {
            content = content.substring("segment_message:".length)
            val messageId = content.substring(0, content.indexOf(":"))
            when (val command = content.substring(messageId.length + 1)) {
                "append" -> {
                    val segment = messages.computeIfAbsent(messageId) { StringBuilder() }
                    segment.append(content.substring(messageId.length + command.length + 2))
                    completionHandler(Uri.encode(JSONObject(mapOf(
                        "status" to 200,
                        "desc" to "OK",
                        "data" to (content.length - messageId.length - command.length - 1)
                    )).toString()))
                    return true
                }
                "complete" -> {
                    val segment = messages.remove(messageId)
                    if (segment.isNullOrEmpty()){
                        completionHandler(Uri.encode(JSONObject(mapOf(
                            "status" to 500,
                            "desc" to "无法解析请求"
                        )).toString()))
                        return true
                    }
                    content = segment.toString()
                }
                else -> {
                    completionHandler(Uri.encode(JSONObject(mapOf(
                        "status" to 500,
                        "desc" to "无法解析请求"
                    )).toString()))
                    return true
                }
            }
        }

        // 构建请求
        return this.dispatch(BridgeExchange(BridgeRequest(content), BridgeResponse(), context), completionHandler)
    }

    /**
     * 分发请求
     */
    fun dispatch(exchange: BridgeExchange, completionHandler: (String?) -> Unit): Boolean {
        val filters = this.filters.toMutableList()
        filters.add(0, ResponseFilter(completionHandler))
        FilterChain(filters).filter(exchange)
        return true
    }
}