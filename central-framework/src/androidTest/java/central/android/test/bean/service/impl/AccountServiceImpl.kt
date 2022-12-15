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

package central.android.test.bean.service.impl

import central.android.test.bean.data.Account
import central.android.test.bean.data.Department
import central.android.test.bean.service.AccountService
import central.bean.factory.config.Component
import java.util.*

/**
 * 帐户服务
 *
 * @author Alan Yeh
 * @since 2023/02/14
 */
@Component
class AccountServiceImpl : AccountService {

//    @Autowired
//    private lateinit var departmentService: DepartmentService

    override fun findByDepartment(department: Department): List<Account> {
        val accounts = mutableListOf<Account>()
        for (index in 0..9) {
            val account = Account()
            account.id = UUID.randomUUID().toString()
            account.name = "张 $index"
            account.age = index
            account.departmentId = department.id
            account.department = department
            accounts.add(account)
        }
        return accounts
    }
}