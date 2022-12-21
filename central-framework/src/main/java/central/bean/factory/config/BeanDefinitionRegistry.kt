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

package central.bean.factory.config

/**
 * Bean 定义注册
 *
 * @author Alan Yeh
 * @since 2022/12/19
 */
interface BeanDefinitionRegistry {

    /**
     * 获取所有 Bean 定义的名称
     */
    fun getDefinitionNames(): List<String>

    /**
     * 注册一个新的 Bean
     *
     * @param definition 定义
     */
    fun registerDefinition(definition: BeanDefinition)

    /**
     * 移除 Bean 定义
     *
     * @param name Bean 名称
     */
    fun removeDefinition(name: String)

    /**
     * 获取 Bean 定义
     *
     * @param name Bean 名称
     */
    fun getDefinition(name: String): BeanDefinition?

    /**
     * 获取 Bean 定义
     *
     * @param predicate 用于返回指定条件的 Bean 定定
     */
    fun getDefinitions(predicate: (BeanDefinition) -> Boolean): List<BeanDefinition>

    /**
     * 是否包含指定的 Bean 定义
     *
     * @param name Bean 名称
     */
    fun containsDefinition(name: String): Boolean
}