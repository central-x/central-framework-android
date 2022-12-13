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
 * 方法调用
 *
 * @author Alan Yeh
 * @since 2023/01/30
 */
interface MethodInvocation {
    /**
     * 执行方法
     */
    fun proceed(): Any?

    /**
     * 使用指定的参数执行方法
     */
    fun proceed(arguments: Array<Any?>): Any?

    /**
     * 返回当前待执行的方法
     */
    fun getMethod(): Method

    /**
     * 返回当前待执行方法的参数
     */
    fun getArguments(): Array<Any?>

    /**
     * 返回当前待执行方法的对象
     */
    fun getTarget(): Any
}