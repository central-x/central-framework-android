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

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset

/*
 * String 扩展
 *
 * @author Alan Yeh
 * @since 2023/04/24
 */


/**
 * 添加前缀
 *
 * @param prefix 前缀
 */
fun String.addPrefix(prefix: String): String = prefix + this

/**
 * 添加后缀
 */
fun String.addSuffix(suffix: String): String = this + suffix

/**
 * 移除前缀
 *
 * @param prefix 前缀
 */
fun String.removePrefix(prefix: String): String {
    return if (this.startsWith(prefix)) this.substring(prefix.length) else this
}

/**
 * 移除后缀
 *
 * @param suffix 后缀
 */
fun String.removeSuffix(suffix: String): String {
    return if (this.endsWith(suffix)) this.substring(0, this.length - suffix.length) else this
}

/**
 * URL 编码
 */
fun String.encode(charset: Charset): String = URLEncoder.encode(this, charset.name())

/**
 * URL 解码
 */
fun String.decode(charset: Charset): String = URLDecoder.decode(this, charset.name())

/**
 * 替换 String 占位符 {}
 *
 * @param args 占位符值
 */
fun String.place(vararg args: Any?): String {
    if (args.isEmpty()) {
        return this
    }

    val result = StringBuilder(this.length + 50)

    var i = 0
    for (item in args) {
        val j = this.indexOf("{}", i)
        if (j == -1) {
            // 没有找到
            if (i == 0) {
                // 当前是个普通的字符串
                return this
            } else {
                result.append(this, i, this.length)
                i = this.length
            }
        } else {
            result.append(this, i, j)
            result.append(item ?: "")
            i = j + 2
        }
    }

    if (i < this.length) {
        result.append(this, i, this.length)
    }

    return result.toString()
}