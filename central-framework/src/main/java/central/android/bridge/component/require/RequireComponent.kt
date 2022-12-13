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

package central.android.bridge.component.require

import central.android.bridge.BridgeComponent
import central.android.bridge.BridgeParam
import central.android.bridge.core.BridgeExchange
import central.android.bridge.core.annotation.BridgeMethod

/**
 * Require Component
 *
 * 用于解析组件结构，生成代理对象。
 *
 * 前端申请组件时，方法由本组件实现
 *
 * ```
 * var sms = bridge.require("sms")
 * sms.send({
 *     numbers: ["131111111111"],
 *     body: "这是一封测试短信"
 * });
 * ```
 *
 * @author Alan Yeh
 * @since 2022/12/09
 */
class RequireComponent : BridgeComponent() {

    @BridgeMethod
    fun require(exchange: BridgeExchange, @BridgeParam param: String) {

    }
}