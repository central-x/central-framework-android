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
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.nio.charset.Charset

/**
 * Gson 序列化实现
 *
 * @author Alan Yeh
 * @since 2023/04/23
 */
class GsonSerializer : JsonSerializer {

    override fun serialize(any: Any, output: OutputStream, charset: Charset, formatted: Boolean) {
        val gson = if (formatted) {
            GsonBuilder().setPrettyPrinting().create()
        } else {
            Gson()
        }
        val writer = OutputStreamWriter(output, charset)
        gson.toJson(any, writer)
        writer.flush()
    }

    override fun <T> deserialize(reference: TypeReference<T>, input: InputStream, charset: Charset): T {
        val gson = Gson()
        return gson.fromJson(InputStreamReader(input, charset), reference.type)
    }
}