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

package central.pattern.chain

/**
 * 过滤链
 *
 * @author Alan Yeh
 * @since 2022/12/12
 */
class FilterChain<T>(
    /**
     * 处理器列表
     */
    private val filters: List<Filter<T>>,
    /**
     * 当前待处行的处理器下标
     */
    private val index: Int = 0
) {

    /**
     * 执行下一过滤器
     *
     * @param target 待处理对象
     *
     */
    fun filter(target: T) {
        if (this.index < this.filters.size) {
            val filter = this.filters[this.index]
            val next = FilterChain(this.filters, this.index + 1)
            if (filter.predicate(target)) {
                // 断言成功，执行过滤器
                filter.filter(target, next)
            } else {
                // 断言不成功，则直接执行下一过滤器
                next.filter(target)
            }
        }
    }
}