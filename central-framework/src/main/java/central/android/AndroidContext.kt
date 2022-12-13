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

package central.android

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * Android 上下文
 *
 * @author Alan Yeh
 * @since 2022/12/19
 */
interface AndroidContext {
    /**
     * 获取 Android 上下文
     */
    fun obtainContext(): Context

    /**
     * 获取当前 Activity 上下文。如果当前上下文不是 Activity，则返回空
     */
    fun obtainActivity(): Activity? {
        return null
    }

    /**
     * 获取应用上下文
     */
    fun obtainApplication(): Application

    /**
     * 启动一个新的 Activity，并在 onActivityResult 中监听结果
     *
     * 如果当前上下文无法打开 Activity，则此方法可能为空
     */
    fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle? = null, listener: ActivityResultListener? = null) {}
}

interface ActivityResultListener {
    fun onActivityResult(requestCode: Int, data: Intent?) {}
    fun onNewIntent(intent: Intent?) {}
}