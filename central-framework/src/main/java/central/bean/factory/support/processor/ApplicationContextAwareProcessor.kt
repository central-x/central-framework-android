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

package central.bean.factory.support.processor

import central.bean.context.ApplicationContext
import central.bean.context.ApplicationContextAware
import central.bean.factory.FactoryBean
import central.bean.factory.config.BeanPostProcessor
import central.bean.factory.config.FactoryBeanPostProcessor

/**
 * 处理 ApplicationContext 注入
 *
 * @author Alan Yeh
 * @see ApplicationContext
 * @see ApplicationContextAware
 * @since 2023/01/30
 */
class ApplicationContextAwareProcessor(private val applicationContext: ApplicationContext) : BeanPostProcessor, FactoryBeanPostProcessor {

    override fun processBeforeInitialization(name: String, bean: Any): Any {
        return bean.also {
            (it as? ApplicationContextAware)?.applicationContext = this.applicationContext
        }
    }

    override fun postProcessFactoryBean(factory: FactoryBean<*>) {
        (factory as? ApplicationContextAware)?.applicationContext = this.applicationContext
    }
}