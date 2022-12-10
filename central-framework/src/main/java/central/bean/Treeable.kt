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

/**
 * Treeable Entity
 *
 * 可构建成树状的数据
 *
 * @author Alan Yeh
 * @since 2022/12/09
 */
interface Treeable<T : Treeable<T>> : Identifiable {
    /**
     * 节点唯一标识
     */
    override var id: String?

    /**
     * 父节点唯一标识
     *
     * 如果父节点为空，则认为是根节点
     */
    var parentId: String?

    /**
     * 子节点
     */
    var children: List<T>?

    companion object {
        /**
         * 构建成树
         */
        @JvmStatic
        fun <T : Treeable<T>> build(data: Collection<T>): List<T> {
            TODO("构建成树")
        }
    }
}