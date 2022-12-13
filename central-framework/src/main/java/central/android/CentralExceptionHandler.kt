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

import android.app.Application
import android.widget.Toast
import central.bean.factory.Autowired
import central.bean.factory.DestroyableBean
import central.bean.factory.InitializingBean
import central.bean.factory.config.Component

/**
 * 全局异常捕捉
 *
 * @author Alan Yeh
 * @since 2022/12/10
 */
@Component
class CentralExceptionHandler : Thread.UncaughtExceptionHandler, InitializingBean, DestroyableBean {

    @Autowired
    private lateinit var context: Application

    private var defaultCrashHandler: Thread.UncaughtExceptionHandler? = null

    /**
     * 初始化
     */
    override fun initialize() {
        this.defaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
        // 替换系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 销毁
     */
    override fun destroy() {
        if (defaultCrashHandler != null){
            Thread.setDefaultUncaughtExceptionHandler(defaultCrashHandler)
        }
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        Toast.makeText(context, "程序出现异常: " + throwable.localizedMessage, Toast.LENGTH_LONG).show()
        defaultCrashHandler?.uncaughtException(thread, throwable)
    }
}