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

package central.env

/**
 * 属性源
 *
 * @author Alan Yeh
 * @since 2022/12/22
 */
interface PropertySource {

    /**
     * 属性源名称
     */
    fun getName(): String

    /**
     * 返回是否包含指定的属性
     *
     * @param key 属性名
     */
    fun containsProperty(key: String): Boolean

    /**
     * 返回指定的属性值
     *
     * @param key 属性名
     */
    fun getProperty(key: String): String?

    /**
     * 返回指定的属性
     *
     * @param key 属性名
     * @throws IllegalStateException 如果属性不存在，将被抛出此异常
     */
    @Throws(IllegalStateException::class)
    fun getRequiredProperty(key: String): String

    /**
     * 返回指定的属性值，如果属性不存在，则返回默认值
     *
     * @param key 属性名
     * @param defaultValue 默认值
     */
    fun getProperty(key: String, defaultValue: String): String

    /**
     * 返回指定的属性值，并将该属性的值转换为指定类型
     *
     * @param key 属性名
     * @param targetType 类型
     */
    fun <T> getProperty(key: String, targetType: Class<T>): T?

    /**
     * 返回指定的属性值，并将该属性的值转换为指定类型。如果属性不存在，则返回默认值
     *
     * @param key 属性名
     * @param targetType 类型
     * @param defaultValue 默认值
     */
    fun <T> getProperty(key: String, targetType: Class<T>, defaultValue: T): T

    /**
     * 返回指定的属性值，并将该属性的值转换为指定类型
     *
     * @param key 属性名
     * @param targetType 类型
     * @throws IllegalStateException 如果属性不存在，将被抛出此异常
     */
    @Throws(IllegalStateException::class)
    fun <T> getRequiredProperty(key: String, targetType: Class<T>): T

    /**
     * 将字符串中的 `${...}` 占位符替换为对应的属性。如果属性不存在，则替换为空字符串
     *
     * @param text 待替换的文本
     */
    fun resolvePlaceholders(text: String): String

    /**
     * 将字符串中的 `${...}` 占位符替换为对应的属性。如果属性不存在，将抛出异常
     *
     * @param text 待替换的文本
     * @throws IllegalStateException 如果属性不存在，将被抛出此异常
     */
    fun resolveRequiredPlaceholders(text: String): String
}