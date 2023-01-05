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

package central.bean.factory

import central.bean.factory.config.BeanDefinitionRegistry
import central.bean.factory.config.BeanPostProcessor
import central.convert.Converter

/**
 * 可配置的 Bean 工厂
 *
 * @author Alan Yeh
 * @since 2023/02/01
 */
interface ConfigurableBeanFactory: BeanFactory {

    /**
     * 指定加载 Bean 时的 ClassLoader
     */
    var beanClassLoader: ClassLoader

    /**
     * 类型转换服务
     */
    var converter: Converter

    /**
     * Bean 定义注册中心
     */
    var registry: BeanDefinitionRegistry
    
    /**
     * 销毁 Bean
     */
    fun destroyBean(name: String)

    /**
     * 注册单例 Bean
     */
    fun registerSingleton(name: String, instance: Any)

    /**
     * 销毁所有单例
     */
    fun destroySingletons()

    /**
     * 清空 Bean
     */
    fun clearBeans()

    /**
     * 初始化所有非延迟初始化的单例
     *
     * @see central.bean.factory.config.LazyInit
     */
    fun preInstantiateSingletons()

    /**
     * 添加 Bean 后置处理器
     */
    fun addBeanPostProcessor(processor: BeanPostProcessor)

    /**
     * 移除 Bean 后置处理器
     */
    fun removeBeanPostProcessor(processor: BeanPostProcessor)

    /**
     * 清空 Bean 后置处理器
     */
    fun clearBeanPostProcessors()
}