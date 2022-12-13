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

package central.android.bridge.component.event

import central.android.bridge.BridgeCallback
import central.android.bridge.BridgeParam

/**
 * Event Component
 *
 * @author Alan Yeh
 * @since 2022/12/10
 */
interface EventComponentStub {

    /**
     * 注册事件
     *
     * @param name 事件名称
     * @param callback 事件回调
     * @return 回调标识（移除事件时需要）
     */
    fun register(@BridgeParam name: String, callback: BridgeCallback): String

    /**
     * 除移监听
     *
     * @param name 事件名称
     * @param code 回调标识（注册事件时的返回值）
     */
    fun deregister(@BridgeParam name: String, @BridgeParam code: String)

    /**
     * 发送事件
     *
     * @param name 事件名称
     * @param options 事件参数
     */
    fun publish(@BridgeParam name: String, @BridgeParam options: Any? = null)
}