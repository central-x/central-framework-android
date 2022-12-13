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

package central.android.bridge.core.filter.invoke.resolver

import central.android.bridge.BridgeParam
import central.lang.reflect.invoke.AnnotatedParameterResolve
import central.util.Context
import java.lang.reflect.Method
import java.lang.reflect.Parameter

/**
 * 用于解析用户传递的参数
 *
 * @see BridgeParam
 * @author Alan Yeh
 * @since 2022/12/13
 */
class BridgeParamParameterResolver : AnnotatedParameterResolve(BridgeParam::class.java) {
    override var order: Int? = Int.MAX_VALUE

    override fun resolve(clazz: Class<*>, method: Method, parameter: Parameter, context: Context): Any? {
        TODO("Not yet implemented")
    }
}