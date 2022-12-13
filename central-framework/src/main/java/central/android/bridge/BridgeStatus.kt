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

package central.android.bridge

/**
 * 调用状态码
 *
 * @author Alan Yeh
 * @since 2022/12/11
 */
class BridgeStatus(val value: Int, val phrase: String) {
    companion object {
        val OK = BridgeStatus(200, "OK")
        val PARTIAL_CONTENT = BridgeStatus(206, "Partial Content")
        val BAD_REQUEST = BridgeStatus(400, "Bad Request")
        val UNAUTHORIZED = BridgeStatus(401, "Unauthorized")
        val FORBIDDEN = BridgeStatus(403, "Forbidden")
        val NOT_FOUND = BridgeStatus(404, "Not Found")
        val REQUEST_TIMEOUT = BridgeStatus(408, "Request Timeout")
        val CANCELED = BridgeStatus(499, "Canceled")
        val SERVER_ERROR = BridgeStatus(500, "Server Error")
    }
}