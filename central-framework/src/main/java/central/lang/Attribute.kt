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

package central.lang

import java.io.Serializable
import java.util.*
import java.util.function.Supplier

/**
 * 属性
 *
 * @author Alan Yeh
 * @since 2022/12/09
 */
class Attribute<T>(
    /**
     * 标识
     */
    val code: String,
    /**
     * 值提供器
     */
    private val supplier: Supplier<T>?
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = -3642607092575713567L

        @JvmStatic
        fun <T> of(code: String, value: T) = Attribute(code) { value }

        @JvmStatic
        fun <T> of(code: String, value: Supplier<T>) = Attribute(code, value)

        @JvmStatic
        fun <T> of(code: String): Attribute<T> {
            return Attribute(code, null)
        }
    }

    /**
     * Get value from supplier
     */
    val value: T?
        get() = supplier?.get()

    /**
     * Get nonnull value from supplier
     */
    fun requireValue(): T {
        return Assertx.requireNotNull(this.value, ::IllegalStateException, "Cannot return null value form supplier")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class.java != other::class.java) return false
        if (other is Attribute<*>) {
            return this.code == other.code
        }
        return false
    }

    override fun hashCode(): Int = Objects.hashCode(this.code)

    override fun toString(): String = "Attribute(code: $code)"
}