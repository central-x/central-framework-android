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

import central.convert.Converter
import central.lang.Assertx
import java.util.*

/**
 * 配置文件变量
 *
 * @author Alan Yeh
 * @since 2022/12/22
 */
class PropertiesSource(private val name: String, private val properties: Properties, private val converter: Converter) : PropertySource {

    override fun getName(): String = this.name

    override fun containsProperty(key: String): Boolean {
        return properties.containsKey(key)
    }

    override fun getProperty(key: String): String? {
        return properties.getProperty(key)
    }

    override fun getProperty(key: String, defaultValue: String): String {
        return properties.getProperty(key, defaultValue)
    }

    override fun <T> getProperty(key: String, targetType: Class<T>): T? {
        val value = getProperty(key) ?: return null
        return converter.convert(value, targetType)
    }

    override fun <T> getProperty(key: String, targetType: Class<T>, defaultValue: T): T {
        return getProperty(key, targetType) ?: defaultValue
    }

    override fun getRequiredProperty(key: String): String {
        return Assertx.requireNotNull(this.getProperty(key), ::IllegalStateException, "Missing required property '$key'")
    }

    override fun <T> getRequiredProperty(key: String, targetType: Class<T>): T {
        return Assertx.requireNotNull(this.getProperty(key, targetType), ::IllegalStateException, "Missing required property '$key'")
    }

    override fun resolvePlaceholders(text: String): String {
        TODO("Not yet implemented")
    }

    override fun resolveRequiredPlaceholders(text: String): String {
        TODO("Not yet implemented")
    }
}