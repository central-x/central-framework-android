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

package central.bean.context

import central.bean.factory.BeanException
import central.bean.factory.ConfigurableBeanFactory
import central.bean.factory.config.BeanFactoryPostProcessor
import central.env.ConfigurableEnvironment

/**
 * 可配置的应用上下文
 *
 * @author Alan Yeh
 * @since 2023/01/31
 */
interface ConfigurableApplicationContext : ApplicationContext {

    /**
     * 设置环境
     */
    override var environment: ConfigurableEnvironment

    /**
     * 获取 Bean 工厂
     */
    val beanFactory: ConfigurableBeanFactory

    /**
     * 刷新应用上下文
     */
    @Throws(BeanException::class, IllegalArgumentException::class)
    fun refresh()

    /**
     * 添加 Bean 工厂后置处理器
     */
    fun addBeanFactoryPostProcessor(processor: BeanFactoryPostProcessor)
}