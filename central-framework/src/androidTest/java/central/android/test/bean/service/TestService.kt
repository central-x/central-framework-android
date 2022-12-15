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

package central.android.test.bean.service

import android.util.Log
import central.bean.context.ApplicationContext
import central.bean.context.ApplicationContextAware
import central.bean.factory.InitializingBean
import central.bean.factory.config.Component
import java.util.*

/**
 * 测试服务
 *
 * @author Alan Yeh
 * @since 2023/02/14
 */
@Component
class TestService() : ApplicationContextAware, InitializingBean {
    override lateinit var applicationContext: ApplicationContext

    constructor(accountService: AccountService, departmentService: DepartmentService) : this() {
        this.accountService = accountService
        this.departmentService = departmentService
    }

    //    @set:Autowired
    lateinit var accountService: AccountService

    //    @set:Autowired
    lateinit var departmentService: DepartmentService

//    @Autowired
//    fun inject(accountService: AccountService, departmentService: DepartmentService) {
//        this.accountService = accountService
//        this.departmentService = departmentService
//    }

    override fun initialize() {
        val department = this.departmentService.findById(UUID.randomUUID().toString())
        Log.d("TestService", "test")
    }
}