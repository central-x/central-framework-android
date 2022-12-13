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

package central.android.bridge.webview

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.EditText
import androidx.core.net.toFile
import central.android.Assetsx
import central.android.BuildConfig
import central.android.bridge.BridgeContext
import central.android.bridge.BridgeWebView
import central.android.bridge.core.BridgeDispatcher
import java.io.ByteArrayInputStream
import java.net.URI
import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardOpenOption

/**
 * 系统 WebView
 *
 * @author Alan Yeh
 * @since 2022/12/09
 */
class SystemWebView(context: Context, attrs: AttributeSet?) : WebView(context, attrs), BridgeWebView {
    init {
        settings.run {
            // 允许执行JavaScript
            javaScriptEnabled = true
            // 不允许触摸放大缩小
            builtInZoomControls = false

            // 不允许双击放大缩小
            useWideViewPort = false
            // 图片最后加载
            // 由于在android4.4以前为了减少带宽采用阻塞式加载网页图片资源，需要将方法的参数设置为true，但是在更高的版本上面将参数设置为true反而导致图片无法加载。
            blockNetworkImage = false

            // 开启DOM storage API功能
            domStorageEnabled = true
            // 使用默认的缓存模式
            cacheMode = WebSettings.LOAD_DEFAULT
            // 设置数据库可用
            databaseEnabled = true
            // 设置缓存可用
//            setAppCacheEnabled(true)
            // WebView是否下载图片资源
            // 注意，该方法控制所有图片的下载，包括使用URI嵌入的图片（使用setBlockNetworkImage(boolean)
            // 只控制使用网络URI的图片的下载）。如果该设置项的值由false变为true，WebView展示的内容所引用的所有的图片资源将自动下载。
            loadsImagesAutomatically = true
            // 设置WebView是否支持多窗口
            setSupportMultipleWindows(false)
            // 允许加载本地文件html  file协议
            allowFileAccess = true
            javaScriptCanOpenWindowsAutomatically = true
            // 5.0 之后不支持 Https 和 Http 的混合模式
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            // 设置 WebView 支持标签的viewport属性
            loadWithOverviewMode = true
            useWideViewPort = true

            val userAgent = StringBuilder(userAgentString + " Bridge/" + BuildConfig.VERSION)

            // 添加 USER AGENT
            userAgentString = userAgent.toString()

            // 禁止文字跟随系统字体缩放
            textZoom = 100
        }

        // 隐藏水平方向滚动条
        isHorizontalScrollBarEnabled = false
        // 隐藏垂直方向滚动条
        isVerticalScrollBarEnabled = false
        // 嵌套滚动
        isNestedScrollingEnabled = true

        // TODO 在生产模式应关闭
        // 启用 ChromeDebug
        setWebContentsDebuggingEnabled(true)

        setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            webViewEventListener?.onDownloadStart(URL(url), userAgent, contentDisposition, mimetype, contentLength)
        }

        // 接受第三方 Cookie
        CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

        this.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                webViewEventListener?.onPageStart()

                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String) {
                view.evaluateJavascript("document.documentElement.style.webkitUserSelect='none'") {}
                view.evaluateJavascript("document.documentElement.style.webkitTouchCallout='none'") {}
                // 为 Android WebView 添加 AudioSession 补丁，用于挟持 HTMLAudioElement 对象的 play、pause 方法
                view.evaluateJavascript("let script = document.createElement('script');script.src='res:///audiosession-android-patch.js';(document.getElementsByTagName('head'))[0].appendChild(script);"){}

                super.onPageFinished(view, url)
                webViewEventListener?.onPageFinished(URL(url))
            }

            /**
             * 屏蔽自定义 schema
             */
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                return when (request.url.scheme) {
                    "http", "https", "file", "asset" -> false
                    else -> true
                }
            }

            /**
             * 是否拦截请求
             * 本方法也用于处理自定义 schema，用于加载 res:// assets:// 协议的资源
             */
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                var response = webViewEventListener?.shouldInterceptRequest(request)
                if (response != null) {
                    return response
                }
                response = getResource(request);
                if (response != null) {
                    return response
                }

                return super.shouldInterceptRequest(view, request)
            }

            /**
             * 网页加载错误回调
             */
            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)
                webViewEventListener?.onReceivedError(request, error)
            }
        }

        this.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams): Boolean {
                return true == webViewEventListener?.onShowFileChooser(fileChooserParams) {
                    filePathCallback.onReceiveValue(it)
                }
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                webViewEventListener?.onProgressChanged(newProgress)
            }

            override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                AlertDialog.Builder(view.context)
                    .setTitle("提示")
                    .setMessage(message)
                    .setPositiveButton("确定") { _, _ -> result.confirm() }
                    .setCancelable(false)
                    .create().show();
                return true
            }

            override fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
                AlertDialog.Builder(view.context)
                    .setTitle("提示")
                    .setMessage(message)
                    .setPositiveButton("确定") { _, _ -> result.confirm() }
                    .setNegativeButton("取消") { _, _ -> result.cancel() }
                    .setCancelable(false)
                    .create().show()
                return true
            }

            override fun onJsPrompt(view: WebView, url: String, message: String?, defaultValue: String?, result: JsPromptResult): Boolean {
                // 通过 prompt 来处理桥接信息
                val handled = BridgeDispatcher.dispatch(message, view.context as BridgeContext) {

                }
                if (handled) {
                    result.confirm("")
                    return true
                }

                // 处理 prompt
                val editText = EditText(view.context)
                editText.setText(defaultValue)
                editText.hint = "请输入信息"
                AlertDialog.Builder(view.context)
                    .setTitle("提示")
                    .setMessage(message)
                    .setView(editText)
                    .setPositiveButton("确定") { _, _ ->
                        val input = editText.text?.toString()
                        if (input.isNullOrEmpty()) {
                            result.confirm()
                        } else {
                            result.confirm(input)
                        }
                    }
                    .setNegativeButton("取消") { _, _ -> result.cancel() }
                    .setCancelable(false)
                    .create().show()
                return true
            }
        }

        // 处理长点击事件
        setOnLongClickListener {
            val result = hitTestResult
            val type = result.type
            val url = result.extra

            if (type != HitTestResult.IMAGE_TYPE) {
                return@setOnLongClickListener false
            }

            // TODO

            return@setOnLongClickListener true
        }
    }

    var touchX = 0
        private set

    var touchY = 0
        private set

    override var webViewEventListener: BridgeWebView.WebViewEventListener? = null

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (this.hasFocus()) {
            //先清理一下焦点，避免长按复制的时候无法取消掉复制框
            clearFocus()
        }
        touchX = event.rawX.toInt()
        touchY = event.rawY.toInt()
        return super.onInterceptTouchEvent(event)
    }

    override fun getView(): View = this

    override fun evaluateJavaScript(script: String, complete: (String?) -> Unit) {
        super.evaluateJavascript(script, complete)
    }

    override fun destroy() {
        this.removeAllViews()
        (parent as ViewGroup).removeView(this)
        super.destroy()
    }

    override fun loadUrl(url: URL) {
        super.loadUrl(url.toString())
    }

    override fun loadUri(uri: URI) {
        super.loadUrl(uri.toString())
    }

    private fun getResource(request: WebResourceRequest): WebResourceResponse? {
        return when (request.url.scheme) {
            "res" -> {
                // 加载 asset 资源
                var mimeType: String? = null
                val extension = MimeTypeMap.getFileExtensionFromUrl(request.url.toString())
                if (extension != null) {
                    mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                }
                if (mimeType == null) {
                    mimeType = "application/octet-stream"
                }

                val stream = Assetsx.getAsStream(this.context, URI.create(request.url.toString()))
                if (stream != null) {
                    return WebResourceResponse(mimeType, StandardCharsets.UTF_8.name(), stream)
                } else {
                    return WebResourceResponse(mimeType, StandardCharsets.UTF_8.name(), 404, "NOT FOUND", emptyMap(), ByteArrayInputStream(ByteArray(0)))
                }
            }
            "file" -> {
                // 加载 file 资源
                var mimeType: String? = null
                val extension = MimeTypeMap.getFileExtensionFromUrl(request.url.toString())
                if (extension != null) {
                    mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                }
                if (mimeType == null) {
                    mimeType = "application/octet-stream"
                }

                val file = request.url.toFile()
                if (file.exists()) {
                    return WebResourceResponse(mimeType, StandardCharsets.UTF_8.name(), Files.newInputStream(file.toPath(), StandardOpenOption.READ))
                } else {
                    return WebResourceResponse(mimeType, StandardCharsets.UTF_8.name(), 404, "NOT FOUND", emptyMap(), ByteArrayInputStream(ByteArray(0)))
                }
            }
            else -> null
        }
    }
}