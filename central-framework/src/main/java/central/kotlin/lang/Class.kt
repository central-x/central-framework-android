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

package central.kotlin.lang

/*
 * Class 扩展
 *
 * @author Alan Yeh
 * @since 2023/04/24
 */

/**
 * 判断类型是不是Number
 */
fun <T> Class<T>.isAssignableFromNumber(): Boolean {
    return this == Number::class.java
            || this.isAssignableFrom(Double::class.java) || this.isAssignableFrom(Double::class.javaObjectType)
            || this.isAssignableFrom(Float::class.java) || this.isAssignableFrom(Float::class.javaObjectType)
            || this.isAssignableFrom(Long::class.java) || this.isAssignableFrom(Long::class.javaObjectType)
            || this.isAssignableFrom(Int::class.java) || this.isAssignableFrom(Int::class.javaObjectType)
            || this.isAssignableFrom(Short::class.java) || this.isAssignableFrom(Short::class.javaObjectType)
            || this.isAssignableFrom(Byte::class.java) || this.isAssignableFrom(Byte::class.javaObjectType)
}

/**
 * 判断类型是不是Boolean
 */
fun <T> Class<T>.isAssignableFromBoolean(): Boolean {
    return this.isAssignableFrom(Boolean::class.java) || this.isAssignableFrom(Boolean::class.javaObjectType)
}