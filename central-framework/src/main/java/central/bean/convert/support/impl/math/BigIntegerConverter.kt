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

package central.bean.convert.support.impl.math

import central.bean.convert.support.ConvertException
import central.bean.convert.Converter
import java.math.BigInteger

/**
 * BigInteger Converter
 *
 * @author Alan Yeh
 * @since 2022/12/07
 */
object BigIntegerConverter : Converter<BigInteger> {
    override fun support(source: Class<*>): Boolean = when {
        source == BigInteger::class.java -> true
        Number::class.java.isAssignableFrom(source) -> true
        source == String::class.java -> true
        else -> false
    }

    override fun convert(source: Any): BigInteger? = when (source) {
        is BigInteger -> source
        is Number -> BigInteger.valueOf(source.toLong())
        is String -> BigInteger(source)
        else -> throw ConvertException(source, BigInteger::class.java)
    }
}