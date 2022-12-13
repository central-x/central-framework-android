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

package central.android.bridge.core.filter

import android.content.Context
import central.android.Assetsx
import central.android.bridge.BridgeComponent
import central.android.bridge.BridgeStatus
import central.android.bridge.core.BridgeExchange
import central.pattern.chain.Filter
import central.pattern.chain.FilterChain
import java.net.URI

/**
 * 注入相关信息
 *
 * @author Alan Yeh
 * @since 2022/12/12
 */
class InjectionFilter(private val context: Context): Filter<BridgeExchange> {

    /**
     * 组件的实例
     *
     * identifier -> BridgeComponent
     */
    private val components = mapOf<String, BridgeComponent>()

    override fun filter(target: BridgeExchange, chain: FilterChain<BridgeExchange>) {
        var component = this.components[target.request.identifier]
        if (component == null){
            // 如果组件为空的话，可能需要提前初始化
            val stream = Assetsx.getAsStream(context, URI("res://central/android/bridge/${target.request.component}.json"))
            if (stream == null){
                // 找不到对应的声明文件，说明要么是用户申请错了，要么是组件没引用
                target.response.status = BridgeStatus.NOT_FOUND
                target.response.body = "组件不存在"
                return
            }
        }
        target.request.identifier

        TODO("Not yet implemented")
    }
}