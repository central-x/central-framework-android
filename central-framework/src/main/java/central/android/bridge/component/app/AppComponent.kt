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

package central.android.bridge.component.app

import android.app.Application
import android.content.pm.PackageManager
import central.android.bridge.BridgeComponent
import central.android.bridge.core.annotation.BridgeGetter

/**
 * App Component
 *
 * @author Alan Yeh
 * @since 2022/12/09
 */
class AppComponent : BridgeComponent() {
    /**
     * 获取应用名称
     *
     * ```
     * var app = bridge.require("app");
     * val name = app.name;
     * ```
     */
    @BridgeGetter
    fun getName(application: Application): CharSequence? {
        val packageManager = application.packageManager
        val applicationInfo = packageManager?.getApplicationInfo(application.packageName, 0)
        return if (applicationInfo != null) {
            packageManager.getApplicationLabel(applicationInfo)
        } else {
            null
        }
    }

    /**
     * 获取应用包名
     */
    @BridgeGetter
    fun getBundle(application: Application): String? {
        return application.packageName
    }

    /**
     * 获取应用版本号
     */
    @BridgeGetter
    fun getVersion(application: Application): String? {
        val packageManager = application.packageManager
        val packageInfo = packageManager?.getPackageInfo(application.packageName, PackageManager.GET_CONFIGURATIONS)
        return packageInfo?.versionName
    }
}