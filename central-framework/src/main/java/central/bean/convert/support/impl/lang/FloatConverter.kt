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

package central.bean.convert.support.impl.lang

import central.bean.convert.support.ConvertException
import central.bean.convert.support.Converter

/**
 * Float Convert
 *
 * @author Alan Yeh
 * @since 2022/12/07
 */
object FloatConverter : Converter<Float> {
    override fun support(source: Class<*>): Boolean = when {
        source == Float::class.javaObjectType -> true
        source == String::class.java -> true
        Number::class.java.isAssignableFrom(source) -> true
        else -> false
    }

    override fun convert(source: Any): Float? = when (source) {
        is Float -> source
        is Number -> source.toFloat()
        is String -> source.toFloat()
        else -> throw ConvertException(source, Float::class.javaObjectType)
    }
}