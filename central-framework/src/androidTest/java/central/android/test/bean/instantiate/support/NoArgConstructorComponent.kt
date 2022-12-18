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

import central.android.test.bean.service.AccountService
import central.android.test.bean.service.DepartmentService
import central.android.test.bean.service.impl.AccountServiceImpl
import central.android.test.bean.service.impl.DepartmentServiceImpl
import central.bean.Validatable
import central.bean.factory.Autowired
import central.bean.factory.BeanNameAware
import org.junit.Assert
import java.util.*

/**
 * 无参构造函数组件
 *
 * @author Alan Yeh
 * @since 2023/02/16
 */
class NoArgConstructorComponent : BeanNameAware, Validatable {

    override lateinit var beanName: String

    /**
     * 通过字段注入
     */
    @Autowired
    lateinit var departmentService: DepartmentService

    /**
     * 通过 field 注入
     */
    @field:Autowired
    private var departmentService2: DepartmentService? = null

    /**
     * 通过 Setter 注入
     */
    @set:Autowired
    lateinit var accountService: AccountService

    /**
     * 测试非必要的注入
     */
    @Autowired(required = false)
    var noImplService: NoImplService? = null

    override fun validate() {
        // 测试所有的 Bean 是否有正确注入
        Assert.assertTrue(this::departmentService.isInitialized)
        Assert.assertTrue(this.departmentService is DepartmentServiceImpl)

        Assert.assertNotNull(this.departmentService2)
        Assert.assertTrue(this.departmentService2 is DepartmentServiceImpl)

        Assert.assertNotNull(this::accountService.isInitialized)
        Assert.assertTrue(this.accountService is AccountServiceImpl)

        Assert.assertNull(this.noImplService)

        val id = UUID.randomUUID().toString()
        val department = this.departmentService.findById(id)
        Assert.assertNotNull(department)
        Assert.assertEquals(department.id, id)
    }
}