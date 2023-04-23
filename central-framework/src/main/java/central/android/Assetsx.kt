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
import central.lang.Assertx
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.nio.file.Files
import java.nio.file.StandardOpenOption

/**
 * 处理 assets 资源
 *
 * @author Alan Yeh
 * @since 2022/12/10
 */
class Assetsx {
    companion object {
        /**
         * 判断指定 URL 是不是 assets 资源路径
         */
        @JvmStatic
        fun isAssetUri(asset: URI): Boolean {
            return "res".equals(asset.scheme, true)
        }

        @JvmStatic
        private fun getPath(asset: URI): String {
            Assertx.mustTrue(isAssetUri(asset), "Argument 'asset' is not a valid asset uri")
            return asset.rawPath.removePrefix("/")
        }

        /**
         * 判断资源是否存在
         *
         * @param context Context
         * @param asset 资源
         */
        @JvmStatic
        fun exists(context: Context, asset: URI): Boolean {
            val assetPath = this.getPath(asset)

            val path: String // 存放路径
            val name: String // 文件名称

            val index = assetPath.lastIndexOf("/")
            if (index != -1) {
                path = assetPath.substring(0, index)
                name = assetPath.substring(index + 1)
            } else {
                path = ""
                name = assetPath
            }

            return try {
                val manager = context.assets
                manager.list(path)?.contains(name) == true
            } catch (ignored: IOException) {
                false
            }
        }

        /**
         * 将资源传输到本地的目录
         *
         * @param context Context
         * @param asset 资源
         * @param target 本地路径
         */
        @JvmStatic
        @Throws(IOException::class)
        fun transfer(context: Context, asset: URI, target: File): Boolean {
            val manager = context.assets

            val assetPath = this.getPath(asset)

            val index = assetPath.lastIndexOf("/")
            val name = if (index != -1) {
                assetPath.substring(index + 1)
            } else {
                assetPath
            }

            val files = manager.list(assetPath)

            if (files?.isNotEmpty() == true) {
                // 文件目录
                val directory = File(target, name)

                for (file in files) {
                    val result = transfer(context, URI("res://$assetPath/$file"), directory)
                    if (!result) {
                        return false
                    }
                }
                return true
            } else {
                // 创建文件夹
                if (!target.exists() || !target.isDirectory) {
                    Assertx.mustTrue(target.mkdirs(), ::IOException, "无法访问文件: ${target.absolutePath}")
                }
                val file = File(target, name)
                if (file.exists()) {
                    // 覆盖
                    file.delete()
                }
                Assertx.mustTrue(file.createNewFile(), ::IOException, "无法访问文件: ${file.absolutePath}")

                // 复制
                manager.open(assetPath).use { input ->
                    Files.newOutputStream(file.toPath(), StandardOpenOption.WRITE).use { output ->
                        val bufferedInput = input.buffered()
                        val bufferedOutput = output.buffered()

                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var length: Int
                        do {
                            length = bufferedInput.read(buffer, 0, DEFAULT_BUFFER_SIZE)
                            if (length <= 0) {
                                break
                            }
                            bufferedOutput.write(buffer, 0, length)
                        } while (true)
                    }
                }
                return true
            }
        }

        /**
         * 获取资源流
         *
         * @param context Context
         * @param asset 资源
         */
        @JvmStatic
        fun getAsStream(context: Context, asset: URI): InputStream? {
            return try {
                context.assets.open(getPath(asset))
            } catch (ignored: FileNotFoundException) {
                return null
            }
        }


        /**
         * 以文本方式解析资源
         *
         *
         * @param context Context
         * @param asset 资源
         */
        @JvmStatic
        fun getAsText(context: Context, asset: URI): String? {
            val stream = getAsStream(context, asset)
            return stream?.bufferedReader()?.readText()
        }

        /**
         * 以 JSON 方式解析资源
         *
         *
         * @param context Context
         * @param asset 资源
         */
        @JvmStatic
        fun getAsJSONObject(context: Context, asset: URI): JSONObject? {
            val text = getAsText(context, asset)
            return if (text == null){
                null
            } else{
                JSONObject(text)
            }
        }
    }
}