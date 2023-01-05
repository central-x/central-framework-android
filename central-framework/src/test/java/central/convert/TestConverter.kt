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

package central.convert

import central.convert.support.GenericConverter
import central.util.LazyValue
import org.junit.Assert.*
import org.junit.Test
import java.sql.Timestamp
import java.util.*

/**
 * ConversionService Test Cases
 *
 * @author Alan Yeh
 * @since 2022/12/07
 */
class TestConverter {
    /**
     * Boolean Convert
     */
    @Test
    fun case1() {
        val converter = GenericConverter()
        // Boolean
        assertTrue(converter.support(Boolean::class.java, Boolean::class.java))
        var target = converter.convert(true, Boolean::class.java)
        assertEquals(true, target)

        target = converter.convert(false, Boolean::class.java)
        assertEquals(false, target)

        // String
        var strSource: String = "true"
        target = converter.convert(strSource, Boolean::class.java)
        assertEquals(true, target)

        strSource = "1"
        target = converter.convert(strSource, Boolean::class.java)
        assertEquals(true, target)

        strSource = "false"
        target = converter.convert(strSource, Boolean::class.java)
        assertEquals(false, target)

        strSource = "0"
        target = converter.convert(strSource, Boolean::class.java)
        assertEquals(false, target)

        strSource = "test"
        target = converter.convert(strSource, Boolean::class.java)
        assertEquals(false, target)

        // Integer
        var intSource: Int = 1
        target = converter.convert(intSource, Boolean::class.java)
        assertEquals(true, target)

        intSource = 0
        target = converter.convert(intSource, Boolean::class.java)
        assertEquals(false, target)

        intSource = -1
        target = converter.convert(intSource, Boolean::class.java)
        assertEquals(true, target)

        // Long
        var longSource: Long = 1L
        target = converter.convert(longSource, Boolean::class.java)
        assertEquals(true, target)

        longSource = 0L
        target = converter.convert(longSource, Boolean::class.java)
        assertEquals(false, target)

        longSource = -1L
        target = converter.convert(longSource, Boolean::class.java)
        assertEquals(true, target)
    }

    /**
     * Byte Convert
     */
    @Test
    fun case2() {
        val converter = GenericConverter()
        // Byte
        val byteSource: Byte = Byte.MAX_VALUE
        assertTrue(converter.support(byteSource::class.java, Byte::class.java))
        var target = converter.convert(byteSource, Byte::class.java)
        assertEquals(byteSource, target)

        // Long
        val longSource: Long = byteSource.toLong()
        assertTrue(converter.support(longSource::class.java, Byte::class.java))
        target = converter.convert(longSource, Byte::class.java)
        assertEquals(byteSource, target)

        // Integer
        val intSource: Int = byteSource.toInt()
        assertTrue(converter.support(intSource::class.java, Byte::class.java))
        target = converter.convert(intSource, Byte::class.java)
        assertEquals(byteSource, target)
    }

    /**
     * Double Convert
     */
    @Test
    fun case3() {
        val converter = GenericConverter()
        // Double
        val doubleSource: Double = "123".toDouble()
        assertTrue(converter.support(doubleSource::class.java, Double::class.java))
        var target = converter.convert(doubleSource, Double::class.java)
        assertEquals(doubleSource, target)

        // Long
        val longSource: Long = doubleSource.toLong()
        assertTrue(converter.support(longSource::class.java, Double::class.java))
        target = converter.convert(longSource, Double::class.java)
        assertEquals(doubleSource, target)

        // Integer
        val intSource: Int = doubleSource.toInt()
        assertTrue(converter.support(intSource::class.java, Double::class.java))
        target = converter.convert(intSource, Double::class.java)
        assertEquals(doubleSource, target)

        // Float
        val floatSource: Float = doubleSource.toFloat()
        assertTrue(converter.support(floatSource::class.java, Double::class.java))
        target = converter.convert(floatSource, Double::class.java)
        assertEquals(doubleSource, target)
    }

    /**
     * Float Convert
     */
    @Test
    fun case4() {
        val converter = GenericConverter()
        // Float
        val floatSource: Float = "123".toFloat()
        assertTrue(converter.support(floatSource::class.java, Float::class.java))
        var target = converter.convert(floatSource, Float::class.java)
        assertEquals(floatSource, target)

        // Long
        val longSource: Long = floatSource.toLong()
        assertTrue(converter.support(longSource::class.java, Float::class.java))
        target = converter.convert(longSource, Float::class.java)
        assertEquals(floatSource, target)

        // Integer
        val intSource: Int = floatSource.toInt()
        assertTrue(converter.support(intSource::class.java, Float::class.java))
        target = converter.convert(intSource, Float::class.java)
        assertEquals(floatSource, target)

        // Double
        val doubleSource: Float = floatSource.toFloat()
        assertTrue(converter.support(doubleSource::class.java, Float::class.java))
        target = converter.convert(doubleSource, Float::class.java)
        assertEquals(doubleSource, target)
    }

    /**
     * Integer Convert
     */
    @Test
    fun case5() {
        val converter = GenericConverter()
        // Integer
        val intSource: Int = "123".toInt()
        assertTrue(converter.support(intSource::class.java, Int::class.java))
        var target = converter.convert(intSource, Int::class.java)
        assertEquals(intSource, target)

        // Long
        val longSource: Long = intSource.toLong()
        assertTrue(converter.support(longSource::class.java, Int::class.java))
        target = converter.convert(longSource, Int::class.java)
        assertEquals(intSource, target)

        // String
        val stringSource: String = "$intSource"
        assertTrue(converter.support(stringSource::class.java, Int::class.java))
        target = converter.convert(stringSource, Int::class.java)
        assertEquals(intSource, target)
    }

    /**
     * Long Convert
     */
    @Test
    fun case6() {
        val converter = GenericConverter()
        // Long
        val longSource: Long = 123L
        assertTrue(converter.support(longSource::class.java, Long::class.java))
        var target = converter.convert(longSource, Long::class.java)
        assertEquals(longSource, target)

        // Integer
        val intSource: Int = longSource.toInt()
        assertTrue(converter.support(intSource::class.java, Long::class.java))
        target = converter.convert(intSource, Long::class.java)
        assertEquals(longSource, target)

        // String
        val stringSource: String = "$longSource"
        assertTrue(converter.support(stringSource::class.java, Long::class.java))
        target = converter.convert(stringSource, Long::class.java)
        assertEquals(longSource, target)

        // Date
        val dateSource: Date = Date()
        assertTrue(converter.support(dateSource::class.java, Long::class.java))
        target = converter.convert(dateSource, Long::class.java)
        assertEquals(dateSource.time, target)

        // Timestamp
        val timestampSource: Timestamp = Timestamp(System.currentTimeMillis());
        assertTrue(converter.support(timestampSource::class.java, Long::class.java))
        target = converter.convert(timestampSource, Long::class.java)
        assertEquals(timestampSource.time, target)
    }

    /**
     * Short Convert
     */
    @Test
    fun case7() {
        val converter = GenericConverter()
        // Short
        val shortSource: Short = "123".toShort()
        assertTrue(converter.support(shortSource::class.java, Short::class.java))
        var target = converter.convert(shortSource, Short::class.java)
        assertEquals(shortSource, target)

        // Long
        val longSource: Long = shortSource.toLong()
        assertTrue(converter.support(longSource::class.java, Short::class.java))
        target = converter.convert(longSource, Short::class.java)
        assertEquals(shortSource, target)

        // Integer
        val intSource: Int = shortSource.toInt()
        assertTrue(converter.support(intSource::class.java, Short::class.java))
        target = converter.convert(intSource, Short::class.java)
        assertEquals(shortSource, target)

        // Double
        val doubleSource: Double = shortSource.toDouble()
        assertTrue(converter.support(doubleSource::class.java, Short::class.java))
        target = converter.convert(doubleSource, Short::class.java)
        assertEquals(shortSource, target)

        // String
        val stringSource: String = "$shortSource"
        assertTrue(converter.support(stringSource::class.java, Short::class.java))
        target = converter.convert(stringSource, Short::class.java)
        assertEquals(shortSource, target)
    }

    /**
     * String Convert
     */
    @Test
    fun case8() {
        val converter = GenericConverter()
        // String
        val stringSource: String = "string"
        assertTrue(converter.support(stringSource::class.java, String::class.java))
        var target = converter.convert(stringSource, String::class.java)
        assertEquals(stringSource, target)

        // Long
        val longSource: Long = 1L
        assertTrue(converter.support(longSource::class.java, String::class.java))
        target = converter.convert(longSource, String::class.java)
        assertEquals("1", target)

        // Integer
        val intSource: Int = 1
        assertTrue(converter.support(intSource::class.java, String::class.java))
        target = converter.convert(intSource, String::class.java)
        assertEquals("1", target)
    }

    /**
     * Timestamp Convert
     */
    @Test
    fun case9() {
        val converter = GenericConverter()
        // Timestamp
        val timestampSource = Timestamp(System.currentTimeMillis())
        assertTrue(converter.support(timestampSource::class.java, Timestamp::class.java))
        var target = converter.convert(timestampSource, Timestamp::class.java)
        assertEquals(timestampSource, target)

        // Long
        val longSource: Long = timestampSource.time
        assertTrue(converter.support(longSource::class.java, Timestamp::class.java))
        target = converter.convert(longSource, Timestamp::class.java)
        assertEquals(timestampSource, target)
    }

    /**
     * Test Register
     */
    @Test
    fun case10() {
        val converter = GenericConverter()
        assertFalse(converter.support(Int::class.java, LazyValue::class.java))
        assertFalse(converter.support(String::class.java, LazyValue::class.java))
        assertFalse(converter.support(Double::class.java, LazyValue::class.java))

        converter.register(LazyConverter())

        assertTrue(converter.support(Int::class.java, LazyValue::class.java))
        assertTrue(converter.support(String::class.java, LazyValue::class.java))
        assertTrue(converter.support(Double::class.java, LazyValue::class.java))

        val value = converter.convert(1, LazyValue::class.java)
        assertNotNull(value)
        assertTrue(value is LazyValue)
        assertEquals(1, value?.get())
    }

    class LazyConverter : TypeConverter<LazyValue<*>> {
        override fun support(source: Class<*>): Boolean = true

        override fun convert(source: Any): LazyValue<*>? = LazyValue { source }

    }
}