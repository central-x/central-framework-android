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

package central.lang.reflect.invoke

import central.bean.Orderable
import central.util.Context
import java.lang.reflect.Method
import java.lang.reflect.Parameter

/**
 * 参数解析器
 *
 * @author Alan Yeh
 * @since 2022/12/13
 */
interface ParameterResolver : Orderable {
    /**
     * 判断是否支持解析该参数
     *
     * @param clazz     方法所在的类
     * @param method    方法
     * @param parameter 待解析的参数
     */
    fun support(clazz: Class<*>, method: Method, parameter: Parameter): Boolean

    /**
     * 解析参数
     *
     *
     * @param clazz     方法所在的类
     * @param method    方法
     * @param parameter 待解析的参数
     * @param context   上下文
     * @return 解析后的参数
     */
    fun resolve(clazz: Class<*>, method: Method, parameter: Parameter, context: Context): Any?
}