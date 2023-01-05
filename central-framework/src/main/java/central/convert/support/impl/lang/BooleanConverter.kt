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

package central.convert.support.impl.lang

import central.convert.ConvertException
import central.convert.TypeConverter

/**
 * Boolean Converter
 *
 * @author Alan Yeh
 * @since 2022/12/07
 */
class BooleanConverter : TypeConverter<Boolean> {
    override fun support(source: Class<*>): Boolean = when {
        source == Boolean::class.javaObjectType -> true
        source == String::class.java -> true
        Number::class.java.isAssignableFrom(source) -> true
        else -> false
    }

    override fun convert(source: Any): Boolean? = when (source) {
        is Boolean -> source
        is String -> when {
            "true".equals(source, true) -> true
            "1" == source -> true
            else -> false
        }
        is Number -> source.toInt() != 0
        else -> throw ConvertException(source, Boolean::class.javaObjectType)
    }
}