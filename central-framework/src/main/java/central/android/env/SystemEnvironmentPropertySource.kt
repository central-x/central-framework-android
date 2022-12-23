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

package central.android.env

import android.os.Build
import central.bean.convert.ConversionService
import central.env.MapPropertySource
import central.env.PropertySource

/**
 * 系统环境变量
 *
 * @author Alan Yeh
 * @since 2022/12/22
 */
class SystemEnvironmentPropertySource(private val converter: ConversionService) : PropertySource {
    private val delegate: MapPropertySource

    init {
        val environment = mapOf<String, Any>(
            "product.model" to Build.MODEL,
            "product.name" to Build.PRODUCT,
            "product.device" to Build.DEVICE,
            "product.board" to Build.BOARD,
            "product.manufacturer" to Build.MANUFACTURER,
            "cpu.abi" to Build.SUPPORTED_ABIS
        )
        delegate = MapPropertySource("system", environment, converter)
    }

    override fun getName(): String = this.delegate.getName()

    override fun containsProperty(key: String): Boolean = this.delegate.containsProperty(key)

    override fun getProperty(key: String): String? = this.delegate.getProperty(key)

    override fun getProperty(key: String, defaultValue: String): String = this.delegate.getProperty(key, defaultValue)

    override fun <T> getProperty(key: String, targetType: Class<T>): T? = this.delegate.getProperty(key, targetType)

    override fun <T> getProperty(key: String, targetType: Class<T>, defaultValue: T): T = this.delegate.getProperty(key, targetType, defaultValue)

    override fun getRequiredProperty(key: String): String = this.delegate.getRequiredProperty(key)

    override fun <T> getRequiredProperty(key: String, targetType: Class<T>): T = this.delegate.getRequiredProperty(key, targetType)

    override fun resolvePlaceholders(text: String): String = this.delegate.resolvePlaceholders(text)

    override fun resolveRequiredPlaceholders(text: String): String = this.delegate.resolveRequiredPlaceholders(text)
}