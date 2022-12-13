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

package central.lang.reflect

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 类型引用
 *
 * 由于泛型在使用的过程中，会被类型擦除，因此可能需要比较麻烦才能拿到类型信息
 * 根据 Java 的特性，可以通过继承的方式将泛型固化下来
 *
 * 本类通过声明匿名类的方式，可以将泛型正确地固化下来。
 *
 * @author Alan Yeh
 * @since 2022/12/27
 */
abstract class TypeReference<T>(
    /**
     * 类型
     */
    type: Type? = null
) {

    private val type: Type

    init {
        if (type != null) {
            this.type = type
        } else {
            val superClass = this::class.java.genericSuperclass as ParameterizedType
            this.type = superClass.actualTypeArguments.first()
        }
    }

    companion object {
        /**
         * 根据 Type 创建 TypeReference
         *
         * 也可以使用 {@link Method#getGenericReturnType()} 的值作为参数
         *
         * @param type 类型
         */
        @JvmStatic
        fun <T> of(type: Type): TypeReference<T> {
            return object : TypeReference<T>(type) {}
        }

        /**
         * 根据 Class 创建 TypeReference
         *
         * @param type 类
         */
        @JvmStatic
        fun <T> of(type: Class<T>): TypeReference<T> {
            return object : TypeReference<T>(type) {}
        }

        /**
         * 根据类型创建 TypeReference
         *
         * @param typeName 类名（全限定）
         */
        @JvmStatic
        fun <T> of(typeName: String): TypeReference<T> {
            return object : TypeReference<T>(Class.forName(typeName)) {}
        }

        /**
         * 创建 List 类型
         *
         * @param elementType 元素类型
         */
        @JvmStatic
        fun <T : List<V>, V> ofList(elementType: TypeReference<V>): TypeReference<T> {
            return object : TypeReference<T>(ParameterizedTypeImpl(List::class.java, arrayOf(elementType.type))) {}
        }

        /**
         * 创建 List 类型
         *
         * @param elementType 元素类型
         */
        @JvmStatic
        fun <T : List<V>, V> ofList(elementType: Class<out V>): TypeReference<T> {
            return object : TypeReference<T>(ParameterizedTypeImpl(List::class.java, arrayOf(elementType))) {}
        }

        /**
         * 创建 Map 类型
         *
         * @param keyType Key 类型
         * @param valueType Value 类型
         */
        @JvmStatic
        fun <T : Map<K, V>, K, V> ofMap(keyType: TypeReference<K>, valueType: TypeReference<V>): TypeReference<T> {
            return object : TypeReference<T>(ParameterizedTypeImpl(Map::class.java, arrayOf(keyType.type, valueType.type))) {}
        }

        /**
         * 创建 Map 类型
         *
         * @param keyType Key 类型
         * @param valueType Value 类型
         */
        fun <T : Map<K, V>, K, V> ofMap(keyType: Class<out K>, valueType: TypeReference<V>): TypeReference<T> {
            return object : TypeReference<T>(ParameterizedTypeImpl(Map::class.java, arrayOf(keyType, valueType.type))) {}
        }

        /**
         * 创建 Map 类型
         *
         * @param keyType Key 类型
         * @param valueType Value 类型
         */
        @JvmStatic
        fun <T : Map<K, V>, K, V> ofMap(keyType: Class<out K>, valueType: Class<out V>): TypeReference<T> {
            return object : TypeReference<T>(ParameterizedTypeImpl(Map::class.java, arrayOf(keyType, valueType))) {}
        }
    }

    private class ParameterizedTypeImpl(
        private val ownerType: Type?,
        private val rawType: Type,
        private val actualTypeArguments: Array<Type>
    ) : ParameterizedType {

        constructor(rawType: Type, actualTypeArguments: Array<Type>) : this(null, rawType, actualTypeArguments)

        override fun getActualTypeArguments(): Array<Type> = this.actualTypeArguments

        override fun getRawType(): Type = this.rawType

        override fun getOwnerType(): Type? = this.ownerType
    }


}