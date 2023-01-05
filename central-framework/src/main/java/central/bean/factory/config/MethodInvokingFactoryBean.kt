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
import central.bean.factory.*
import central.bean.factory.support.BeanReference
import central.convert.Converter
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * 用于通过调用方法产生 Bean
 *
 * @author Alan Yeh
 * @since 2022/12/21
 */
class MethodInvokingFactoryBean(private val instance: Any, private val method: Method) : FactoryBean<Any>, ApplicationContextAware {

    override lateinit var applicationContext: ApplicationContext

    /**
     * 标记该方法产生的实例是否是单例
     */
    override val singleton: Boolean = method.getAnnotation(Scope::class.java)?.singleton ?: true

    /**
     * 标记该方法是否延迟初始化（在需要时才初始化）
     */
    override val lazy: Boolean = method.getAnnotation(LazyInit::class.java)?.value ?: true

    /**
     * 获取 Bean 实例
     */
    override fun getBean(): Any {
        val parameters = this.resolveParameters(this.method)

        val result = if (Modifier.isStatic(this.method.modifiers)) {
            method.invoke(this.method.declaringClass, * parameters)
        } else if (instance is BeanReference) {
            val target = this.applicationContext.requireBean<Any>(instance.definition.name)
            method.invoke(target, *parameters)
        } else {
            method.invoke(instance, *parameters)
        }

        return result ?: throw IllegalStateException("Method '${method.declaringClass.name}#${method.name}' return a null object")
    }

    /**
     * 解析参数
     */
    private fun resolveParameters(method: Method): Array<Any?> {
        val converter = this.applicationContext.requireBean(Converter::class.java)

        val parameters = arrayOfNulls<Any>(method.parameterCount)

        method.parameters.forEachIndexed { index, parameter ->
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
                    throw BeanCreationException("Cannot find any beans for parameter [${parameter.name}] in method [$method]")
                }
            }
        }

        return parameters
    }

    override fun getBeanType(): Class<*> {
        return this.method.returnType
    }
}