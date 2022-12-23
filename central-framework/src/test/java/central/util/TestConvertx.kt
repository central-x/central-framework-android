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

package central.util

import central.bean.convert.Converter
import org.junit.Assert.*
import org.junit.Test
import java.sql.Timestamp
import java.util.*

/**
 * Convertx Test Cases
 *
 * @author Alan Yeh
 * @since 2022/12/07
 */
class TestConvertx {
    /**
     * Boolean Convert
     */
    @Test
    fun case1() {
        // Boolean
        assertTrue(Convertx.support(Boolean::class.java, Boolean::class.java))
        var target = Convertx.convert(true, Boolean::class.java)
        assertEquals(true, target)

        target = Convertx.convert(false, Boolean::class.java)
        assertEquals(false, target)

        // String
        var strSource: String = "true"
        target = Convertx.convert(strSource, Boolean::class.java)
        assertEquals(true, target)

        strSource = "1"
        target = Convertx.convert(strSource, Boolean::class.java)
        assertEquals(true, target)

        strSource = "false"
        target = Convertx.convert(strSource, Boolean::class.java)
        assertEquals(false, target)

        strSource = "0"
        target = Convertx.convert(strSource, Boolean::class.java)
        assertEquals(false, target)

        strSource = "test"
        target = Convertx.convert(strSource, Boolean::class.java)
        assertEquals(false, target)

        // Integer
        var intSource: Int = 1
        target = Convertx.convert(intSource, Boolean::class.java)
        assertEquals(true, target)

        intSource = 0
        target = Convertx.convert(intSource, Boolean::class.java)
        assertEquals(false, target)

        intSource = -1
        target = Convertx.convert(intSource, Boolean::class.java)
        assertEquals(true, target)

        // Long
        var longSource: Long = 1L
        target = Convertx.convert(longSource, Boolean::class.java)
        assertEquals(true, target)

        longSource = 0L
        target = Convertx.convert(longSource, Boolean::class.java)
        assertEquals(false, target)

        longSource = -1L
        target = Convertx.convert(longSource, Boolean::class.java)
        assertEquals(true, target)
    }

    /**
     * Byte Convert
     */
    @Test
    fun case2() {
        // Byte
        val byteSource: Byte = Byte.MAX_VALUE
        assertTrue(Convertx.support(byteSource::class.java, Byte::class.java))
        var target = Convertx.convert(byteSource, Byte::class.java)
        assertEquals(byteSource, target)

        // Long
        val longSource: Long = byteSource.toLong()
        assertTrue(Convertx.support(longSource::class.java, Byte::class.java))
        target = Convertx.convert(longSource, Byte::class.java)
        assertEquals(byteSource, target)

        // Integer
        val intSource: Int = byteSource.toInt()
        assertTrue(Convertx.support(intSource::class.java, Byte::class.java))
        target = Convertx.convert(intSource, Byte::class.java)
        assertEquals(byteSource, target)
    }

    /**
     * Double Convert
     */
    @Test
    fun case3() {
        // Double
        val doubleSource: Double = "123".toDouble()
        assertTrue(Convertx.support(doubleSource::class.java, Double::class.java))
        var target = Convertx.convert(doubleSource, Double::class.java)
        assertEquals(doubleSource, target)

        // Long
        val longSource: Long = doubleSource.toLong()
        assertTrue(Convertx.support(longSource::class.java, Double::class.java))
        target = Convertx.convert(longSource, Double::class.java)
        assertEquals(doubleSource, target)

        // Integer
        val intSource: Int = doubleSource.toInt()
        assertTrue(Convertx.support(intSource::class.java, Double::class.java))
        target = Convertx.convert(intSource, Double::class.java)
        assertEquals(doubleSource, target)

        // Float
        val floatSource: Float = doubleSource.toFloat()
        assertTrue(Convertx.support(floatSource::class.java, Double::class.java))
        target = Convertx.convert(floatSource, Double::class.java)
        assertEquals(doubleSource, target)
    }

    /**
     * Float Convert
     */
    @Test
    fun case4() {
        // Float
        val floatSource: Float = "123".toFloat()
        assertTrue(Convertx.support(floatSource::class.java, Float::class.java))
        var target = Convertx.convert(floatSource, Float::class.java)
        assertEquals(floatSource, target)

        // Long
        val longSource: Long = floatSource.toLong()
        assertTrue(Convertx.support(longSource::class.java, Float::class.java))
        target = Convertx.convert(longSource, Float::class.java)
        assertEquals(floatSource, target)

        // Integer
        val intSource: Int = floatSource.toInt()
        assertTrue(Convertx.support(intSource::class.java, Float::class.java))
        target = Convertx.convert(intSource, Float::class.java)
        assertEquals(floatSource, target)

        // Double
        val doubleSource: Float = floatSource.toFloat()
        assertTrue(Convertx.support(doubleSource::class.java, Float::class.java))
        target = Convertx.convert(doubleSource, Float::class.java)
        assertEquals(doubleSource, target)
    }

    /**
     * Integer Convert
     */
    @Test
    fun case5() {
        // Integer
        val intSource: Int = "123".toInt()
        assertTrue(Convertx.support(intSource::class.java, Int::class.java))
        var target = Convertx.convert(intSource, Int::class.java)
        assertEquals(intSource, target)

        // Long
        val longSource: Long = intSource.toLong()
        assertTrue(Convertx.support(longSource::class.java, Int::class.java))
        target = Convertx.convert(longSource, Int::class.java)
        assertEquals(intSource, target)

        // String
        val stringSource: String = "$intSource"
        assertTrue(Convertx.support(stringSource::class.java, Int::class.java))
        target = Convertx.convert(stringSource, Int::class.java)
        assertEquals(intSource, target)
    }

    /**
     * Long Convert
     */
    @Test
    fun case6() {
        // Long
        val longSource: Long = 123L
        assertTrue(Convertx.support(longSource::class.java, Long::class.java))
        var target = Convertx.convert(longSource, Long::class.java)
        assertEquals(longSource, target)

        // Integer
        val intSource: Int = longSource.toInt()
        assertTrue(Convertx.support(intSource::class.java, Long::class.java))
        target = Convertx.convert(intSource, Long::class.java)
        assertEquals(longSource, target)

        // String
        val stringSource: String = "$longSource"
        assertTrue(Convertx.support(stringSource::class.java, Long::class.java))
        target = Convertx.convert(stringSource, Long::class.java)
        assertEquals(longSource, target)

        // Date
        val dateSource: Date = Date()
        assertTrue(Convertx.support(dateSource::class.java, Long::class.java))
        target = Convertx.convert(dateSource, Long::class.java)
        assertEquals(dateSource.time, target)

        // Timestamp
        val timestampSource: Timestamp = Timestamp(System.currentTimeMillis());
        assertTrue(Convertx.support(timestampSource::class.java, Long::class.java))
        target = Convertx.convert(timestampSource, Long::class.java)
        assertEquals(timestampSource.time, target)
    }

    /**
     * Short Convert
     */
    @Test
    fun case7() {
        // Short
        val shortSource: Short = "123".toShort()
        assertTrue(Convertx.support(shortSource::class.java, Short::class.java))
        var target = Convertx.convert(shortSource, Short::class.java)
        assertEquals(shortSource, target)

        // Long
        val longSource: Long = shortSource.toLong()
        assertTrue(Convertx.support(longSource::class.java, Short::class.java))
        target = Convertx.convert(longSource, Short::class.java)
        assertEquals(shortSource, target)

        // Integer
        val intSource: Int = shortSource.toInt()
        assertTrue(Convertx.support(intSource::class.java, Short::class.java))
        target = Convertx.convert(intSource, Short::class.java)
        assertEquals(shortSource, target)

        // Double
        val doubleSource: Double = shortSource.toDouble()
        assertTrue(Convertx.support(doubleSource::class.java, Short::class.java))
        target = Convertx.convert(doubleSource, Short::class.java)
        assertEquals(shortSource, target)

        // String
        val stringSource: String = "$shortSource"
        assertTrue(Convertx.support(stringSource::class.java, Short::class.java))
        target = Convertx.convert(stringSource, Short::class.java)
        assertEquals(shortSource, target)
    }

    /**
     * String Convert
     */
    @Test
    fun case8() {
        // String
        val stringSource: String = "string"
        assertTrue(Convertx.support(stringSource::class.java, String::class.java))
        var target = Convertx.convert(stringSource, String::class.java)
        assertEquals(stringSource, target)

        // Long
        val longSource: Long = 1L
        assertTrue(Convertx.support(longSource::class.java, String::class.java))
        target = Convertx.convert(longSource, String::class.java)
        assertEquals("1", target)

        // Integer
        val intSource: Int = 1
        assertTrue(Convertx.support(intSource::class.java, String::class.java))
        target = Convertx.convert(intSource, String::class.java)
        assertEquals("1", target)
    }

    /**
     * Timestamp Convert
     */
    @Test
    fun case9() {
        // Timestamp
        val timestampSource = Timestamp(System.currentTimeMillis())
        assertTrue(Convertx.support(timestampSource::class.java, Timestamp::class.java))
        var target = Convertx.convert(timestampSource, Timestamp::class.java)
        assertEquals(timestampSource, target)

        // Long
        val longSource: Long = timestampSource.time
        assertTrue(Convertx.support(longSource::class.java, Timestamp::class.java))
        target = Convertx.convert(longSource, Timestamp::class.java)
        assertEquals(timestampSource, target)
    }

    /**
     * Test Register
     */
    @Test
    fun case10() {
        assertFalse(Convertx.support(Int::class.java, LazyValue::class.java))
        assertFalse(Convertx.support(String::class.java, LazyValue::class.java))
        assertFalse(Convertx.support(Double::class.java, LazyValue::class.java))

        Convertx.register(LazyConverter())

        assertTrue(Convertx.support(Int::class.java, LazyValue::class.java))
        assertTrue(Convertx.support(String::class.java, LazyValue::class.java))
        assertTrue(Convertx.support(Double::class.java, LazyValue::class.java))

        val value = Convertx.convert(1, LazyValue::class.java)
        assertNotNull(value)
        assertTrue(value is LazyValue)
        assertEquals(1, value?.get())
    }

    class LazyConverter : Converter<LazyValue<*>> {
        override fun support(source: Class<*>): Boolean = true

        override fun convert(source: Any): LazyValue<*>? = LazyValue { source }

    }
}