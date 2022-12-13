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
import org.json.JSONObject

/**
 * 桥接请求
 *
 * @author Alan Yeh
 * @since 2022/12/09
 */
class BridgeRequest(message: String) {
    /**
     * 组件名
     */
    val component: String
    /**
     * 组件实例标识
     */
    val identifier: String
    /**
     * 请求方法
     */
    val method: String
    /**
     * 参数
     */
    val params: JSONObject
    /**
     * 参数回调
     */
    val paramCbIds: JSONObject
    /**
     * 结果回调
     */
    val resultCbId: String?

    init {
        val data = JSONObject(Uri.decode(message))
        component = data.optString("component")
        identifier = data.optString("identifier")
        method = data.optString("method")
        params = data.optJSONObject("params") ?: JSONObject()
        paramCbIds = data.optJSONObject("paramCbIds") ?: JSONObject()
        resultCbId = data.optString("resultCbId")
    }
}