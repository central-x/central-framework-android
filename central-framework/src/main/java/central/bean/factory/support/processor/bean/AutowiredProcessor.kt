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

package central.bean.factory.support.processor.bean

import central.bean.context.ApplicationContext
import central.bean.factory.Autowired
import central.bean.factory.NoSuchBeanException
import central.bean.factory.Qualifier
import central.bean.factory.config.BeanPostProcessor
import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * 用于处理 @Autowired 注入
 *
 * @author Alan Yeh
 * @see Autowired
 * @see Qualifier
 * @since 2023/01/19
 */
class AutowiredProcessor(private val applicationContext: ApplicationContext) : BeanPostProcessor {

    override fun processBeforeInitialization(name: String, bean: Any): Any {
        this.injectFields(bean)
        this.injectMethods(bean)

        return bean
    }

    /**
     * 字段注入
     */
    private fun injectFields(bean: Any) {
        for (field in bean::class.java.declaredFields) {
            val autowired = field.getAnnotation(Autowired::class.java)
            val qualifier = field.getAnnotation(Qualifier::class.java)

            if (autowired == null && qualifier == null) {
                // 如果没有出现 autowired 两个注解，则忽略该字段的注入
                continue
            }

            field.isAccessible = true
            if (qualifier != null) {
                val fieldBean = if (autowired?.required != false) {
                    this.applicationContext.requireBean(qualifier.value, field.type)
                } else {
                    this.applicationContext.getBean(qualifier.value, field.type)
                }

                field.set(bean, fieldBean)
            } else {
                val fieldBean = if (autowired?.required != false) {
                    this.applicationContext.requireBean(field.type)
                } else {
                    this.applicationContext.getBean(field.type)
                }

                field.set(bean, fieldBean)
            }
        }
    }

    /**
     * 方法注入
     */
    private fun injectMethods(bean: Any) {
        for (method in bean::class.java.declaredMethods) {
            if (Modifier.isStatic(method.modifiers)) {
                // 静态方法一般情况下不执行注入，但是在 kotlin 里，如果直接在 private lateinit var propertyField 上面标注 @Autowired 注解的话，
                // kotlin 会为这个字段生成一个 getter 方法(private static void xxx.xxx.getPropertyField$annotations())，用于保存注解信息
                if (method.name.startsWith("get") && method.name.endsWith("\$annotations") && method.parameterCount == 0) {
                    val autowired = method.getAnnotation(Autowired::class.java)
                    val qualifier = method.getAnnotation(Qualifier::class.java)
                    if (autowired == null && qualifier == null) {
                        continue
                    }

                    var field: Field? = null
                    try {
                        field = bean::class.java.getDeclaredField(method.name.removePrefix("get").removeSuffix("\$annotations").replaceFirstChar { it.lowercaseChar() })
                    } catch (ignored: NoSuchBeanException){}
                    if (field == null){
                        continue
                    }

                    val injectBean = if (qualifier != null) {
                        if (autowired?.required != false) {
                            this.applicationContext.requireBean(qualifier.value, field.type)
                        } else {
                            this.applicationContext.getBean(qualifier.value, field.type)
                        }
                    } else {
                        if (autowired?.required != false) {
                            this.applicationContext.requireBean(field.type)
                        } else {
                            this.applicationContext.getBean(field.type)
                        }
                    }

                    field.isAccessible = true
                    field.set(bean, injectBean)
                }
                continue
            }

            if (!Modifier.isPublic(method.modifiers)) {
                // 非公开方法不执行注入
                continue
            }

            // 如果是 setter 方法（方法名以 set 开头，参数只有一个），autowired 或 qualifier 都可以用于标注该方法需要被注入
            if (method.name.startsWith("set") && method.parameterCount == 1) {
                val autowired = method.getAnnotation(Autowired::class.java)
                val qualifier = method.getAnnotation(Qualifier::class.java)
                if (autowired == null && qualifier == null) {
                    continue
                }
                if (qualifier != null) {
                    val injectBean = if (autowired?.required != false) {
                        this.applicationContext.requireBean(qualifier.value, method.parameters[0].type)
                    } else {
                        this.applicationContext.getBean(qualifier.value, method.parameters[0].type)
                    }
                    method.invoke(bean, injectBean)
                } else {
                    val injectBean = if (autowired?.required != false) {
                        this.applicationContext.requireBean(method.parameters[0].type)
                    } else {
                        this.applicationContext.getBean(method.parameters[0].type)
                    }

                    method.invoke(bean, injectBean)
                }
                continue
            } else if (method.isAnnotationPresent(Autowired::class.java)) {
                // 非 setter 方法，则只要方法上带上 @Autowired，都执行注入。注入时，参数的注入方式由参数的 @Qualifier 注解决定
                val arguments = arrayOfNulls<Any>(method.parameterCount)

                method.parameters.forEachIndexed { index, parameter ->
                    val autowired = parameter.getAnnotation(Autowired::class.java)
                    val qualifier = parameter.getAnnotation(Qualifier::class.java)

                    val injectBean = if (qualifier != null) {
                        if (autowired?.required != false) {
                            this.applicationContext.requireBean(qualifier.value, method.parameters[index].type)
                        } else {
                            this.applicationContext.getBean(qualifier.value, method.parameters[index].type)
                        }
                    } else {
                        if (autowired?.required != false) {
                            this.applicationContext.requireBean(method.parameters[index].type)
                        } else {
                            this.applicationContext.getBean(method.parameters[index].type)
                        }
                    }

                    arguments[index] = injectBean
                }

                method.invoke(bean, *arguments)
            }
        }
    }
}