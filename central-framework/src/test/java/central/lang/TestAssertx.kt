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

package central.lang

import org.junit.Assert.*
import org.junit.Test

/**
 * Assertx Test Cases
 *
 * @author Alan Yeh
 * @since 2022/12/09
 */
class TestAssertx {

    /**
     * @see Assertx.mustTrue
     */
    @Test
    fun case1() {
        assertThrows(IllegalArgumentException::class.java) {
            Assertx.mustTrue("test".toByteArray().isEmpty(), "Test Assertx#mustTrue")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustTrue("test".toByteArray().isEmpty(), ::IllegalStateException, "Test Assertx#mustTrue")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustTrue("test".toByteArray().isEmpty()) { IllegalStateException("Test Assertx#mustTrue") }
        }
    }

    /**
     * @see Assertx.mustFalse
     */
    @Test
    fun case2() {
        assertThrows(IllegalArgumentException::class.java) {
            Assertx.mustFalse("test".toByteArray().isNotEmpty(), "Test Assertx#mustFalse")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustFalse("test".toByteArray().isNotEmpty(), ::IllegalStateException, "Test Assertx#mustFalse")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustFalse("test".toByteArray().isNotEmpty()) { IllegalStateException("Test Assertx#mustFalse") }
        }
    }

    /**
     * @see Assertx.mustEquals
     */
    @Test
    fun case3() {
        assertThrows(IllegalArgumentException::class.java) {
            Assertx.mustEquals("a", 1, "Test Assertx#mustEquals")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustEquals("a", 1, ::IllegalStateException, "Test Assertx#mustEquals")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustEquals("a", 1) { IllegalStateException("Test Assertx#mustEquals") }
        }
    }

    /**
     * @see Assertx.mustNotEquals
     */
    @Test
    fun case4() {
        assertThrows(IllegalArgumentException::class.java) {
            Assertx.mustNotEquals("a", "a", "Test Assertx#mustNotEquals")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNotEquals("a", "a", ::IllegalStateException, "Test Assertx#mustNotEquals")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNotEquals("a", "a") { IllegalStateException("Test Assertx#mustNotEquals") }
        }
    }

    /**
     * @see Assertx.mustNull
     */
    @Test
    fun case5() {
        assertThrows(IllegalArgumentException::class.java) {
            Assertx.mustNull("a", "Test Assertx#mustNull")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNull("a", ::IllegalStateException, "Test Assertx#mustNull")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNull("a") { IllegalStateException("Test Assertx#mustNull") }
        }
    }

    /**
     * @see Assertx.requireNotNull
     */
    @Test
    fun case6() {
        val nullableValue: Int? = null
        assertThrows(IllegalArgumentException::class.java) {
            val value = Assertx.requireNotNull(nullableValue, "Test Assertx#requireNotNull")
            assertNotNull(value)
        }

        assertThrows(IllegalStateException::class.java) {
            val value = Assertx.requireNotNull(nullableValue, ::IllegalStateException, "Test Assertx#requireNotNull")
            assertNotNull(value)
        }

        assertThrows(IllegalStateException::class.java) {
            val value = Assertx.requireNotNull(nullableValue) { IllegalStateException("Test Assertx#requireNotNull") }
            assertNotNull(value)
        }
    }

    /**
     * @see Assertx.mustNotNull
     */
    @Test
    fun case7() {
        val nullableValue: Int? = null
        assertThrows(IllegalArgumentException::class.java) {
            Assertx.mustNotNull(nullableValue, "Test Assertx#mustNotNull")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNotNull(nullableValue, ::IllegalStateException, "Test Assertx#mustNotNull")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNotNull(nullableValue) { IllegalStateException("Test Assertx#mustNotNull") }
        }
    }

    /**
     * @see Assertx.requireNotEmpty
     */
    @Test
    fun case8() {
        val nullableValue: String? = null
        assertThrows(IllegalArgumentException::class.java) {
            val value = Assertx.requireNotEmpty(nullableValue, "Test Assertx#requireNotEmpty")
            assertTrue(value.isNotEmpty())
        }

        assertThrows(IllegalStateException::class.java) {
            val value = Assertx.requireNotEmpty(nullableValue, ::IllegalStateException, "Test Assertx#requireNotEmpty")
            assertTrue(value.isNotEmpty())
        }

        assertThrows(IllegalStateException::class.java) {
            val value = Assertx.requireNotEmpty(nullableValue) { IllegalStateException("Test Assertx#requireNotEmpty") }
            assertTrue(value.isNotEmpty())
        }
    }

    /**
     * @see Assertx.mustNotEmpty
     */
    @Test
    fun case9() {
        val nullString: String? = null
        assertThrows(IllegalArgumentException::class.java) {
            Assertx.mustNotEmpty(nullString, "Test Assertx#mustNotEmpty")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNotEmpty(nullString, ::IllegalStateException, "Test Assertx#mustNotEmpty")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNotEmpty(nullString) { IllegalStateException("Test Assertx#mustNotEmpty") }
        }
    }

    /**
     * @see Assertx.mustNullOrEmpty
     */
    @Test
    fun case10() {
        val notEmptyString = "abc"
        assertThrows(IllegalArgumentException::class.java) {
            Assertx.mustNullOrEmpty(notEmptyString, "Test Assertx#mustNullOrEmpty")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNullOrEmpty(notEmptyString, ::IllegalStateException, "Test Assertx#mustNullOrEmpty")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNullOrEmpty(notEmptyString) { IllegalStateException("Test Assertx#mustNullOrEmpty") }
        }
    }

    /**
     * @see Assertx.requireNotBlank
     */
    @Test
    fun case11() {
        val blankString = "   "
        assertThrows(IllegalArgumentException::class.java) {
            val value = Assertx.requireNotBlank(blankString, "Test Assertx#requireNotBlank")
            assertTrue(value.isNotBlank())
        }

        assertThrows(IllegalStateException::class.java) {
            val value = Assertx.requireNotBlank(blankString, ::IllegalStateException, "Test Assertx#requireNotBlank")
            assertTrue(value.isNotBlank())
        }

        assertThrows(IllegalStateException::class.java) {
            val value = Assertx.requireNotBlank(blankString) { IllegalStateException("Test Assertx#requireNotBlank") }
            assertTrue(value.isNotBlank())
        }
    }

    /**
     * @see Assertx.mustNotBlank
     */
    @Test
    fun case12() {
        val blankString = "   "
        assertThrows(IllegalArgumentException::class.java) {
            Assertx.mustNotBlank(blankString, "Test Assertx#mustNotBlank")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNotBlank(blankString, ::IllegalStateException, "Test Assertx#mustNotBlank")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNotBlank(blankString) { IllegalStateException("Test Assertx#mustNotBlank") }
        }
    }

    /**
     * @see Assertx.mustNullOrBlank
     */
    @Test
    fun case13() {
        val notBlankString = "  d "
        assertThrows(IllegalArgumentException::class.java) {
            Assertx.mustNullOrBlank(notBlankString, "Test Assertx#mustNullOrBlank")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNullOrBlank(notBlankString, ::IllegalStateException, "Test Assertx#mustNullOrBlank")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNullOrBlank(notBlankString) { IllegalStateException("Test Assertx#mustNullOrBlank") }
        }
    }

    /**
     * @see Assertx.requireNotEmpty
     */
    @Test
    fun case14() {
        val emptyArray: Array<String> = Array(0) { "" }
        assertThrows(IllegalArgumentException::class.java) {
            val value = Assertx.requireNotEmpty(emptyArray, "Test Assertx#requireNotEmpty")
            assertTrue(value.isNotEmpty())
        }

        assertThrows(IllegalStateException::class.java) {
            val value = Assertx.requireNotEmpty(emptyArray, ::IllegalStateException, "Test Assertx#requireNotEmpty")
            assertTrue(value.isNotEmpty())
        }

        assertThrows(IllegalStateException::class.java) {
            val value = Assertx.requireNotEmpty(emptyArray) { IllegalStateException("Test Assertx#requireNotEmpty") }
            assertTrue(value.isNotEmpty())
        }
    }

    /**
     * @see Assertx.mustNotEmpty
     */
    @Test
    fun case15() {
        val emptyArray: Array<String> = Array(0) { "" }
        assertThrows(IllegalArgumentException::class.java) {
            Assertx.mustNotEmpty(emptyArray, "Test Assertx#mustNotEmpty")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNotEmpty(emptyArray, ::IllegalStateException, "Test Assertx#mustNotEmpty")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNotEmpty(emptyArray) { IllegalStateException("Test Assertx#mustNotEmpty") }
        }
    }

    /**
     * @see Assertx.requireNotEmpty
     */
    @Test
    fun case16() {
        val emptyCollection = mutableListOf<String>()
        assertThrows(IllegalArgumentException::class.java) {
            val value = Assertx.requireNotEmpty(emptyCollection, "Test Assertx#requireNotEmpty")
            assertTrue(value.isNotEmpty())
        }

        assertThrows(IllegalStateException::class.java) {
            val value = Assertx.requireNotEmpty(emptyCollection, ::IllegalStateException, "Test Assertx#requireNotEmpty")
            assertTrue(value.isNotEmpty())
        }

        assertThrows(IllegalStateException::class.java) {
            val value = Assertx.requireNotEmpty(emptyCollection) { IllegalStateException("Test Assertx#requireNotEmpty") }
            assertTrue(value.isNotEmpty())
        }
    }

    /**
     * @see Assertx.mustNotEmpty
     */
    @Test
    fun case17() {
        val emptyCollection = mutableListOf<String>()
        assertThrows(IllegalArgumentException::class.java) {
            Assertx.mustNotEmpty(emptyCollection, "Test Assertx#mustNotEmpty")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNotEmpty(emptyCollection, ::IllegalStateException, "Test Assertx#mustNotEmpty")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNotEmpty(emptyCollection) { IllegalStateException("Test Assertx#mustNotEmpty") }
        }
    }

    /**
     * @see Assertx.requireNotEmpty
     */
    @Test
    fun case18() {
        val emptyMap = mutableMapOf<String, String>()
        assertThrows(IllegalArgumentException::class.java) {
            val value = Assertx.requireNotEmpty(emptyMap, "Test Assertx#requireNotEmpty")
            assertTrue(value.isNotEmpty())
        }

        assertThrows(IllegalStateException::class.java) {
            val value = Assertx.requireNotEmpty(emptyMap, ::IllegalStateException, "Test Assertx#requireNotEmpty")
            assertTrue(value.isNotEmpty())
        }

        assertThrows(IllegalStateException::class.java) {
            val value = Assertx.requireNotEmpty(emptyMap) { IllegalStateException("Test Assertx#requireNotEmpty") }
            assertTrue(value.isNotEmpty())
        }
    }

    /**
     * @see Assertx.mustNotEmpty
     */
    @Test
    fun case19() {
        val emptyMap = mutableMapOf<String, String>()
        assertThrows(IllegalArgumentException::class.java) {
            Assertx.mustNotEmpty(emptyMap, "Test Assertx#mustNotEmpty")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNotEmpty(emptyMap, ::IllegalStateException, "Test Assertx#mustNotEmpty")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustNotEmpty(emptyMap) { IllegalStateException("Test Assertx#mustNotEmpty") }
        }
    }

    /**
     * @see Assertx.requireInstanceOf
     */
    @Test
    fun case20() {
        val any: Any = "1"
        assertThrows(IllegalArgumentException::class.java) {
            val value = Assertx.requireInstanceOf(Int::class.java, any, "Test Assertx#requireInstanceOf")
            assertTrue(Int::class.java.isAssignableFrom(value::class.java))
        }

        assertThrows(IllegalStateException::class.java) {
            val value = Assertx.requireInstanceOf(Int::class.java, any, ::IllegalStateException, "Test Assertx#requireInstanceOf")
            assertTrue(Int::class.java.isAssignableFrom(value::class.java))
        }

        assertThrows(IllegalStateException::class.java) {
            val value = Assertx.requireInstanceOf(Int::class.java, any) { IllegalStateException("Test Assertx#requireInstanceOf") }
            assertTrue(Int::class.java.isAssignableFrom(value::class.java))
        }
    }

    /**
     * @see Assertx.mustInstanceOf
     */
    @Test
    fun case21() {
        val any: Any = "1"
        assertThrows(IllegalArgumentException::class.java) {
            Assertx.mustInstanceOf(Int::class.java, any, "Test Assertx#mustInstanceOf")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustInstanceOf(Int::class.java, any, ::IllegalStateException, "Test Assertx#mustInstanceOf")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustInstanceOf(Int::class.java, any) { IllegalStateException("Test Assertx#mustInstanceOf") }
        }
    }

    /**
     * @see Assertx.requiredAssignableFrom
     */
    @Test
    fun case22() {
        val anyType = "1"::class.java
        assertThrows(IllegalArgumentException::class.java) {
            val value = Assertx.requiredAssignableFrom(Int::class.java, anyType, "Test Assertx#requiredAssignableFrom")
            assertTrue(Int::class.java.isAssignableFrom(value))
        }

        assertThrows(IllegalStateException::class.java) {
            val value = Assertx.requiredAssignableFrom(Int::class.java, anyType, ::IllegalStateException, "Test Assertx#requiredAssignableFrom")
            assertTrue(Int::class.java.isAssignableFrom(value))
        }

        assertThrows(IllegalStateException::class.java) {
            val value = Assertx.requiredAssignableFrom(Int::class.java, anyType) { IllegalStateException("Test Assertx#requiredAssignableFrom") }
            assertTrue(Int::class.java.isAssignableFrom(value))
        }
    }

    /**
     * @see Assertx.mustAssignableFrom
     */
    @Test
    fun case23() {
        val anyType = "1"::class.java
        assertThrows(IllegalArgumentException::class.java) {
            Assertx.mustAssignableFrom(Int::class.java, anyType, "Test Assertx#mustAssignableFrom")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustAssignableFrom(Int::class.java, anyType, ::IllegalStateException, "Test Assertx#mustAssignableFrom")
        }

        assertThrows(IllegalStateException::class.java) {
            Assertx.mustAssignableFrom(Int::class.java, anyType) { IllegalStateException("Test Assertx#mustAssignableFrom") }
        }
    }

}