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

import java.util.concurrent.CopyOnWriteArrayList

/**
 * 优先属性源
 *
 * @author Alan Yeh
 * @since 2022/12/23
 */
class PriorityPropertySource : PropertySource {

    private val sources = CopyOnWriteArrayList<PropertySource>()

    /**
     * 判断是否包含指定的属性源
     */
    fun contains(name: String): Boolean {
        return this.sources.any { it.getName() == name }
    }

    /**
     * 获取指定的属性源
     */
    fun get(name: String): PropertySource {
        return this.sources.first { it.getName() == name }
    }

    /**
     * 将指定的属性源对象添加到最高优先级
     */
    fun addFirst(propertySource: PropertySource) {
        synchronized(this.sources) {
            remove(propertySource.getName())
            this.sources.add(0, propertySource)
        }
    }

    /**
     * 将定的属性源对象添加到最低优先级
     */
    fun addLast(propertySource: PropertySource) {
        synchronized(this.sources) {
            remove(propertySource.getName())
            this.sources.add(propertySource)
        }
    }

    /**
     * 移除指定的属性源
     */
    fun remove(propertySource: PropertySource) {
        this.sources.remove(propertySource)
    }

    /**
     * 移除指定名称的属性源
     */
    fun remove(name: String) {
        this.sources.removeAll { it.getName() == name }
    }

    /**
     * 获取属性列表大小
     */
    val size: Int
        get() = this.sources.size

    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun containsProperty(key: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getProperty(key: String): String? {
        TODO("Not yet implemented")
    }

    override fun getProperty(key: String, defaultValue: String): String {
        TODO("Not yet implemented")
    }

    override fun <T> getProperty(key: String, targetType: Class<T>): T? {
        TODO("Not yet implemented")
    }

    override fun <T> getProperty(key: String, targetType: Class<T>, defaultValue: T): T {
        TODO("Not yet implemented")
    }

    override fun getRequiredProperty(key: String): String {
        TODO("Not yet implemented")
    }

    override fun <T> getRequiredProperty(key: String, targetType: Class<T>): T {
        TODO("Not yet implemented")
    }

    override fun resolvePlaceholders(text: String): String {
        TODO("Not yet implemented")
    }

    override fun resolveRequiredPlaceholders(text: String): String {
        TODO("Not yet implemented")
    }
}