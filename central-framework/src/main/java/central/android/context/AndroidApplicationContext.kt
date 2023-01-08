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

package central.android.context

import central.android.env.AndroidEnvironment
import central.bean.context.ApplicationEvent
import central.bean.context.ApplicationListener
import central.bean.context.ApplicationPublisher
import central.bean.context.ConfigurableApplicationContext
import central.bean.context.event.ContextRefreshedEvent
import central.bean.context.support.GenericApplicationPublisher
import central.bean.factory.BeanException
import central.bean.factory.ConfigurableBeanFactory
import central.bean.factory.Prioritized
import central.bean.factory.PriorityComparator
import central.bean.factory.config.BeanFactoryPostProcessor
import central.bean.factory.config.BeanPostProcessor
import central.bean.factory.support.GenericBeanFactory
import central.bean.factory.support.processor.bean.*
import central.convert.ConfigurableConverter
import central.convert.Converter
import central.convert.support.GenericConverter
import central.env.ConfigurableEnvironment
import central.io.ResourceLoader
import central.io.support.ClassPathResourceLoader

/**
 * Android 应用上下文
 *
 * @author Alan Yeh
 * @since 2023/01/31
 */
open class AndroidApplicationContext : ConfigurableApplicationContext {
    override val beanFactory: ConfigurableBeanFactory = GenericBeanFactory()
    override var environment: ConfigurableEnvironment = AndroidEnvironment()

    private val beanFactoryPostProcessors: MutableList<BeanFactoryPostProcessor> = mutableListOf()

    var resourceLoader: ResourceLoader = ClassPathResourceLoader(Thread.currentThread().contextClassLoader ?: AndroidApplicationContext::class.java.classLoader)

    override fun refresh() {
        prepareBeanFactory()

        try {
            // 执行 BeanFactoryPostProcessor
            this.postProcessBeanFactory()
            // 注册 BeanPostProcessor
            this.registerBeanPostProcessors()
            // 初始化事件分发器
            this.initApplicationPublisher()
            // 初始化其它 Bean
            this.onRefresh()
            // 注册事件监听器
            this.registerListeners()
            // 完成 BeanFactory 的初始化，初始化那些剩余的非延迟初始化的 Bean
            this.finishBeanFactoryInitialization()
            // 完成 Refresh
            this.finishRefresh()
        } catch (ex: BeanException) {
            // 销毁所有已创建的单例，防止资源游离（内存泄露）
            this.beanFactory.destroySingletons()

            throw ex
        }
    }

    private fun prepareBeanFactory() {
        // TODO 设置 BeanFactory 的 ClassLoader
        // TODO 了解 ClassLoader 的作用是什么

        // 添加预设的 Bean 后置处理器
        this.beanFactory.addBeanPostProcessor(BeanNameAwareProcessor())
        this.beanFactory.addBeanPostProcessor(ApplicationContextAwareProcessor(this))
        this.beanFactory.addBeanPostProcessor(EnvironmentAwareProcessor(this.environment))
        this.beanFactory.addBeanPostProcessor(ResourceLoaderAwareProcessor(this.resourceLoader))
        this.beanFactory.addBeanPostProcessor(AutowiredProcessor(this))
        // 用于特殊处理 ApplicationListener
        this.beanFactory.addBeanPostProcessor(ApplicationListenerDetector(this))
    }

    /**
     * 支持 BeanFactoryPostProcessor
     */
    private fun postProcessBeanFactory() {
        // 1. 优先执行预注册的 BeanFactoryPostProcessor
        for (processor in this.beanFactoryPostProcessors) {
            processor.postProcessBeanFactory(this.beanFactory)
        }

        // 2. 接着执行用户动态定义的 BeanFactoryPostProcessor

        // 区分实现了 Prioritized 接口的 BeanFactoryPostProcessor
        val prioritizedPostProcessors = mutableListOf<BeanFactoryPostProcessor>()
        val nonPrioritizedPostProcessors = mutableListOf<BeanFactoryPostProcessor>()

        // 获取所有 BeanFactoryPostProcessor，使
        val processorNames = beanFactory.getBeanNamesForType(BeanFactoryPostProcessor::class.java, true, false)

        for (name in processorNames) {
            if (beanFactory.isTypeMatch(name, Prioritized::class.java)) {
                prioritizedPostProcessors.add(beanFactory.requireBean(name, BeanFactoryPostProcessor::class.java))
            } else {
                nonPrioritizedPostProcessors.add(beanFactory.requireBean(name, BeanFactoryPostProcessor::class.java))
            }
        }

        // 执行实现了 Prioritized 接口的 BeanFactoryPostProcessor
        prioritizedPostProcessors.sortedWith(PriorityComparator())
        for (processor in prioritizedPostProcessors) {
            processor.postProcessBeanFactory(this.beanFactory)
        }

        // 接着执行没有实现 Prioritized 接口的 BeanFactoryPostProcessor
        for (processor in nonPrioritizedPostProcessors) {
            processor.postProcessBeanFactory(this.beanFactory)
        }
    }

    /**
     * 注册 BeanPostProcessor
     */
    private fun registerBeanPostProcessors() {
        val processorNames = this.beanFactory.getBeanNamesForType(BeanPostProcessor::class.java, true, false)

        // 区分实现了 Prioritized 接口的 BeanPostProcessor
        val prioritizedPostProcessors = mutableListOf<BeanPostProcessor>()
        val nonPrioritizedPostProcessor = mutableListOf<BeanPostProcessor>()

        for (name in processorNames) {
            if (beanFactory.isTypeMatch(name, Prioritized::class.java)) {
                prioritizedPostProcessors.add(beanFactory.requireBean(name))
            } else {
                nonPrioritizedPostProcessor.add(beanFactory.requireBean(name))
            }
        }

        // 注册实现了 Prioritized 接口的 BeanPostProcessor
        prioritizedPostProcessors.sortedWith(PriorityComparator())
        prioritizedPostProcessors.forEach(this.beanFactory::addBeanPostProcessor)

        // 注册其它的 BeanPostProcessor
        nonPrioritizedPostProcessor.forEach(this.beanFactory::addBeanPostProcessor)

        // 注册 ApplicationListenerDetector
        this.beanFactory.addBeanPostProcessor(ApplicationListenerDetector(this))
    }

    /**
     * 初始化事件分发器
     */
    private fun initApplicationPublisher() {
        val applicationPublisher = this.beanFactory.getBean("applicationPublisher", ApplicationPublisher::class.java)
        if (applicationPublisher == null) {
            // 标准事件广播器
            this.applicationPublisher = GenericApplicationPublisher(this.beanFactory)
            this.beanFactory.registerSingleton("applicationPublisher", this.applicationPublisher)
        } else {
            // 支持用户自定义事件广播器
            this.applicationPublisher = applicationPublisher
        }
    }

    /**
     * 注册事件监听器
     */
    private fun registerListeners() {
        val listenerBeanNames = this.beanFactory.getBeanNamesForType(ApplicationListener::class.java, true, false)
        for (name in listenerBeanNames) {
            this.applicationPublisher.addApplicationListenerBean(name)
        }
    }

    /**
     * 用于通知子类方法
     */
    protected open fun onRefresh() {

    }

    /**
     * 完成 BeanFactory 的初始化
     */
    private fun finishBeanFactoryInitialization() {
        var converter = this.beanFactory.getBean(Converter::class.java)
        if (converter == null) {
            converter = GenericConverter()
            this.beanFactory.converter = converter
            this.beanFactory.registerSingleton("conversionService", converter)
        } else {
            // 支持用户自定义的类型转换服务
            this.beanFactory.converter = converter
        }
        if (converter is ConfigurableConverter) {
            this.beanFactory.addBeanPostProcessor(ConverterDetector(converter))
        }

        // 初始化剩余非延迟初始化的单例
        this.beanFactory.preInstantiateSingletons()
    }

    /**
     * 结束刷新
     */
    private fun finishRefresh() {
        // 清理缓存

        // 初始化 LifecycleProcessor

        // 通知 lifecycle 处理器

        // 广播事件
        this.publishEvent(ContextRefreshedEvent(this))
    }


    override fun addBeanFactoryPostProcessor(processor: BeanFactoryPostProcessor) {
        this.beanFactoryPostProcessors.add(processor)
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // ListableBeanFactory

    override fun <T> getBeansOfType(requiredType: Class<T>): Map<String, T> = this.beanFactory.getBeansOfType(requiredType)
    override fun getBeanNamesForType(type: Class<*>): List<String> = this.beanFactory.getBeanNamesForType(type)
    override fun getBeanNamesForType(type: Class<*>, includeNonSingletons: Boolean, allowEagerInit: Boolean): List<String> = this.beanFactory.getBeanNamesForType(type, includeNonSingletons, allowEagerInit)
    override fun getBeansWithAnnotations(annotationTypes: List<Class<out Annotation>>): Map<String, Any> = this.beanFactory.getBeansWithAnnotations(annotationTypes)
    override fun <T> getBean(name: String): T? = this.beanFactory.getBean(name)
    override fun <T> getBean(name: String, requiredType: Class<T>): T? = this.beanFactory.getBean(name, requiredType)
    override fun <T> getBean(requiredType: Class<T>): T? = this.beanFactory.getBean(requiredType)
    override fun containsBean(name: String): Boolean = this.beanFactory.containsBean(name)
    override fun getType(name: String): Class<*>? = this.beanFactory.getType(name)
    override fun isTypeMatch(name: String, type: Class<*>): Boolean = this.beanFactory.isTypeMatch(name, type)

    /////////////////////////////////////////////////////////////////////////////////////
    // ApplicationPublisher

    private lateinit var applicationPublisher: ApplicationPublisher

    override fun publishEvent(event: ApplicationEvent) = this.applicationPublisher.publishEvent(event)
    override fun addApplicationListener(listener: ApplicationListener<*>) = this.applicationPublisher.addApplicationListener(listener)
    override fun addApplicationListenerBean(listenerBeanName: String) = this.applicationPublisher.addApplicationListenerBean(listenerBeanName)
    override fun removeApplicationListener(listener: ApplicationListener<*>) = this.applicationPublisher.removeApplicationListener(listener)
    override fun removeApplicationListenerBean(listenerBeanName: String) = this.applicationPublisher.removeApplicationListenerBean(listenerBeanName)
    override fun removeAllListeners() = this.applicationPublisher.removeAllListeners()
}