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

package central.android.test.bean.instantiate

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import central.android.AndroidApplication
import central.android.test.bean.instantiate.support.AwareComponent
import central.android.test.bean.instantiate.support.ConstructorComponent
import central.android.test.bean.instantiate.support.NoArgConstructorComponent
import central.android.test.bean.service.impl.AccountServiceImpl
import central.android.test.bean.service.impl.DepartmentServiceImpl
import central.bean.factory.config.Bean
import central.bean.factory.config.Configuration
import central.bean.factory.config.Import
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * 实例化单元测试
 *
 * @author Alan Yeh
 * @since 2023/02/16
 */
@SmallTest
@RunWith(AndroidJUnit4::class)
class TestInstantiation {

    @Configuration
    @Import(AccountServiceImpl::class, DepartmentServiceImpl::class)
    class Case1Configuration {
        /**
         * 测试通过 Configuration 注入
         */
        @Bean
        fun constructorComponent(): NoArgConstructorComponent {
            return NoArgConstructorComponent()
        }
    }

    /**
     * 测试无参构造函数
     * 通过 setter 方法注入
     */
    @Test
    fun case1() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        AndroidApplication.run(application, Case1Configuration::class.java)

        val component = AndroidApplication.applicationContext.requireBean(NoArgConstructorComponent::class.java)
        component.validate()
        Assert.assertEquals(Case1Configuration::constructorComponent.name, component.beanName)

        AndroidApplication.stop()
    }

    @Configuration
    @Import(ConstructorComponent::class, AccountServiceImpl::class, DepartmentServiceImpl::class)
    class Case2Configuration

    /**
     * 测试通过构造函数注入
     */
    @Test
    fun case2(){
        val application = ApplicationProvider.getApplicationContext<Application>()
        AndroidApplication.run(application, Case2Configuration::class.java)

        val component = AndroidApplication.applicationContext.requireBean(ConstructorComponent::class.java)
        component.validate()

        AndroidApplication.stop()
    }

    @Configuration
    @Import(AwareComponent::class)
    class Case3Configuration

    /**
     * 测试通过 Aware 接口注入
     */
    @Test
    fun case3() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        AndroidApplication.run(application, Case3Configuration::class.java)

        val component = AndroidApplication.applicationContext.requireBean(AwareComponent::class.java)
        component.validate()

        AndroidApplication.stop()
    }
}