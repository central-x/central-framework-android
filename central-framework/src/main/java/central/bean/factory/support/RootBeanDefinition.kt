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

package central.bean.factory.support

import central.bean.factory.BeanFactory
import central.bean.factory.FactoryBean
import central.bean.factory.config.*

/**
 * 用于指定 Bean 定义
 *
 * @author Alan Yeh
 * @since 2023/02/10
 */
class RootBeanDefinition : BeanDefinition {
    override val name: String
    override var type: Class<*>
    override var singleton: Boolean
    override var lazyInit: Boolean
    override var dependsOn: List<String>
    override var primary: Boolean
    override var factory: FactoryBean<*>

    constructor(type: Class<*>, beanFactory: BeanFactory) {
        this.name = type.simpleName.replaceFirstChar { it.lowercaseChar() }
        this.type = type

        // 判断是否单例
        val scope = type.getAnnotation(Scope::class.java)
        this.singleton = scope?.singleton ?: true

        // 是否延迟初始化
        val lazy = type.getAnnotation(LazyInit::class.java)
        this.lazyInit = lazy?.value ?: false

        // 依赖
        val dependsOn = type.getAnnotation(DependsOn::class.java)
        this.dependsOn = dependsOn?.value?.toList() ?: emptyList()

        // 是否主类型
        val primary = type.getAnnotation(Primary::class.java)
        this.primary = primary != null

        // 通过构造函数创建实例
        this.factory = ConstructorInvokingFactoryBean(this.type)
    }

    constructor(type: Class<*>, factory: FactoryBean<*>) {
        this.name = type.simpleName.replaceFirstChar { it.lowercaseChar() }
        this.type = type

        // 判断是否单例
        val scope = type.getAnnotation(Scope::class.java)
        this.singleton = scope?.singleton ?: true

        // 是否延迟初始化
        val lazy = type.getAnnotation(LazyInit::class.java)
        this.lazyInit = lazy?.value ?: false

        // 依赖
        val dependsOn = type.getAnnotation(DependsOn::class.java)
        this.dependsOn = dependsOn?.value?.toList() ?: emptyList()

        // 是否主类型
        val primary = type.getAnnotation(Primary::class.java)
        this.primary = primary != null

        // 通过指定工厂创建实例
        this.factory = factory
    }

    override fun toString(): String {
        return "${RootBeanDefinition::class.java.simpleName}(name=${this.name}, type=${this.type.name}, singleton=${this.singleton}, lazy=${this.lazyInit}, dependsOn=${this.dependsOn}, primary=${this.primary})"
    }
}