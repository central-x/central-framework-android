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

package central.util.json

import central.lang.reflect.TypeReference
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Json 序列化工具
 *
 * @author Alan Yeh
 * @since 2022/12/27
 */
interface JsonSerializer {

    /**
     * 将对象序列化成 Json 字符串
     *
     * @param any 任意对象
     */
    fun serialize(any: Any): String {
        return serialize(any, false)
    }

    /**
     * 将对象序列化成 Json 字符串
     *
     * @param any 任意对象
     * @param formatted 是否格式化
     */
    fun serialize(any: Any, formatted: Boolean = false): String {
        val output = ByteArrayOutputStream()
        this.serialize(any, output, StandardCharsets.UTF_8, formatted)
        return String(output.toByteArray(), StandardCharsets.UTF_8)
    }

    /**
     * 将对象序列化成 Json
     *
     * @param any 任意对象
     * @param output 序列化输出流
     * @param charset 字符集
     * @param formatted 是否格式化
     */
    fun serialize(any: Any, output: OutputStream, charset: Charset = StandardCharsets.UTF_8, formatted: Boolean = false)

    /**
     * 将 JSON 字符串反序列化成对象
     *
     * @param reference 类型引用
     * @param json JSON 字符串
     */
    fun <T> deserialize(reference: TypeReference<T>, json: String): T {
        return this.deserialize(reference, ByteArrayInputStream(json.toByteArray()))
    }

    /**
     * 将 JSON 字符串反序列化成对象
     *
     * @param type 类型
     * @param json JSON 字符串
     */
    fun <T> deserialize(type: Class<T>, json: String): T {
        return this.deserialize(TypeReference.of(type), json)
    }

    /**
     * 将 JSON 字节码反序列化成对象
     *
     * @param reference 类型引用
     * @param input 字节流
     * @param charset 字符集
     */
    fun <T> deserialize(reference: TypeReference<T>, input: InputStream, charset: Charset = StandardCharsets.UTF_8): T
}