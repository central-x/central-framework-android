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

import central.bean.context.ApplicationContext
import central.bean.context.ApplicationContextAware
import central.bean.convert.ConversionService
import central.bean.factory.*
import java.lang.reflect.Constructor

/**
 * 通过构造函数创建 Bean
 *
 * @author Alan Yeh
 * @since 2022/12/22
 */
class ConstructorInvokingFactoryBean<T>(private val type: Class<T>) : FactoryBean<T>, ApplicationContextAware {

    override lateinit var applicationContext: ApplicationContext

    /**
     * 标记该实例是否是单例
     */
    override val singleton: Boolean = type.getAnnotation(Scope::class.java)?.singleton ?: true

    /**
     * 标记该实例是否延迟初始化（在需要时才初始化）
     */
    override val lazy: Boolean = type.getAnnotation(LazyInit::class.java)?.value ?: true

    override fun getBean(): T {
        // 选择构造函数
        val constructor = this.determineConstructor(type)

        // 构建参数列表
        val parameters = this.resolveParameters(constructor)

        // 创建实例
        return constructor.newInstance(*parameters)
    }

    @Suppress("UNCHECKED_CAST")
    private fun determineConstructor(type: Class<T>): Constructor<T> {
        // 默认无参构造函数
        val candidates = mutableListOf<Constructor<*>>()

        if (this.type.constructors.isNullOrEmpty()) {
            // 没有公开的构造函数
            throw BeanCreationException("Resolution of declared constructors on bean class [${type.name}] failed: Cannot find any public constructor")
        }

        if (this.type.constructors.size == 1) {
            // 如果只有一个构造函数，那么就只能使用该构造函数了
            candidates.add(this.type.constructors[0])
        } else {
            candidates.addAll(this.type.constructors)
        }

        // 优先使用带 @Autowired 注解的构造函数
        // 其次使用参数多的构造函数
        candidates.sortedBy { it.parameterCount + if (it.isAnnotationPresent(Autowired::class.java)) 10000 else 0 }

        return candidates.last() as Constructor<T>
    }

    private fun resolveParameters(constructor: Constructor<T>): Array<Any?> {
        val converter = this.applicationContext.requireBean(ConversionService::class.java)

        val parameters = arrayOfNulls<Any>(constructor.parameterCount)

        constructor.parameters.forEachIndexed { index, parameter ->
            val value = parameter.getAnnotation(Value::class.java)
            if (value != null) {
                val str = if (value.required) {
                    applicationContext.environment.resolveRequiredPlaceholders(value.value)
                } else {
                    applicationContext.environment.resolvePlaceholders(value.value)
                }

                parameters[index] = converter.convert(str, parameter.type)
            } else {
                val autowired = parameter.getAnnotation(Autowired::class.java)
                val qualifier = parameter.getAnnotation(Qualifier::class.java)
                if (qualifier != null) {
                    // 根据实体名获取 Bean
                    parameters[index] = this.applicationContext.getBean(qualifier.value, parameter.type)
                } else {
                    // 根据类型获取 Bean
                    parameters[index] = this.applicationContext.getBean(parameter.type)
                }

                if (parameters[index] == null && autowired?.required == true) {
                    throw BeanCreationException("Cannot find any beans for parameter [${parameter.name}] in constructor [$constructor]")
                }
            }
        }

        return parameters
    }

    override fun getBeanType(): Class<T> {
        return type
    }
}