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

package central.bean.factory.config

import central.bean.factory.FactoryBean

/**
 * Bean 定义
 *
 * @author Alan Yeh
 * @since 2022/12/14
 */
interface BeanDefinition {
    /**
     * 称称
     */
    val name: String

    /**
     * Bean 类型
     */
    var type: Class<*>

    /**
     * 是否单例
     */
    var singleton: Boolean

    /**
     * 是否延迟初始化
     */
    var lazyInit: Boolean

    /**
     * 当前的 Bean 依赖的其他 Bean 的名称
     */
    var dependsOn: List<String>

    /**
     * 在注入时，当前的 Bean 是否是主要候选
     */
    var primary: Boolean

    /**
     * 工厂
     */
    var factory: FactoryBean<*>

    /**
     * 当前的 Bean 定义是否与指定的类型匹配
     */
    fun isTypeMatched(type: Class<*>): Boolean {
        return this.type == type || type.isAssignableFrom(this.type)
    }
}