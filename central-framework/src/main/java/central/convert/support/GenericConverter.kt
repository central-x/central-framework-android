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

package central.convert.support

import central.bean.InitializeException
import central.convert.ConfigurableConverter
import central.convert.ConvertException
import central.convert.Converter
import central.convert.TypeConverter
import central.convert.support.impl.UnsupportedConverter
import central.lang.Assertx
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 标准转换服务
 *
 * @author Alan Yeh
 * @since 2023/02/06
 */
class GenericConverter : ConfigurableConverter {
    /**
     * 已注册的类型转换器
     *
     * target class name -> converter
     */
    private val converters = ConcurrentHashMap<String, MutableList<TypeConverter<*>>>()

    /**
     * 已缓存的转换器
     *
     * 因为如果没找到转换器的话，需要依次调用各个 Converter::support 方法来判断是否支持的数据转换，相对来说比较低效。
     * 因此将已知的已匹配的类型保存起来，这样下次就可以直接获取到指定的转换器了
     */
    private val cached = ConcurrentHashMap<MatchedKey, TypeConverter<*>>()

    init {
        // 预注册转换器
        val resources = Thread.currentThread().contextClassLoader?.getResources("META-INF/${Converter::class.java.name}.imports")
        if (resources != null) {
            while (resources.hasMoreElements()) {
                val properties = Properties()
                properties.load(resources.nextElement().openStream())
                val classes = properties.getProperty("imports")
                classes.split(",")
                    // 将 properties 里面指定的类加载出来
                    .map { Class.forName(it) }
                    .onEach { Assertx.mustAssignableFrom(TypeConverter::class.java, it) { InitializeException(it, "") } }
                    .map { it.newInstance() }
                    .forEach { this.register(it as TypeConverter<*>) }
            }
        }
    }

    /**
     * 注册转换器
     *
     * @param converter 数据转换器
     */
    override fun register(converter: TypeConverter<*>) {
        val targetType = Assertx.requireNotNull(this.findTargetType(converter), "Register converter failed: Cannot find interface Converter<?> from '${converter::javaClass.name}'")

        this.converters.computeIfAbsent(targetType.typeName) { mutableListOf() }.add(converter)
        this.cached.clear()
    }

    /**
     * 取消注册
     */
    override fun deregister(converter: TypeConverter<*>) {
        val targetType = this.findTargetType(converter) ?: return

        this.converters.computeIfAbsent(targetType.typeName) { mutableListOf() }.remove(converter)
        this.cached.clear()
    }

    /**
     * 判断是否支持转换数据类型
     *
     * @param source 源类型
     * @param target 目标类型
     */
    override fun support(source: Class<*>, target: Class<*>): Boolean {
        if (source == target) {
            return true
        }

        return this.getConverter(source, target) !is UnsupportedConverter
    }

    /**
     * 转换数据类型
     *
     * @param source 源数据
     * @param target 目标类型
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T> convert(source: Any?, target: Class<T>): T? {
        if (source == null) {
            return null
        }
        if (target.isInstance(source)) {
            // 如果源类型与目标类型一致，则不需要转换
            return source as T
        }

        val converter = this.getConverter(source::class.java, target)
        Assertx.mustTrue(converter !is UnsupportedConverter) { ConvertException(source, target) }

        return converter.convert(source) as T
    }


    private fun findTargetType(converter: TypeConverter<*>): Type? {
        return converter.javaClass.genericInterfaces
            .filterIsInstance<ParameterizedType>()
            .filter { TypeConverter::class.java.typeName == it.rawType.typeName }
            .map { it.actualTypeArguments[0] }
            .map { if (it is ParameterizedType) it.rawType else it }
            .firstOrNull()
    }

    private fun getConverter(source: Class<*>, target: Class<*>): TypeConverter<*> {
        // 查询之前已匹配的记录
        return cached.computeIfAbsent(MatchedKey(this.toObjectType(source), this.toObjectType(target))) { key ->
            // 如果没有找到，则需要重新查找合适的转换器
            this.converters.computeIfAbsent(key.target.typeName) { mutableListOf() }
                // 依次判断转换器是否支持转换源数据类型
                // 找到第一个支持的转换器即可
                .firstOrNull { it.support(key.source) } ?: UnsupportedConverter()
        }
    }

    private fun toObjectType(type: Class<*>): Class<*> {
        return if (type.isPrimitive) {
            when (type.name) {
                "long" -> Long::class.javaObjectType
                "int" -> Int::class.javaObjectType
                "short" -> Short::class.javaObjectType
                "float" -> Float::class.javaObjectType
                "double" -> Double::class.javaObjectType
                "char" -> Char::class.javaObjectType
                "byte" -> Byte::class.javaObjectType
                "boolean" -> Boolean::class.javaObjectType
                else -> throw IllegalArgumentException("未知的原始类型[${type.name}]")
            }
        } else {
            type
        }
    }

    private data class MatchedKey(val source: Class<*>, val target: Class<*>)
}