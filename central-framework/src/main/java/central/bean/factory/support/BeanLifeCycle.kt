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

package central.bean.factory.support

import central.bean.factory.InitializingBean
import central.bean.factory.config.BeanPostProcessor

/**
 * Bean 生命周期管理
 *
 * @author Alan Yeh
 * @since 2023/01/31
 */
class BeanLifeCycle {
    ////////////////////////////////////////////////////////////////
    // BeanPostProcessor Register

    private val processors = mutableListOf<BeanPostProcessor>()

    /**
     * 添加 Bean 处理器
     */
    fun addProcessor(processor: BeanPostProcessor) {
        this.processors.add(processor)
    }

    /**
     * 添加 Bean 处理器
     */
    fun addProcessors(processors: List<BeanPostProcessor>) {
        this.processors.addAll(processors)
    }

    /**
     * 添加 Bean 处理器
     */
    fun addProcessor(processor: BeanPostProcessor, index: Int) {
        this.processors.add(index, processor)
    }

    /**
     * 移除 Bean 处理器
     */
    fun removeProcessor(processor: BeanPostProcessor) {
        this.processors.remove(processor)
    }


    ////////////////////////////////////////////////////////////////
    // Bean LifeCycle

    /**
     * 初始化 Bean
     */
    fun initialize(name: String, bean: Any) {
        for (processor in this.processors) {
            processor.processBeforeInitialization(name, bean)
        }
        (bean as? InitializingBean)?.initialize()
        for (processor in this.processors) {
            processor.processAfterInitialization(name, bean)
        }
    }

    /**
     * 销毁 Bean
     */
    fun destroy(name: String, bean: Any) {

    }
}