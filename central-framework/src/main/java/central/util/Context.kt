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

package central.util

import java.util.concurrent.ConcurrentHashMap

/**
 * 上下文容器
 *
 * @author Alan Yeh
 * @since 2022/12/13
 */
class Context {
    private val context = ConcurrentHashMap<String, Any>()

    /**
     * 克隆上下文
     *
     * @param
     */
    fun clone(): Context {
        val context = this.context
        return Context().apply {
            this.context.putAll(context)
        }
    }

    /**
     * 判断是否存在键
     *
     * @param key 键
     */
    fun contains(key: String): Boolean = this.context.contains(key)

    /**
     * 获取值
     *
     * @param key 键
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String): T? = this.context[key] as? T

    /**
     * 获取值，如果值为空时将返回异常
     *
     * @param key 键
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> require(key: String): T = this.context[key] as T

    /**
     * 保存值
     *
     * @param key 键
     */
    fun set(key: String, value: Any?) {
        if (value == null) {
            this.context.remove(key)
        } else {
            this.context[key] = value
        }
    }

    /**
     * 移除键
     *
     * @param key 键
     */
    fun remove(key: String) = this.context.remove(key)


    /**
     * 判断是否存在键
     *
     * @param key 键
     */
    fun contains(key: Class<*>): Boolean = this.context.containsKey(key.canonicalName!!)

    /**
     * 获取值
     *
     * @param key 键
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: Class<T>): T? = this.context[key.canonicalName!!] as? T

    /**
     * 获取值，如果值为空时将返回异常
     *
     * @param key 键
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> require(key: Class<T>): T = this.context[key.canonicalName!!] as T

    /**
     * 保存值
     *
     * @param key 键
     */
    fun <T> set(key: Class<T>, value: T?) {
        if (value == null) {
            this.context.remove(key.canonicalName!!)
        } else {
            this.context[key.canonicalName!!] = value
        }
    }

    /**
     * 移除键
     *
     * @param key 键
     */
    fun remove(key: Class<*>) = this.context.remove(key.canonicalName!!)
}