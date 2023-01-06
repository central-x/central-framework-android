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

package central.bean.aop.support

import central.bean.aop.MethodInvocation
import central.bean.aop.Pointcut
import java.lang.reflect.Method

/**
 * 标准方法调用
 *
 * @author Alan Yeh
 * @since 2023/01/30
 */
class GenericMethodInvocation(private val target: Any, private val method: Method, private val arguments: Array<Any?>, private val interceptors: List<Pointcut>, private val index: Int) : MethodInvocation {

    constructor(target: Any, method: Method, arguments: Array<Any?>) : this(target, method, arguments, emptyList(), 0)

    override fun proceed(): Any? {
        return if (index >= interceptors.size) {
            this.method.invoke(this.target, *this.arguments)
        } else {
            val invocation = GenericMethodInvocation(this.target, this.method, this.arguments, this.interceptors, this.index + 1)
            this.interceptors[this.index + 1].invoke(invocation)
        }
    }

    override fun proceed(arguments: Array<Any?>): Any? {
        return if (index >= interceptors.size) {
            this.method.invoke(this.target, *arguments)
        } else {
            val invocation = GenericMethodInvocation(this.target, this.method, arguments, this.interceptors, this.index + 1)
            return this.interceptors[this.index + 1].invoke(invocation)
        }
    }

    override fun getMethod(): Method {
        return this.method
    }

    override fun getTarget(): Any {
        return this.target
    }

    override fun getArguments(): Array<Any?> {
        return this.arguments
    }
}