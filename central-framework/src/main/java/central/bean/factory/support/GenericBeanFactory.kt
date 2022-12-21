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

import central.bean.convert.ConversionService
import central.bean.convert.support.GenericConversionService
import central.bean.factory.*
import central.bean.factory.config.*
import central.lang.Assertx
import java.util.concurrent.ConcurrentHashMap

/**
 * 标准 Bean 工厂
 *
 * @author Alan Yeh
 * @since 2022/12/19
 */
class GenericBeanFactory : ConfigurableBeanFactory {

    override var beanClassLoader: ClassLoader = Thread.currentThread().contextClassLoader ?: GenericBeanFactory::class.java.classLoader!!

    override var conversionService: ConversionService = GenericConversionService()

    override var registry: BeanDefinitionRegistry = GenericBeanDefinitionRegistry()

    /**
     * 单例对象
     * bean name -> bean instance
     */
    private val singletons = ConcurrentHashMap<String, Any>()

    /**
     * 单例工厂
     * bean name -> bean factory
     */
    private val singletonFactories = ConcurrentHashMap<String, BeanFactory>()

    /**
     * 早期单例
     * TODO 什么场景下会用这个
     * bean name -> bean instance
     */
    private val earlySingletons = ConcurrentHashMap<String, Any>()

    /**
     * 用户注册的单例对象名称
     */
    private val registeredSingletons = mutableSetOf<String>()

    /**
     * 当前正在创建的单例名称
     */
    private val singletonsCurrentlyInCreation = mutableSetOf<String>()

    /**
     * 创建 Bean 实例
     */
    private fun createBean(definition: BeanDefinition): Any {
        val factory = this.postProcess<FactoryBean<Any>>(definition, definition.factory)

        return this.postProcess(definition, factory.getBean())
    }

    /**
     * 根据 Bean 定义获取 Bean
     */
    private fun getBean(definition: BeanDefinition): Any {
        return if (definition.singleton) {
            // 如果是单例的话，则从单例池里面获取实例
            synchronized(this.singletons) {
                val singleton = this.singletons[definition.name]
                if (singleton != null){
                    return singleton
                }

                if (this.singletonsCurrentlyInCreation.contains(definition.name)) {
                    // 如果这个单例正在创建中，又来获取这个 Bean，则有可能出现创建多个单例的问题
                    throw BeanCreationException("Bean '${definition.name}' is in creation.")
                }
                try {
                    this.singletonsCurrentlyInCreation.add(definition.name)
                    this.createBean(definition)
                } finally {
                    this.singletonsCurrentlyInCreation.remove(definition.name)
                }
            }
        } else {
            // 非单例则每次都创建新的 Bean
            this.createBean(definition)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> postProcess(definition: BeanDefinition, bean: Any): T {
        var processedBean = bean
        for (processor in this.beanPostProcessors) {
            processedBean = processor.processBeforeInitialization(definition.name, processedBean)
        }
        (processedBean as? InitializingBean)?.initialize()
        for (processor in this.beanPostProcessors) {
            processedBean = processor.processAfterInitialization(definition.name, processedBean)
        }
        return processedBean as T
    }

    //---------------------------------------------------------------------
    // Implementation of ListableBeanFactory
    //---------------------------------------------------------------------
    override fun destroyBean(name: String) {
        TODO("Not yet implemented")
    }

    override fun registerSingleton(name: String, instance: Any) {
        this.registry.registerDefinition(RootBeanDefinition(instance::class.java, factory = InstanceFactoryBean(instance)))
    }

    override fun destroySingletons() {
        // Destroy
        this.singletons.clear()
    }

    override fun clearBeans() {
        this.singletons.clear()
    }

    //---------------------------------------------------------------------
    // Implementation of ListableBeanFactory
    //---------------------------------------------------------------------

    private val beanPostProcessors = mutableListOf<BeanPostProcessor>()
    override fun addBeanPostProcessor(processor: BeanPostProcessor) {
        this.beanPostProcessors.add(processor)
    }

    override fun removeBeanPostProcessor(processor: BeanPostProcessor) {
        this.beanPostProcessors.add(processor)
    }

    override fun clearBeanPostProcessors() {
        this.beanPostProcessors.clear()
    }

    override fun preInstantiateSingletons() {
        this.registry.getDefinitions { definition -> definition.singleton }
            .forEach(this::getBean)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getBeansOfType(requiredType: Class<T>): Map<String, T> {
        return this.registry.getDefinitions { definition ->
            definition.isTypeMatched(requiredType)
        }.associate { it.name to this.getBean(it) as T }
    }

    override fun getBeanNamesForType(type: Class<*>): List<String> {
        return this.getBeanNamesForType(type, true, true)
    }

    override fun getBeanNamesForType(type: Class<*>, includeNonSingletons: Boolean, allowEagerInit: Boolean): List<String> {
        val candidates = this.registry.getDefinitions { definition ->
            definition.isTypeMatched(type)
        }.filter { includeNonSingletons || it.singleton }

        if (allowEagerInit) {
            // 需要初始化
            candidates.forEach(this::createBean)
        }

        return candidates.map { it.name }
    }

    override fun getBeansWithAnnotations(annotationTypes: List<Class<out Annotation>>): Map<String, Any> {
        val candidates = this.registry.getDefinitions { definition ->
            // 所有的注解都需要出现
            annotationTypes.all { annotation -> definition.type.isAnnotationPresent(annotation) }
        }

        return candidates.associate { it.name to this.getBean(it) }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getBean(name: String): T? {
        val definition = this.registry.getDefinition(name) ?: return null
        return getBean(definition) as T
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T> getBean(name: String, requiredType: Class<T>): T? {
        val definition = this.registry.getDefinition(name) ?: return null
        Assertx.mustAssignableFrom(requiredType, definition.type, ::ClassCastException, "Cannot cast bean type of '${definition.type.name}' to '${requiredType.name}'")
        return getBean(definition) as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getBean(requiredType: Class<T>): T? {
        val beanNames = this.getBeanNamesForType(requiredType)
        if (beanNames.isEmpty()) {
            return null
        } else if (beanNames.size == 1) {
            return this.getBean(beanNames.first(), requiredType)
        } else {
            // 出现多个候选，根据 primary 决定返回的 Bean
            var primaryBean: BeanDefinition? = null
            for (name in beanNames) {
                val definition = this.registry.getDefinition(name) ?: continue
                if (definition.primary) {
                    if (primaryBean != null) {
                        // 出现多个 primary
                        throw NoUniqueBeanDefinitionException("No qualifying bean of type '${requiredType.name}' available: expected single matching bean but found ${beanNames.size}: ${beanNames.joinToString()}")
                    }
                    primaryBean = definition
                }
            }
            if (primaryBean == null) {
                // 没有 primary
                throw NoUniqueBeanDefinitionException("No qualifying bean of type '${requiredType.name}' available: expected single matching bean but found ${beanNames.size}: ${beanNames.joinToString()}")
            }
            return this.getBean(primaryBean) as T
        }
    }

    override fun containsBean(name: String): Boolean {
        return this.registry.containsDefinition(name)
    }

    override fun getType(name: String): Class<*>? {
        return this.registry.getDefinition(name)?.type
    }

    override fun isTypeMatch(name: String, type: Class<*>): Boolean {
        return this.registry.getDefinition(name)?.isTypeMatched(type) == true
    }
}