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

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import java.net.URI
import java.net.URL

/**
 * 桥接 WebView
 *
 * @author Alan Yeh
 * @since 2022/12/09
 */
interface BridgeWebView {
    /**
     * 获取 View
     */
    fun getView(): View

    /**
     * 执行 JavaScript
     *
     * @param script JavaScript 脚本
     * @param complete 执行完毕后的回调
     */
    fun evaluateJavaScript(script: String, complete: (String?) -> Unit)

    /**
     * 加载地址(只支持 http:// 或 https://)
     *
     * @param url URL 地址
     */
    fun loadUrl(url: URL)

    /**
     * 加载资源
     * - res://: 支持存放在 res 目录下的压缩包、文件夹等
     * - http://
     * - https://
     *
     * @param uri 资源地址
     */
    fun loadUri(uri: URI)

    /**
     * 重新加载当前地址
     */
    fun reload()

    /**
     * 停止加载
     */
    fun stopLoading()

    /**
     * 获取当前网页的标题
     */
    fun getTitle(): String?

    /**
     * 获取当前网页的 favicon.ico
     */
    fun getFavicon(): Bitmap?

    /**
     * 获取当前网页地址
     */
    fun getUrl(): String?

    /**
     * 判断是否有上一页可以返回
     */
    fun canGoBack(): Boolean

    /**
     * 返回上一页
     */
    fun goBack()

    /**
     * 判断是否有下一页可以前进
     */
    fun canGoForward(): Boolean

    /**
     * 前进
     */
    fun goForward()

    /**
     * 销毁并释放资源
     */
    fun destroy()

    /**
     * 清除浏览器缓存
     *
     * @param includeDiskFiles 是否同时将磁盘里的缓存一起删除
     */
    fun clearCache(includeDiskFiles: Boolean = true)

    /**
     * 浏览器事件监听
     */
    var webViewEventListener: WebViewEventListener?

    interface WebViewEventListener {
        /**
         * 页面开始加载事件
         */
        fun onPageStart() {}

        /**
         * 页面加载进度变化事件
         */
        fun onProgressChanged(progress: Int) {}

        /**
         * 页页加载完成事件
         */
        fun onPageFinished(url: URL?) {}

        /**
         * 页面加载出现异常
         */
        fun onReceivedError(request: WebResourceRequest, error: WebResourceError) {}

        /**
         * 拦截网页资源请求，return null表示不拦截，在子线程执行
         */
        fun shouldInterceptRequest(request: WebResourceRequest): WebResourceResponse? {
            return null
        }

        /**
         * 文件选择器
         */
        fun onShowFileChooser(fileChooserParams: WebChromeClient.FileChooserParams, complete: (Array<Uri>?) -> Unit): Boolean {
            return false
        }

        /**
         * 网页下载
         */
        fun onDownloadStart(url: URL?, userAgent: String?, contentDisposition: String?, mimetype: String?, contentLength: Long?) {}
    }
}