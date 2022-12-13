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

import java.util.function.Function
import java.util.function.Supplier

/**
 * 断言工具
 *
 * @author Alan Yeh
 * @since 2022/12/07
 */
class Assertx private constructor() {
    companion object {
        /**
         * Assertx.must(b is Int, ::ClassCastException, "Cannot cast 'b' to ${Int::class.java.name}")
         */
        private fun <E : Throwable> must(expression: Boolean, throwable: Function<String?, E>, message: String?) {
            if (!expression) {
                throw throwable.apply(message)
            }
        }

        /**
         * Assertx.must(b is Int) { ClassCastException("Cannot cast 'b' to ${Int::class.java.name}")}
         */
        private fun <E : Throwable> must(expression: Boolean, throwable: Supplier<E>) {
            if (!expression) {
                throw throwable.get()
            }
        }

        /**
         * Assertx.mustTrue(index < length, "Index out of range: $length")
         */
        fun mustTrue(expression: Boolean, message: String) {
            must(expression, ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustTrue(index < length, ::IndexOutOfBoundsException, "Index out of range: $length")
         */
        fun <E : Throwable> mustTrue(expression: Boolean, throwable: Function<String?, E>, message: String) {
            must(expression, throwable, message)
        }

        /**
         * Assertx.mustTrue(index < length) { IndexOutOfBoundsException("Index out of range: $length")}
         */
        fun <E : Throwable> mustTrue(expression: Boolean, throwable: Supplier<E>) {
            must(expression, throwable)
        }

        /**
         * Assertx.mustFalse(index >= length, "Index out of range: $length")
         */
        fun mustFalse(expression: Boolean, message: String) {
            must(!expression, ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustTrue(index >= length, ::IndexOutOfBoundsException, "Index out of range: $length")
         */
        fun <E : Throwable> mustFalse(expression: Boolean, throwable: Function<String?, E>, message: String) {
            must(!expression, throwable, message)
        }

        /**
         * Assertx.mustFalse(index >= length) { IndexOutOfBoundsException("Index out of range: $length")}
         */
        fun <E : Throwable> mustFalse(expression: Boolean, throwable: Supplier<E>) {
            must(!expression, throwable)
        }

        /**
         * Assertx.mustEquals(a, b, "a must equals to b")
         */
        fun mustEquals(expected: Any, actual: Any?, message: String) {
            must(expected == actual, ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustEquals(a, b, ::IllegalArgumentException, "a must equals to b")
         */
        fun <E : Throwable> mustEquals(expected: Any, actual: Any?, throwable: Function<String?, E>, message: String) {
            must(expected == actual, throwable, message)
        }

        /**
         * Assertx.mustEquals(a, b) { IllegalArgumentException("a must equals to b")}
         */
        fun <E : Throwable> mustEquals(expected: Any, actual: Any?, throwable: Supplier<E>) {
            must(expected == actual, throwable)
        }

        /**
         * Assertx.mustNotEquals(a, b, "a must not equals to b")
         */
        fun mustNotEquals(expected: Any, actual: Any?, message: String) {
            must(expected != actual, ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustNotEquals(a, b, ::IllegalArgumentException, "a must not equals to b")
         */
        fun <E : Throwable> mustNotEquals(expected: Any, actual: Any?, throwable: Function<String?, E>, message: String) {
            must(expected != actual, throwable, message)
        }

        /**
         * Assertx.mustNotEquals(a, b) { IllegalArgumentException("a must not equals to b")}
         */
        fun <E : Throwable> mustNotEquals(expected: Any, actual: Any?, throwable: Supplier<E>) {
            must(expected != actual, throwable)
        }

        /**
         * var value = Assertx.requireNull(nullableValue, "The value must null")
         */
        fun <R> requireNull(any: R?, message: String): R {
            must(any != null, ::IllegalArgumentException, message)
            return any!!
        }

        /**
         * var value = Assertx.requireNull(nullableValue, ::IllegalArgumentException, "The value must null")
         */
        fun <R, E : Throwable> requireNull(any: R?, throwable: Function<String?, E>, message: String): R? {
            must(any != null, throwable, message)
            return any!!
        }

        /**
         * var value = Assertx.requireNull(nullableValue)  { IllegalArgumentException("The value must null") }
         */
        fun <R, E : Throwable> requireNull(any: R?, throwable: Supplier<E>): R? {
            must(any != null, throwable)
            return any!!
        }

        /**
         * Assertx.mustNull(value, "Expect null value")
         */
        fun mustNull(expected: Any?, message: String) {
            must(expected == null, ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustNull(value, ::IllegalArgumentException, "Expect null object")
         */
        fun <E : Throwable> mustNull(expected: Any?, throwable: Function<String?, E>, message: String) {
            must(expected == null, throwable, message)
        }

        /**
         * Assertx.mustNull(value) { IllegalArgumentException("Expect null object") }
         */
        fun <E : Throwable> mustNull(expected: Any?, throwable: Supplier<E>) {
            must(expected == null, throwable)
        }

        /**
         * var value = Assertx.requireNotNull(nullableValue, "The value must not null")
         */
        fun <R> requireNotNull(any: R?, message: String): R {
            must(any != null, ::IllegalArgumentException, message)
            return any!!
        }

        /**
         * var value = Assertx.requireNotNull(nullableValue, ::IllegalArgumentException, "The value must not null")
         */
        fun <R, E : Throwable> requireNotNull(any: R?, throwable: Function<String?, E>, message: String): R {
            must(any != null, throwable, message)
            return any!!
        }

        /**
         * var value = Assertx.requireNotNull(nullableValue)  { IllegalArgumentException("The value must not null") }
         */
        fun <R, E : Throwable> requireNotNull(any: R?, throwable: Supplier<E>): R {
            must(any != null, throwable)
            return any!!
        }

        /**
         * Assertx.mustNotNull(value, "The value must not null")
         */
        fun mustNotNull(any: Any?, message: String) {
            must(any != null, ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustNotNull(value, ::IllegalArgumentException, "The value must not null")
         */
        fun <E : Throwable> mustNotNull(expected: Any?, throwable: Function<String?, E>, message: String) {
            must(expected != null, throwable, message)
        }

        /**
         * Assertx.mustNotNull(value) { IllegalArgumentException("The value must not null") }
         */
        fun <E : Throwable> mustNotNull(expected: Any?, throwable: Supplier<E>) {
            must(expected != null, throwable)
        }

        /**
         * val string = Assertx.requireNotEmpty(value, "The value must not empty")
         */
        fun requireNotEmpty(text: String?, message: String): String {
            must(!text.isNullOrEmpty(), ::IllegalArgumentException, message)
            return text!!
        }

        /**
         * val string = Assertx.requireNotEmpty(value, ::IllegalArgumentException, "The value must not empty")
         */
        fun <E : Throwable> requireNotEmpty(text: String?, throwable: Function<String?, E>, message: String): String {
            must(!text.isNullOrEmpty(), throwable, message)
            return text!!
        }

        /**
         * val string = Assertx.requireNotEmpty(value)  { IllegalArgumentException("The value must not empty") }
         */
        fun <E : Throwable> requireNotEmpty(text: String?, throwable: Supplier<E>): String {
            must(!text.isNullOrEmpty(), throwable)
            return text!!
        }

        /**
         * Assertx.mustNotEmpty(value, "The value must not empty")
         */
        fun mustNotEmpty(text: String?, message: String) {
            must(!text.isNullOrEmpty(), ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustNotEmpty(value, ::IllegalArgumentException, "The value must not empty")
         */
        fun <E : Throwable> mustNotEmpty(text: String?, throwable: Function<String?, E>, message: String) {
            must(!text.isNullOrEmpty(), throwable, message)
        }

        /**
         * Assertx.mustNotEmpty(value) { IllegalArgumentException("The value must not empty") }
         */
        fun <E : Throwable> mustNotEmpty(text: String?, throwable: Supplier<E>) {
            must(!text.isNullOrEmpty(), throwable)
        }

        /**
         * Assertx.mustNotEmpty(value, "The value must null or empty")
         */
        fun mustNullOrEmpty(text: String?, message: String) {
            must(text.isNullOrEmpty(), ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustNotEmpty(value, ::IllegalArgumentException, "The value must null or empty")
         */
        fun <E : Throwable> mustNullOrEmpty(text: String?, throwable: Function<String?, E>, message: String) {
            must(text.isNullOrEmpty(), throwable, message)
        }

        /**
         * Assertx.mustNotEmpty(value) { IllegalArgumentException("The value must null or empty") }
         */
        fun <E : Throwable> mustNullOrEmpty(text: String?, throwable: Supplier<E>) {
            must(text.isNullOrEmpty(), throwable)
        }

        /**
         * val string = Assertx.requireNotBlank(value, "The value must not blank")
         */
        fun requireNotBlank(text: String?, message: String): String {
            must(!text.isNullOrBlank(), ::IllegalArgumentException, message)
            return text!!
        }

        /**
         * val string = Assertx.requireNotBlank(value, ::IllegalArgumentException, "The value must not blank")
         */
        fun <E : Throwable> requireNotBlank(text: String?, throwable: Function<String?, E>, message: String): String {
            must(!text.isNullOrBlank(), throwable, message)
            return text!!
        }

        /**
         * val string = Assertx.requireNotBlank(value)  { IllegalArgumentException("The value must not blank") }
         */
        fun <E : Throwable> requireNotBlank(text: String?, throwable: Supplier<E>): String {
            must(!text.isNullOrBlank(), throwable)
            return text!!
        }

        /**
         * Assertx.mustNotBlank(value, "The value must not blank")
         */
        fun mustNotBlank(text: String?, message: String) {
            must(!text.isNullOrBlank(), ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustNotBlank(value, ::IllegalArgumentException, "The value must not blank")
         */
        fun <E : Throwable> mustNotBlank(text: String?, throwable: Function<String?, E>, message: String) {
            must(!text.isNullOrBlank(), throwable, message)
        }

        /**
         * Assertx.mustNotBlank(value) { IllegalArgumentException("The value must not blank") }
         */
        fun <E : Throwable> mustNotBlank(text: String?, throwable: Supplier<E>) {
            must(!text.isNullOrBlank(), throwable)
        }

        /**
         * Assertx.mustNullOrBlank(value, "The value must null or blank")
         */
        fun mustNullOrBlank(text: String?, message: String) {
            must(text.isNullOrBlank(), ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustNullOrBlank(value, ::IllegalArgumentException, "The value must null or blank")
         */
        fun <E : Throwable> mustNullOrBlank(text: String?, throwable: Function<String?, E>, message: String) {
            must(text.isNullOrBlank(), throwable, message)
        }

        /**
         * Assertx.mustNullOrBlank(value) { IllegalArgumentException("The value must null or blank") }
         */
        fun <E : Throwable> mustNullOrBlank(text: String?, throwable: Supplier<E>) {
            must(text.isNullOrBlank(), throwable)
        }

        /**
         * val value = Assertx.requireNotEmpty(array, "The value must contain elements")
         */
        fun <T> requireNotEmpty(array: Array<T>?, message: String): Array<T> {
            must(!array.isNullOrEmpty(), ::IllegalArgumentException, message)
            return array!!
        }

        /**
         * val value = Assertx.requireNotEmpty(array, ::IllegalArgumentException, "The value must contain elements")
         */
        fun <T, E : Throwable> requireNotEmpty(array: Array<T>?, throwable: Function<String?, E>, message: String): Array<T> {
            must(!array.isNullOrEmpty(), throwable, message)
            return array!!
        }

        /**
         * val value = Assertx.requireNotEmpty(array)  { IllegalArgumentException("The value must contain elements") }
         */
        fun <T, E : Throwable> requireNotEmpty(array: Array<T>?, throwable: Supplier<E>): Array<T> {
            must(!array.isNullOrEmpty(), throwable)
            return array!!
        }

        /**
         * Assertx.mustNotEmpty(array, "The value must contain elements")
         */
        fun <T> mustNotEmpty(array: Array<T>?, message: String) {
            must(!array.isNullOrEmpty(), ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustNotEmpty(array, ::IllegalArgumentException, "The value must contain elements")
         */
        fun <T, E : Throwable> mustNotEmpty(array: Array<T>?, throwable: Function<String?, E>, message: String) {
            must(!array.isNullOrEmpty(), throwable, message)
        }

        /**
         * Assertx.mustNotEmpty(array) { IllegalArgumentException("The value must contain elements") }
         */
        fun <T, E : Throwable> mustNotEmpty(array: Array<T>?, throwable: Supplier<E>) {
            must(!array.isNullOrEmpty(), throwable)
        }

        /**
         * Assertx.mustNotEmpty(array, "The value must null or empty")
         */
        fun <T> mustNullOrEmpty(array: Array<T>?, message: String) {
            must(array.isNullOrEmpty(), ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustNotEmpty(value, ::IllegalArgumentException, "The value must null or empty")
         */
        fun <T, E : Throwable> mustNullOrEmpty(array: Array<T>?, throwable: Function<String?, E>, message: String) {
            must(array.isNullOrEmpty(), throwable, message)
        }

        /**
         * Assertx.mustNotEmpty(value) { IllegalArgumentException("The value must null or empty") }
         */
        fun <T, E : Throwable> mustNullOrEmpty(array: Array<T>?, throwable: Supplier<E>) {
            must(array.isNullOrEmpty(), throwable)
        }

        /**
         * val value = Assertx.requireNotEmpty(collection, "The value must contain elements")
         */
        fun <T> requireNotEmpty(collection: Collection<T>?, message: String): Collection<T> {
            must(!collection.isNullOrEmpty(), ::IllegalArgumentException, message)
            return collection!!
        }

        /**
         * val value = Assertx.requireNotEmpty(collection, ::IllegalArgumentException, "The value must contain elements")
         */
        fun <T, E : Throwable> requireNotEmpty(collection: Collection<T>?, throwable: Function<String?, E>, message: String): Collection<T> {
            must(!collection.isNullOrEmpty(), throwable, message)
            return collection!!
        }

        /**
         * val value = Assertx.requireNotEmpty(collection)  { IllegalArgumentException("The value must contain elements") }
         */
        fun <T, E : Throwable> requireNotEmpty(collection: Collection<T>?, throwable: Supplier<E>): Collection<T> {
            must(!collection.isNullOrEmpty(), throwable)
            return collection!!
        }

        /**
         * Assertx.mustNotEmpty(collection, "The value must contain elements")
         */
        fun <T> mustNotEmpty(collection: Collection<T>?, message: String) {
            must(!collection.isNullOrEmpty(), ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustNotEmpty(collection, ::IllegalArgumentException, "The value must contain elements")
         */
        fun <T, E : Throwable> mustNotEmpty(collection: Collection<T>?, throwable: Function<String?, E>, message: String) {
            must(!collection.isNullOrEmpty(), throwable, message)
        }

        /**
         * Assertx.mustNotEmpty(collection) { IllegalArgumentException("The value must contain elements") }
         */
        fun <T, E : Throwable> mustNotEmpty(collection: Collection<T>?, throwable: Supplier<E>) {
            must(!collection.isNullOrEmpty(), throwable)
        }

        /**
         * Assertx.mustNotEmpty(collection, "The value must null or empty")
         */
        fun <T> mustNullOrEmpty(collection: Collection<T>?, message: String) {
            must(collection.isNullOrEmpty(), ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustNotEmpty(collection, ::IllegalArgumentException, "The value must null or empty")
         */
        fun <T, E : Throwable> mustNullOrEmpty(collection: Collection<T>?, throwable: Function<String?, E>, message: String) {
            must(collection.isNullOrEmpty(), throwable, message)
        }

        /**
         * Assertx.mustNotEmpty(collection) { IllegalArgumentException("The value must null or empty") }
         */
        fun <T, E : Throwable> mustNullOrEmpty(collection: Collection<T>?, throwable: Supplier<E>) {
            must(collection.isNullOrEmpty(), throwable)
        }

        /**
         * val value = Assertx.requireNotEmpty(map, "The map must contain entries")
         */
        fun <M : Map<K, V>, K, V> requireNotEmpty(map: M?, message: String): M {
            must(!map.isNullOrEmpty(), ::IllegalArgumentException, message)
            return map!!
        }

        /**
         * val value = Assertx.requireNotEmpty(map, ::IllegalArgumentException, "The map must contain entries")
         */
        fun <M : Map<K, V>, K, V, E : Throwable> requireNotEmpty(map: M?, throwable: Function<String?, E>, message: String): M {
            must(!map.isNullOrEmpty(), throwable, message)
            return map!!
        }

        /**
         * val value = Assertx.requireNotEmpty(map)  { IllegalArgumentException("The map must contain entries") }
         */
        fun <M : Map<K, V>, K, V, E : Throwable> requireNotEmpty(map: M?, throwable: Supplier<E>): M {
            must(!map.isNullOrEmpty(), throwable)
            return map!!
        }

        /**
         * Assertx.mustNotEmpty(map, "The map must contain entries")
         */
        fun <M : Map<K, V>, K, V> mustNotEmpty(map: M?, message: String) {
            must(!map.isNullOrEmpty(), ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustNotEmpty(map, ::IllegalArgumentException, "The map must contain entries")
         */
        fun <M : Map<K, V>, K, V, E : Throwable> mustNotEmpty(map: M?, throwable: Function<String?, E>, message: String) {
            must(!map.isNullOrEmpty(), throwable, message)
        }

        /**
         * Assertx.mustNotEmpty(map) { IllegalArgumentException("The map must contain entries") }
         */
        fun <M : Map<K, V>, K, V, E : Throwable> mustNotEmpty(map: M?, throwable: Supplier<E>) {
            must(!map.isNullOrEmpty(), throwable)
        }

        /**
         * Assertx.mustNotEmpty(value, "The value must null or empty")
         */
        fun <M : Map<K, V>, K, V> mustNullOrEmpty(map: M?, message: String) {
            must(map.isNullOrEmpty(), ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustNotEmpty(value, ::IllegalArgumentException, "The value must null or empty")
         */
        fun <M : Map<K, V>, K, V, E : Throwable> mustNullOrEmpty(map: M?, throwable: Function<String?, E>, message: String) {
            must(map.isNullOrEmpty(), throwable, message)
        }

        /**
         * Assertx.mustNotEmpty(value) { IllegalArgumentException("The value must null or empty") }
         */
        fun <M : Map<K, V>, K, V, E : Throwable> mustNullOrEmpty(map: M?, throwable: Supplier<E>) {
            must(map.isNullOrEmpty(), throwable)
        }

        /**
         * val value = Assertx.requireInstanceOf(Number::class.java, value, "Number expected")
         */
        @Suppress("UNCHECKED_CAST")
        fun <T> requireInstanceOf(expectedType: Class<T>, any: Any?, message: String): T {
            mustNotNull(any, message)
            must(expectedType.isInstance(any), ::IllegalArgumentException, message)
            return any as T
        }

        /**
         * val value = Assertx.requireInstanceOf(Number::class.java, value, ::IllegalArgumentException, "Number expected")
         */
        @Suppress("UNCHECKED_CAST")
        fun <T, E : Throwable> requireInstanceOf(expectedType: Class<T>, any: Any?, throwable: Function<String?, E>, message: String): T {
            mustNotNull(any, throwable, message)
            must(expectedType.isInstance(any), throwable, message)
            return any as T
        }

        /**
         * val value = Assertx.requireInstanceOf(Number::class.java, value) { IllegalArgumentException("Number expected") }
         */
        @Suppress("UNCHECKED_CAST")
        fun <T, E : Throwable> requireInstanceOf(expectedType: Class<T>, any: Any?, throwable: Supplier<E>): T {
            mustNotNull(any, throwable)
            must(expectedType.isInstance(any), throwable)
            return any as T
        }

        /**
         * Assertx.mustInstanceOf(Number::class.java, value, "Number expected")
         */
        fun <T> mustInstanceOf(expectedType: Class<T>, any: Any?, message: String) {
            mustNotNull(any, message)
            must(expectedType.isInstance(any), ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustInstanceOf(Number::class.java, value, ::IllegalArgumentException, "Number expected")
         */
        fun <T, E : Throwable> mustInstanceOf(expectedType: Class<T>, any: Any?, throwable: Function<String?, E>, message: String) {
            mustNotNull(any, throwable, message)
            must(expectedType.isInstance(any), throwable, message)
        }

        /**
         * Assertx.mustInstanceOf(Number::class.java, value) { IllegalArgumentException("Number expected") }
         */
        fun <T, E : Throwable> mustInstanceOf(expectedType: Class<T>, any: Any?, throwable: Supplier<E>) {
            mustNotNull(any, throwable)
            must(expectedType.isInstance(any), throwable)
        }

        /**
         * val value = Assertx.requiredAssignableFrom(Number::class.java, type, "Number expected")
         */
        @Suppress("UNCHECKED_CAST")
        fun <T> requiredAssignableFrom(superType: Class<T>, subType: Class<*>?, message: String): Class<T> {
            mustNotNull(subType, message)
            must(superType.isAssignableFrom(subType!!), ::IllegalArgumentException, message)
            return subType as Class<T>
        }

        /**
         * val value = Assertx.requiredAssignableFrom(Number::class.java, type, ::IllegalArgumentException, "Number expected")
         */
        @Suppress("UNCHECKED_CAST")
        fun <T, E : Throwable> requiredAssignableFrom(superType: Class<T>, subType: Class<*>?, throwable: Function<String?, E>, message: String): Class<T> {
            mustNotNull(subType, throwable, message)
            must(superType.isAssignableFrom(subType!!), throwable, message)
            return subType as Class<T>
        }

        /**
         * val value = Assertx.requiredAssignableFrom(Number::class.java, type) { IllegalArgumentException("Number expected") }
         */
        @Suppress("UNCHECKED_CAST")
        fun <T, E : Throwable> requiredAssignableFrom(superType: Class<T>, subType: Class<*>?, throwable: Supplier<E>): Class<T> {
            mustNotNull(subType, throwable)
            must(superType.isAssignableFrom(subType!!), throwable)
            return subType as Class<T>
        }

        /**
         * Assertx.mustAssignableFrom(Number::class.java, type, "Number expected")
         */
        fun mustAssignableFrom(superType: Class<*>, subType: Class<*>?, message: String) {
            must(superType.isAssignableFrom(subType!!), ::IllegalArgumentException, message)
        }

        /**
         * Assertx.mustAssignableFrom(Number::class.java, type, ::IllegalArgumentException, "Number expected")
         */
        fun <E : Throwable> mustAssignableFrom(superType: Class<*>, subType: Class<*>?, throwable: Function<String?, E>, message: String) {
            must(superType.isAssignableFrom(subType!!), throwable, message)
        }

        /**
         * Assertx.mustAssignableFrom(Number::class.java, type) { IllegalArgumentException("Number expected") }
         */
        fun <E : Throwable> mustAssignableFrom(superType: Class<*>, subType: Class<*>?, throwable: Supplier<E>) {
            must(superType.isAssignableFrom(subType!!), throwable)
        }
    }
}