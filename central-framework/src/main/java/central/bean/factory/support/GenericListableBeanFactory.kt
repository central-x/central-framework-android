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

import central.bean.Validatable
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
class GenericListableBeanFactory(private vararg val sources: Class<*>) : ConfigurableListableBeanFactory, BeanDefinitionRegistry {

    override var beanClassLoader: ClassLoader = Thread.currentThread().contextClassLoader ?: GenericListableBeanFactory::class.java.classLoader!!

    override var conversionService: ConversionService = GenericConversionService()

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
            this.singletons.computeIfAbsent(definition.name) {
                if (this.singletonsCurrentlyInCreation.contains(it)) {
                    // 如果这个单例正在创建中，又来获取这个 Bean，则有可能出现创建多个单例的问题
                    throw BeanCreationException("Bean '${definition.name}' is in creation.")
                }
                try {
                    this.singletonsCurrentlyInCreation.add(it)
                    this.createBean(definition)
                } finally {
                    this.singletonsCurrentlyInCreation.remove(it)
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
        this.definitions[name] = RootBeanDefinition(instance::class.java, InstanceFactoryBean(instance))
    }

    override fun destroySingletons() {
        // Destroy
        this.singletons.clear()
    }

    override fun clearBeans() {
        TODO("Not yet implemented")
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

    override fun registerResolvableDependency(dependencyType: Class<*>, autowiredValue: Any?) {
        // TODO
    }

    override fun preInstantiateSingletons() {
        for (definition in this.definitions.values) {
            if (definition.singleton) {
                if (!this.singletons.containsKey(definition.name)) {
                    // 创建 Bean
                    val bean = this.createBean(definition)
                    this.singletons[definition.name] = bean
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getBeansOfType(requiredType: Class<T>): Map<String, T> {
        val candidates = mutableMapOf<String, T>()

        this.definitions.forEach { (name, definition) ->
            if (definition.isTypeMatched(requiredType)) {
                candidates[name] = getBean(definition) as T
            }
        }

        return candidates
    }

    override fun getBeanNamesForType(type: Class<*>): List<String> {
        return this.getBeanNamesForType(type, true, true)
    }

    override fun getBeanNamesForType(type: Class<*>, includeNonSingletons: Boolean, allowEagerInit: Boolean): List<String> {
        val candidates = mutableListOf<String>()

        this.definitions.forEach { (name, definition) ->
            if (definition.isTypeMatched(type)) {
                if (includeNonSingletons) {
                    // 如果要包含非单例，则所有类型匹配的 Bean 定义都加入候选
                    candidates.add(name)
                } else if (definition.singleton) {
                    // 否则只将单例 Bean 加入候选
                    candidates.add(name)
                }
            }
        }

        return candidates
    }

    override fun getBeansWithAnnotations(annotationTypes: List<Class<out Annotation>>): Map<String, Any> {
        val candidates = mutableMapOf<String, Any>()

        this.definitions.forEach { (name, definition) ->
            for (annotation in annotationTypes) {
                if (definition.type.isAnnotationPresent(annotation)) {
                    // 只要有一个注解没有出现，则不列入候选
                    break
                }
                // 所有注解都出现，加入候选列表
                candidates[name] = getBean(definition)
            }
        }

        return candidates
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getBean(name: String): T? {
        val definition = this.definitions[name] ?: return null
        return getBean(definition) as T
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T> getBean(name: String, requiredType: Class<T>): T? {
        val definition = this.definitions[name] ?: return null
        Assertx.mustAssignableFrom(requiredType, definition.type, ::ClassCastException, "Cannot cast bean type of '${definition.type.name}' to '${requiredType.name}'")
        return getBean(definition) as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getBean(requiredType: Class<T>): T? {
        val beans = this.getBeanNamesForType(requiredType)
        if (beans.isEmpty()) {
            return null
        } else if (beans.size == 1) {
            return this.getBean(beans.first(), requiredType)
        } else {
            // 出现多个候选，根据 primary 决定返回的 Bean
            var primaryBean: BeanDefinition? = null
            for (name in beans) {
                val definition = this.getDefinition(name) ?: continue
                if (definition.primary) {
                    if (primaryBean != null) {
                        // 出现多个 primary
                        throw NoUniqueBeanDefinitionException("No qualifying bean of type '${requiredType.name}' available: expected single matching bean but found ${beans.size}: ${beans.joinToString()}")
                    }
                    primaryBean = definition
                }
            }
            if (primaryBean == null) {
                // 没有 primary
                throw NoUniqueBeanDefinitionException("No qualifying bean of type '${requiredType.name}' available: expected single matching bean but found ${beans.size}: ${beans.joinToString()}")
            }
            return this.getBean(primaryBean) as T
        }
    }

    override fun containsBean(name: String): Boolean {
        return this.definitions.containsKey(name)
    }

    override fun getType(name: String): Class<*>? {
        return this.definitions[name]?.type
    }

    override fun isTypeMatch(name: String, type: Class<*>): Boolean {
        return this.definitions[name]?.isTypeMatched(type) == true
    }

    //---------------------------------------------------------------------
    // Implementation of BeanDefinitionRegistry
    //---------------------------------------------------------------------

    /**
     * Bean 定义
     */
    private val definitions = mutableMapOf<String, BeanDefinition>()
    override fun getBeanDefinitionNames(): List<String> {
        return definitions.keys.toList()
    }

    override fun registerDefinition(definition: BeanDefinition) {
        // 校验
        (definition as? Validatable)?.validate()

        if (this.definitions.containsKey(definition.name)) {
            throw BeanConflictException("Cannot register bean definition for bean '${definition.name}'")
        }

        this.definitions[definition.name] = definition
    }

    override fun removeDefinition(name: String) {
        this.definitions.remove(name)
    }

    override fun getDefinition(name: String): BeanDefinition? {
        return this.definitions[name]
    }

    override fun containsDefinition(name: String): Boolean {
        return this.definitions.containsKey(name)
    }
}