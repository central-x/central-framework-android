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

package central.android

import central.io.ProtocolResourceLoader
import central.io.Resource
import central.lang.Assertx
import java.io.InputStream
import java.net.URI
import java.nio.file.Path

/**
 * Android 本地资源
 *
 * @author Alan Yeh
 * @since 2023/02/14
 */
class AndroidFileSystemResourceLoader(private val path: Path) : ProtocolResourceLoader {
    override fun support(protocol: String): Boolean {
        return "file".equals(protocol, true)
    }

    override fun getResource(location: URI): Resource {
        Assertx.mustTrue(this.support(location.scheme), "不是有效的 file 协议地址: $location")
        return FileSystemResource()
    }

    override fun getResource(location: String): Resource {
        return FileSystemResource()
    }

    private class FileSystemResource() : Resource {
        override fun isExists(): Boolean {
            return false
        }

        override fun getURI(): URI {
            TODO("Not yet implemented")
        }

        override fun getContentLength(): Long {
            TODO("Not yet implemented")
        }

        override fun getFilename(): String? {
            TODO("Not yet implemented")
        }

        override fun getInputStream(): InputStream? {
            TODO("Not yet implemented")
        }
    }
}