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

import android.content.Context
import android.content.res.AssetManager
import central.io.ProtocolResourceLoader
import central.io.Resource
import central.lang.Assertx
import java.io.IOException
import java.io.InputStream
import java.net.URI

/**
 * 用于加载 assets 下的资源
 *
 * 使用本资源加载器时，location 的协议为 asset:
 *
 * @author Alan Yeh
 * @since 2022/12/22
 */
class AssetResourceLoader(private val context: Context) : ProtocolResourceLoader {

    override fun support(protocol: String): Boolean {
        return "asset".equals(protocol, true)
    }

    override fun getResource(location: URI): Resource {
        Assertx.mustTrue(this.support(location.scheme), "不是有效的 asset 协议地址: $location")
        return AssetResource(context.assets, location.rawPath)
    }

    override fun getResource(location: String): Resource {
        return AssetResource(context.assets, location)
    }

    private class AssetResource(private val manager: AssetManager, private val path: String) : Resource {
        override fun isExists(): Boolean {
            val path: String // 存放路径
            val name: String // 文件名称

            val index = this.path.lastIndexOf("/")
            if (index != -1) {
                path = this.path.substring(0, index)
                name = this.path.substring(index + 1)
            } else {
                path = ""
                name = this.path
            }

            return try {
                this.manager.list(path)?.contains(name) == true
            } catch (ignored: IOException) {
                false
            }
        }

        override fun getURI(): URI {
            return URI("asset:" + this.path)
        }

        override fun getContentLength(): Long {
            return -1
        }

        override fun getFilename(): String {
            val index = this.path.lastIndexOf("/")
            return if (index != -1) {
                this.path.substring(index + 1)
            } else {
                this.path
            }
        }

        override fun getInputStream(): InputStream {
            return this.manager.open(this.path)
        }
    }
}