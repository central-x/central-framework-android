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

package central.android.bridge.component.sms

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import central.android.bridge.BridgeComponent
import central.android.bridge.component.sms.param.SendParams
import central.android.bridge.core.annotation.BridgeMethod
import central.android.bridge.core.annotation.BridgePermission
import central.validation.Validated

/**
 * Sms Component
 *
 * @author Alan Yeh
 * @since 2022/12/09
 */
class SmsComponent : BridgeComponent() {

    /**
     * 发送信息
     */
    @BridgeMethod
    @BridgePermission([Manifest.permission.SEND_SMS])
    fun send(@Validated params: SendParams, activity: Activity) {
        val phones = params.numbers.joinToString(",")

        val uri = Uri.parse("smsto:$phones")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra("sms_body", params.body)
        activity.startActivity(intent)
    }
}