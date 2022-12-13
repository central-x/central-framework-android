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

package central.io

import java.io.IOException
import java.io.InputStream
import java.net.URI

/**
 * 资源
 *
 * @author Alan Yeh
 * @since 2022/12/22
 */
interface Resource {
    /**
     * 资源是否存在
     */
    fun isExists(): Boolean

    /**
     * 用于指定资源是否可以返回 InputStream
     */
    fun isReadable(): Boolean {
        return isExists()
    }

    /**
     * 获取 URI
     */
    fun getURI(): URI

    /**
     * 获取资源长度
     *
     * 如果无法获取资源长度，将返回 -1，表示资源长度未知
     */
    @Throws(IOException::class)
    fun getContentLength(): Long

    /**
     * 获取资源文件名
     */
    fun getFilename(): String?

    /**
     * 获取流
     */
    fun getInputStream(): InputStream?
}