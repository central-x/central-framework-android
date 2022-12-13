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

import central.android.bridge.BridgeContext
import central.lang.Attribute
import java.util.concurrent.ConcurrentHashMap

/**
 * Bridge Exchange
 *
 * @author Alan Yeh
 * @since 2022/12/09
 */
class BridgeExchange(
    /**
     * 请求
     */
    val request: BridgeRequest,
    /**
     * 响应
     */
    val response: BridgeResponse,
    /**
     * Android 上下文
     */
    val context: BridgeContext
) {
    /**
     * 创建时间
     */
    val timestamp = System.currentTimeMillis()

    /**
     * 属性
     */
    private val attributes = ConcurrentHashMap<String, Any?>()

    /**
     * 获取属性
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getAttribute(attribute: Attribute<T>): T? {
        return this.attributes.computeIfAbsent(attribute.code) { attribute.value } as? T
    }

    /**
     * 获取属性
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> requireAttribute(attribute: Attribute<T>): T {
        return this.attributes.computeIfAbsent(attribute.code) { attribute.requireValue() } as T
    }

    /**
     * 获取属性
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getAttributeOrDefault(attribute: Attribute<T>, default: T): T {
        return this.attributes.getOrDefault(attribute.code, default) as T
    }

    /**
     * 保存属性
     */
    fun <T> setAttribute(attribute: Attribute<T>, value: T?) {
        if (value == null) {
            this.attributes.remove(attribute.code)
        } else {
            this.attributes.put(attribute.code, value)
        }
    }
}