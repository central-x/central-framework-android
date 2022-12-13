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

import central.util.Context
import java.lang.reflect.Method
import java.lang.reflect.Parameter

/**
 * 方法调用
 *
 * @author Alan Yeh
 * @since 2022/12/13
 */
class Invocation(
    private val method: Method
) {
    private val clazz: Class<*>
    private val parameters: Array<Parameter>
    private val resolves = mutableListOf<ParameterResolver>()

    init {
        this.clazz = method.declaringClass
        this.parameters = method.parameters
    }

    /**
     * 添加参数解析器
     *
     * @param resolves 参数解析器
     */
    fun resolves(resolves: List<ParameterResolver>): Invocation {
        this.resolves.addAll(resolves)
        this.resolves.sortBy { it.order }
        return this
    }

    /**
     * 调用方法
     *
     * 参数解析器会从调用上下文中解析需要的参数
     *
     * @param source  待调用的对象
     * @param context 调用上下文
     * @return 调用结果
     */
    fun invoke(source: Any, context: Context): Any? {
        // 构建调用参数列表
        val args = Array<Any?>(this.parameters.size) {}

        // 绑定参数
        parameters.forEachIndexed { index, parameter ->
            for (resolver in this.resolves) {
                if (resolver.support(this.clazz, this.method, parameter)) {
                    args[index] = resolver.resolve(this.clazz, this.method, parameter, context)
                }
            }
        }

        // 方法调用
        return this.method.invoke(source, *args)
    }
}