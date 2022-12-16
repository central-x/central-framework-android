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

import central.android.test.bean.data.Department
import central.android.test.bean.service.AccountService
import central.android.test.bean.service.DepartmentService
import central.bean.factory.Autowired
import central.bean.factory.config.Component

/**
 * 无参构造函数组件
 *
 * @author Alan Yeh
 * @since 2023/02/16
 */
@Component
class NoArgConstructorComponent {

    /**
     * 通过字段注入
     */
    @Autowired
    lateinit var departmentService: DepartmentService

    /**
     * 通过 field 注入
     */
    @field:Autowired
    private lateinit var departmentService2: DepartmentService

    /**
     * 通过 Setter 注入
     */
    @set:Autowired
    lateinit var accountService: AccountService

    /**
     * 测试非必要的注入
     */
    @Autowired(required = false)
    lateinit var noImplService: NoImplService

    fun findById(id: String): Department {
        return departmentService.findById(id)
    }
}