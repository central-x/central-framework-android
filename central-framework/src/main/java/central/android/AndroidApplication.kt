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

package central.android

import android.app.Application
import central.android.context.AndroidApplicationContext
import central.android.env.AndroidEnvironment
import central.bean.context.ApplicationContext
import central.bean.context.ConfigurableApplicationContext
import central.bean.factory.support.RootBeanDefinition
import central.bean.factory.support.processor.bean.*
import central.bean.factory.support.processor.factory.ConfigurationBeanPostProcessor
import central.env.ConfigurableEnvironment
import central.io.ResourceLoader
import central.io.support.ClassPathResourceLoader
import central.io.support.GenericResourceLoader

/**
 * Android Application
 *
 * @author Alan Yeh
 * @since 2022/12/10
 */
class AndroidApplication(private val application: Application) {

    companion object {
        /**
         * 当前 Application Context
         */
        @JvmStatic
        lateinit var applicationContext: ApplicationContext
            private set

        /**
         * 启动一个新的应用
         */
        @JvmStatic
        fun run(application: Application, vararg sources: Class<*>) {
            this.applicationContext = AndroidApplication(application).run(*sources)
        }

        /**
         * 停止
         */
        @JvmStatic
        fun stop() {
            // 销毁 Bean 工厂
        }
    }

    /**
     * 运行应用
     */
    fun run(vararg sources: Class<*>): ConfigurableApplicationContext {
        val applicationEnvironment = prepareEnvironment()
        val resourceLoader = prepareResourceLoader()
        val applicationContext = prepareContext(application, applicationEnvironment, resourceLoader, sources)
        return applicationContext.also {
            // 刷新上下文，完成上下文初始化
            it.refresh()
        }
    }

    /**
     * 准备上下文
     */
    private fun prepareContext(application: Application, environment: ConfigurableEnvironment, resourceLoader: ResourceLoader, sources: Array<out Class<*>>): ConfigurableApplicationContext {
        val context: ConfigurableApplicationContext = AndroidApplicationContext()
        context.environment = environment

        // 预注册一些 Bean
        context.beanFactory.registerSingleton("applicationContext", context)
        context.beanFactory.registerSingleton("application", application)
        context.beanFactory.registerSingleton("environment", environment)
        context.beanFactory.registerSingleton("resourceLoader", resourceLoader)

        // 添加 Bean 后置处理器
        context.beanFactory.addBeanPostProcessor(ApplicationContextAwareProcessor(context))
        context.beanFactory.addBeanPostProcessor(EnvironmentAwareProcessor(environment))
        context.beanFactory.addBeanPostProcessor(BeanNameAwareProcessor())
        context.beanFactory.addBeanPostProcessor(ResourceLoaderAwareProcessor(resourceLoader))
        context.beanFactory.addBeanPostProcessor(ApplicationListenerDetector(context))

        // 注册用户自定义的组件
        for (source in sources){
            context.beanFactory.registry.registerDefinition(RootBeanDefinition(source))
        }

        // 注册 BeanFactory 后置处理器
        context.addBeanFactoryPostProcessor(ConfigurationBeanPostProcessor())

        return context
    }

    /**
     * 准备环境
     */
    private fun prepareEnvironment(): ConfigurableEnvironment {
        val environment = AndroidEnvironment()
        // 添加系统环境

        // 添加 Android 环境

        // 添加用户环境

        // 添加 Profile 环境

        return environment
    }

    /**
     * 准备资源加载器
     */
    private fun prepareResourceLoader(): ResourceLoader {
        val resourceLoader = GenericResourceLoader()

        // 添加 AssetResourceLoader
        resourceLoader.addResourceLoader(AssetResourceLoader(this.application))
        // 添加 ClassPathResourceLoader
        resourceLoader.addResourceLoader(ClassPathResourceLoader(Thread.currentThread().contextClassLoader ?: this.application::class.java.classLoader))

        return resourceLoader
    }
}