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

package central.io.support

import central.io.ProtocolResourceLoader
import central.io.Resource
import central.lang.Assertx
import java.io.InputStream
import java.net.URI
import kotlin.io.path.toPath

/**
 * Classpath 资源加载器
 *
 * @author Alan Yeh
 * @since 2022/12/22
 */
class ClassPathResourceLoader(private val classLoader: ClassLoader) : ProtocolResourceLoader {

    override fun support(protocol: String): Boolean {
        return "classpath".equals(protocol, true)
    }

    override fun getResource(location: URI): Resource {
        Assertx.mustTrue(this.support(location.scheme), "不是有效的 classpath 协议地址: $location")
        return ClassPathResource(this.classLoader, location.rawPath)
    }

    override fun getResource(location: String): Resource {
        return ClassPathResource(classLoader, location)
    }

    override fun getResources(location: String): List<Resource> {
        return listOf(getResource(location))
    }

    private class ClassPathResource(private val classLoader: ClassLoader, private val path: String) : Resource {
        override fun isExists(): Boolean {
            return classLoader.getResource(path) != null
        }

        override fun getURI(): URI {
            return URI("classpath:$path")
        }

        override fun getContentLength(): Long {
            return -1
        }

        override fun getFilename(): String {
            return getURI().toPath().fileName.toString()
        }

        override fun getInputStream(): InputStream? {
            return classLoader.getResourceAsStream(path)
        }
    }
}