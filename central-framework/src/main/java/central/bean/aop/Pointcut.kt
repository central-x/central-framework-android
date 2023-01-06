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

package central.bean.aop

import java.lang.reflect.Method

/**
 * 用于指定切面点
 *
 * @author Alan Yeh
 * @since 2023/01/30
 */
interface Pointcut {
    /**
     * 用于判断指定的方法是否为切面点
     *
     * @param method 待判断的方法信息
     * @param targetClass 方法所在类
     * @param arguments 方法参数
     *
     * @return 是否执行切面操作
     */
    fun matches(method: Method, targetClass: Class<*>, arguments: Array<Any?>): Boolean

    /**
     * 执行切面操作
     *
     * @param invocation 方法调用信息
     * @return 方法调用结果
     */
    @Throws(Throwable::class)
    fun invoke(invocation: MethodInvocation): Any?
}