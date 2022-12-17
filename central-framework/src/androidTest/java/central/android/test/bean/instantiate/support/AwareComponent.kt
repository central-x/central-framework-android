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

package central.android.test.bean.instantiate.support

import central.bean.Validatable
import central.bean.context.ApplicationContext
import central.bean.context.ApplicationContextAware
import central.bean.factory.*
import central.bean.factory.config.Component
import central.io.ResourceLoader
import org.junit.Assert

/**
 * 测试 Aware 接口
 *
 * @author Alan Yeh
 * @since 2023/02/17
 */
@Component("aware-component")
class AwareComponent : ApplicationContextAware, ResourceLoaderAware, BeanFactoryAware, BeanNameAware, Validatable {
    /**
     * 通过 Aware 接口注入
     */
    override lateinit var applicationContext: ApplicationContext
    override lateinit var resourceLoader: ResourceLoader
    override lateinit var beanFactory: BeanFactory
    override lateinit var beanName: String

    override fun validate() {
        Assert.assertTrue(this::applicationContext.isInitialized)
        Assert.assertNotNull(this::resourceLoader.isInitialized)
        Assert.assertNotNull(this::beanFactory.isInitialized)
        Assert.assertNotNull(this::beanName.isInitialized)
        Assert.assertEquals("aware-component", beanName)
    }
}