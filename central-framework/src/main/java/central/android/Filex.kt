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
import java.io.File

/**
 * 文件工具
 *
 * @author Alan Yeh
 * @since 2022/12/10
 */
class Filex {
    companion object {
        /**
         * 获取系统外部存储根目录
         */
        @JvmStatic
        fun getHomePath(context: Context): File {
            return context.externalCacheDir!!.parentFile!!
        }

        /**
         * 获取日志目录
         */
        @JvmStatic
        fun getLogPath(context: Context): File {
            val logDir = File(getHomePath(context), "log")
            if (!logDir.exists() || !logDir.isDirectory){
                logDir.mkdirs()
            }
            return logDir
        }

        /**
         * 获取缓存目录
         */
        @JvmStatic
        fun getCachePath(context: Context): File {
            return context.externalCacheDir!!
        }

        /**
         * 获取临时目录
         */
        @JvmStatic
        fun getTempPath(context: Context): File {
            val tmpPath = File(getHomePath(context), "tmp")
            tmpPath.mkdirs()
            return tmpPath
        }

        /**
         * 获取数据库目录
         */
        @JvmStatic
        fun getDbPath(context: Context): File {
            val dbDir = File(getHomePath(context), "db")
            dbDir.mkdirs()
            return dbDir
        }

        /**
         * 获取静态资源目录
         */
        @JvmStatic
        fun getWebDir(context: Context): File{
            val webDir = File(getHomePath(context), "web")
            webDir.mkdirs()
            return webDir
        }
    }
}