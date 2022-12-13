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
 * Bean Factory
 *
 * @author Alan Yeh
 * @since 2022/12/14
 */
interface BeanFactory {

    /**
     * 根据 Bean 名称获取指定对象
     */
    fun <T> getBean(name: String): T?

    /**
     * 根据 Bean 名称获取指定对象，如果对象不存在，将抛出 NoSuchBeanException
     */
    @Throws(NoSuchBeanException::class)
    fun <T> requireBean(name: String): T {
        return getBean<T>(name) ?: throw NoSuchBeanException("Cannot find bean with name '$name'")
    }

    /**
     * 根据 Bean 名称及 Bean 类型获取指定对象
     */
    fun <T> getBean(name: String, requiredType: Class<T>): T?

    /**
     * 根据 Bean 名称及 Bean 类型获取指定对象，将抛出 NoSuchBeanException
     */
    @Throws(NoSuchBeanException::class)
    fun <T> requireBean(name: String, requiredType: Class<T>): T {
        return getBean(name, requiredType) ?: throw NoSuchBeanException("Cannot find bean with name '$name' and type '${requiredType.name}'")
    }

    /**
     * 根据 Bean 类型获取指定对象
     */
    fun <T> getBean(requiredType: Class<T>): T?

    /**
     * 根据 Bean 类型获取指定对象，将抛出 NoSuchBeanException
     */
    @Throws(NoSuchBeanException::class)
    fun <T> requireBean(requiredType: Class<T>): T {
        return getBean(requiredType) ?: throw NoSuchBeanException("Cannot find bean with type '${requiredType.name}'")
    }

    /**
     * 判断是否包含指定名称的 Bean
     */
    fun containsBean(name: String): Boolean

    /**
     * 获取指定名称的 Bean 的类型。如果没有找到指定的 Bean，则返回 null
     */
    fun getType(name: String): Class<*>?

    /**
     * 判断指定的 Bean 是否与指定的类型匹配
     *
     * @param name 待匹配的 Bean 名称
     * @param type 指定类型
     */
    @Throws(NoSuchBeanException::class)
    fun isTypeMatch(name: String, type: Class<*>): Boolean
}