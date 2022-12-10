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

package central.bean

import java.io.Serializable

/**
 * 分页
 * @author Alan Yeh
 * @since 2022/12/09
 */
class Page<T : Serializable>(
    /**
     * 数据
     */
    var data: List<T>,
    /**
     * 分页信息
     */
    var page: Pager
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = -3327014632281051674L

        /**
         * 创建空分页
         */
        @JvmStatic
        fun <T : Serializable> empty(): Page<T> = Page(emptyList(), Pager.empty())

        @JvmStatic
        fun <T : Serializable> of(data: List<T>, page: Pager) = Page(data, page)

        @JvmStatic
        fun <T : Serializable> of(data: List<T>, pageIndex: Long, pageSize: Long, pageCount: Long, itemCount: Long) = Page(data, pageIndex, pageSize, pageCount, itemCount)
    }

    /**
     * 创建分页结果
     *
     * @param data      分页数据
     * @param pageIndex 分页下标
     * @param pageSize  分页大小
     * @param pageCount 分页总数
     * @param itemCount 数据总数
     */
    constructor(data: List<T>, pageIndex: Long, pageSize: Long, pageCount: Long, itemCount: Long) : this(data, Pager(pageIndex, pageSize, pageCount, itemCount))
}

data class Pager(
    /**
     * 分页下标（从 1 开始）
     */
    var pageIndex: Long,
    /**
     * 分页大小
     */
    var pageSize: Long,
    /**
     * 分页总数
     */
    var pageCount: Long,
    /**
     * 数据总数
     */
    var itemCount: Long
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = -3327014632281051674L

        @JvmStatic
        fun empty(): Pager = Pager(0, 0, 0, 0)
    }
}