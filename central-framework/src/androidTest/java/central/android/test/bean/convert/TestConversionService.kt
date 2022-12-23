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

package central.android.test.bean.convert

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import central.android.AndroidApplication
import central.android.test.bean.convert.support.Sql
import central.android.test.bean.convert.support.SqlConverter
import central.bean.convert.ConversionService
import central.bean.factory.config.Configuration
import central.bean.factory.config.Import
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 测试类型转换服务
 *
 * @author Alan Yeh
 * @since 2023/02/18
 */
@SmallTest
@RunWith(AndroidJUnit4::class)
class TestConversionService {

    @Configuration
    @Import(SqlConverter::class)
    class Case1Configuration

    @Test
    fun case1() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        AndroidApplication.run(application, Case1Configuration::class.java)

        val converter = AndroidApplication.applicationContext.requireBean(ConversionService::class.java)

        Assert.assertTrue(converter.support(String::class.java, Sql::class.java))

        val str = "SELECT * FROM T_TABLE"
        val sql = converter.convert(str, Sql::class.java)
        Assert.assertNotNull(sql)
        Assert.assertTrue(sql is Sql)
        Assert.assertEquals(str, sql?.value)

        AndroidApplication.stop()
    }
}