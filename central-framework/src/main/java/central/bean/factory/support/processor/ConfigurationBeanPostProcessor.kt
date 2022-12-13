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

import central.bean.factory.ConfigurableListableBeanFactory
import central.bean.factory.Prioritized
import central.bean.factory.config.*
import central.bean.factory.support.BeanReference
import central.bean.factory.support.GenericBeanDefinition
import central.bean.factory.support.RootBeanDefinition
import central.io.ResourceLoader
import central.io.support.ClassPathResourceLoader

/**
 * 用于处理配置类
 *
 * @author Alan Yeh
 * @see Configuration
 * @since 2023/02/10
 */
class ConfigurationBeanPostProcessor : BeanFactoryPostProcessor, Prioritized {

    override val priority: Int = Prioritized.HIGHEST_PRIORITY

    /**
     * 资源加载器
     *
     * 默认使用 ClassPathResourceLoader
     */
    private val resourceLoaders = mutableListOf<ResourceLoader>(ClassPathResourceLoader(GenericBeanDefinition::class.java.classLoader!!))

    /**
     * 配置类解析器
     */
    private val resolvers = listOf(ComponentBeanResolver(), ConfigurationBeanResolver(), ImportBeanResolver(), ResourceImportBeanResolver(this))

    /**
     * 用于记录已解析的配置类
     */
    private val resolvedConfigurations = mutableSetOf<String>()

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        // 注册配置类产生的 Bean 定义
        if (beanFactory is BeanDefinitionRegistry) {
            this.postProcessBeanRegistry(beanFactory)
        }
    }

    private fun postProcessBeanRegistry(registry: BeanDefinitionRegistry) {
        val candidates = mutableListOf<BeanDefinition>()

        // 找到所有配置类
        for (name in registry.getBeanDefinitionNames()) {
            val definition = registry.getDefinition(name) ?: continue

            if (definition.type.isAnnotationPresent(Configuration::class.java)) {
                candidates.add(definition)
            }
        }

        if (candidates.isEmpty()) {
            return
        }

        for (candidate in candidates) {
            this.resolve(registry, candidate)
        }
    }

    private fun resolve(registry: BeanDefinitionRegistry, configuration: BeanDefinition) {
        if (!configuration.type.isAnnotationPresent(Configuration::class.java)){
            // 如果不是配置类的话，就不解析
            return
        }
        if (resolvedConfigurations.contains(configuration.name)) {
            // 避免重复解析
            return
        }
        resolvedConfigurations.add(configuration.name)

        for (resolver in this.resolvers) {
            if (resolver.support(configuration)) {
                val definitions = resolver.resolve(configuration)
                definitions.forEach {
                    registry.registerDefinition(it)
                    // 继续解析
                    this.resolve(registry, it)
                }
            }
        }
    }

    interface BeanResolver {
        /**
         * 是否支持解析该类型
         */
        fun support(definition: BeanDefinition): Boolean

        /**
         * 解析该类型
         */
        fun resolve(definition: BeanDefinition): List<BeanDefinition>
    }

    /**
     * 解析 @Import 注解
     */
    class ImportBeanResolver : BeanResolver {
        override fun support(definition: BeanDefinition): Boolean {
            return definition.type.isAnnotationPresent(Configuration::class.java) && definition.type.isAnnotationPresent(Import::class.java)
        }

        override fun resolve(definition: BeanDefinition): List<BeanDefinition> {
            val definitions = mutableListOf<BeanDefinition>()

            val import = definition.type.getAnnotation(Import::class.java) ?: return definitions
            for (klass in import.value) {
                val component = RootBeanDefinition(klass.java, ConstructorInvokingFactoryBean(klass.java))
                definitions.add(component)
            }

            return definitions
        }
    }

    /**
     * 解析配置类对应的 .imports 引入文件
     */
    class ResourceImportBeanResolver(private val processor: ConfigurationBeanPostProcessor) : BeanResolver {
        override fun support(definition: BeanDefinition): Boolean {
            return definition.type.isAnnotationPresent(Configuration::class.java)
        }

        override fun resolve(definition: BeanDefinition): List<BeanDefinition> {
            val definitions = mutableListOf<BeanDefinition>()

            // 从配置文件中引用 Bean
            for (resourceLoader in processor.resourceLoaders) {
                val imports = resourceLoader.getResources("central/${definition.type.name}.imports")
                for (resource in imports) {
                    if (resource.isExists()) {
                        resource.getInputStream()?.bufferedReader()?.readLines()?.forEach {
                            val klass = Class.forName(it)
                            val component = RootBeanDefinition(klass, ConstructorInvokingFactoryBean(klass))
                            definitions.add(component)
                        }
                    }
                }
            }

            return definitions
        }
    }

    /**
     * 解析配置类里面的标注了 @Bean 注解的方法
     */
    class ConfigurationBeanResolver : BeanResolver {
        override fun support(definition: BeanDefinition): Boolean {
            return definition.type.isAnnotationPresent(Configuration::class.java)
        }

        override fun resolve(definition: BeanDefinition): List<BeanDefinition> {
            val definitions = mutableListOf<BeanDefinition>()

            for (method in definition.type.methods) {
                val bean = method.getAnnotation(Bean::class.java) ?: continue
                val factory = MethodInvokingFactoryBean(BeanReference(definition), method)

                definitions.add(
                    GenericBeanDefinition(
                        name = bean.name.ifEmpty { method.name },
                        type = factory.getBeanType(),
                        singleton = factory.singleton,
                        lazyInit = factory.lazy,
                        dependsOn = emptyList(),
                        primary = method.isAnnotationPresent(Primary::class.java),
                        factory = factory
                    )
                )
            }

            return definitions
        }
    }

    /**
     * 解析 @Component 组件类
     */
    class ComponentBeanResolver : BeanResolver {
        override fun support(definition: BeanDefinition): Boolean {
            return definition.type.isAnnotationPresent(Component::class.java)
        }

        override fun resolve(definition: BeanDefinition): List<BeanDefinition> {
            val component = definition.type.getAnnotation(Component::class.java) ?: return emptyList()

            val factory = ConstructorInvokingFactoryBean(definition.type)

            return listOf(
                GenericBeanDefinition(
                    name = component.value.ifEmpty { definition.type.simpleName.replaceFirstChar { it.uppercaseChar() } },
                    type = factory.getBeanType(),
                    singleton = factory.singleton,
                    lazyInit = factory.lazy,
                    dependsOn = emptyList(),
                    primary = definition.type.isAnnotationPresent(Primary::class.java),
                    factory = factory
                )
            )
        }
    }
}