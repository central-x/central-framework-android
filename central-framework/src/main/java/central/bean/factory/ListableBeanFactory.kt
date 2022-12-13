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

package central.bean.factory

/**
 * 可枚举的 Bean 工厂
 *
 * @author Alan Yeh
 * @since 2022/12/14
 */
interface ListableBeanFactory : BeanFactory {

    /**
     * 获取所有符合指定类型的 Bean
     *
     * beanName -> bean
     */
    fun <T> getBeansOfType(requiredType: Class<T>): Map<String, T>

    /**
     * 获取所有符合指定类型的 Bean 名称
     */
    fun getBeanNamesForType(type: Class<*>): List<String>

    /**
     * 获取所有符合指定类型的 Bean 名称
     *
     * @param type Bean 类型
     * @param includeNonSingletons 是否包含非单例的 Bean
     * @param allowEagerInit 是否初始化
     */
    fun getBeanNamesForType(type: Class<*>, includeNonSingletons: Boolean, allowEagerInit: Boolean): List<String>

    /**
     * 获取所有带指定注解的 Bean
     *
     * beanName -> bean
     */
    fun getBeansWithAnnotations(annotationTypes: List<Class<out Annotation>>): Map<String, Any>
}