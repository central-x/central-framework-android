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

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import central.android.R
import central.bean.factory.Autowired
import java.net.URI
import java.security.KeyPair

/**
 * 桥接浏览器
 *
 * @author Alan Yeh
 * @since 2022/12/10
 */
class BridgeWebActivity : AppCompatActivity(), BridgeContext {

    override fun obtainContext(): Context = this

    override fun obtainActivity(): Activity = this

    override fun obtainApplication(): Application = this.application

    override fun obtainWebView(): BridgeWebView = this.webView

    companion object {
        private const val URI = "BridgeWebActivity.uri"
        private const val MAIN = "BridgeWebActivity.main"

        /**
         * 创建 Intent
         *
         * @param context 上下文
         * @param uri 桥接浏览器需要打开的地址
         * @param main 是否主页面。如果是主页面的话，按返回键会提示是否退出应用
         */
        @JvmStatic
        fun intent(context: Context, uri: URI, main: Boolean = false): Intent {
            return Intent().apply {
                setClass(context, BridgeWebActivity::class.java)
                putExtra(URI, uri.toString())
                putExtra(MAIN, main)
            }
        }
    }

    @Autowired
    private lateinit var keyPair: KeyPair

    private lateinit var webView: BridgeWebView
    private var main = false // 是否主页面（如果是主页面的话，按返回键会提示是否退出应用）

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        this.main = this.intent.getBooleanExtra(MAIN, false)
        this.initWebView()

        var indexUri: String? = null
        try {
            val activityInfo = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
            indexUri = activityInfo.metaData?.getString("uri")
        } catch (ignored: PackageManager.NameNotFoundException) {
        }

        if (indexUri == null) {
            indexUri = this.intent.getStringExtra(URI)
        }

        this.webView.loadUri(URI(indexUri))
    }

    /**
     * 初始化 WebView
     */
    private fun initWebView() {
        setContentView(R.layout.activity_bridge_web)
        val root = findViewById<RelativeLayout>(R.id.layout_main_bridge)
        this.webView = root.findViewById(R.id.webview)

        this.webView.webViewEventListener = object : BridgeWebView.WebViewEventListener {
//            override fun onShowFileChooser(fileChooserParams: WebChromeClient.FileChooserParams, complete: (Array<Uri>?) -> Unit): Boolean {
//                // 打开系统文件浏览器
//                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
//                    addCategory(Intent.CATEGORY_OPENABLE)
//                    // TODO 测试
//                    type = if (fileChooserParams.acceptTypes.isNullOrEmpty()) "*/*" else fileChooserParams.acceptTypes.joinToString(",")
//                }
//
//                registerForActivityResult()
//            }
        }
    }
}