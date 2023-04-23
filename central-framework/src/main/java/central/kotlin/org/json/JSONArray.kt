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

package central.kotlin.org.json

import org.json.JSONArray
import org.json.JSONObject

/*
 * JSONArray 扩展
 *
 * @author Alan Yeh
 * @since 2023/04/24
 */

/**
 * 遍历 JSONArray
 */
fun JSONArray.forEach(action: (Any?) -> Unit) {
    for (i in (0 until this.length())) {
        action(this.opt(i))
    }
}

/**
 * JSONArray 转 List
 */
fun JSONArray.toList(): List<Any?> {
    val result = mutableListOf<Any?>()
    this.forEach {
        when (it) {
            is JSONObject -> result.add(it.toMap())
            is JSONArray -> result.add(it.toList())
            else -> result.add(it)
        }
    }
    return result
}

/**
 *
 */
fun JSONArray.toIterable(): List<Any?> {
    val result = mutableListOf<Any?>()
    this.forEach { result.add(it) }
    return result
}

/**
 * 获取指定下标的值
 *
 * @param index 下标
 * @return 值
 */
fun JSONArray.pick(index: Int): Any {
    return this.opt(index)!!
}

/**
 * 获取指定下标的值
 *
 * @param index 下标
 * @param default 如果值为空时默认值
 * @return 值
 */
fun JSONArray.pick(index: Int, default: Any): Any {
    return this.opt(index) ?: default
}

/**
 * 获取指定下标的值
 *
 * @param index 下标
 * @return 值
 */
fun JSONArray.pickString(index: Int): String {
    return this.opt(index)!!.toString()
}

/**
 * 获取指定下标的值
 *
 * @param index 下标
 * @param default 如果值为空时默认值
 * @return 值
 */
fun JSONArray.pickString(index: Int, default: String): String {
    return this.opt(index)?.toString() ?: default
}

/**
 * 获取指定下标的值
 *
 * @param index 下标
 * @return 值
 */
fun JSONArray.pickBoolean(index: Int): Boolean {
    val value = this.opt(index)
    return when (value) {
        is Boolean -> value
        is String -> {
            value != "false" && value != "0"
        }

        is Number -> value != 0
        null -> false
        else -> {
            true
        }
    }
}

/**
 * 获取指定下标的值
 *
 * @param index 下标
 * @param default 如果值为空时默认值
 * @return 值
 */
fun JSONArray.pickBoolean(index: Int, default: Boolean): Boolean {
    val value = this.opt(index)
    return if (value == null) default
    else this.pickBoolean(index)
}

/**
 * 获取指定下标的值
 *
 * @param index 下标
 * @return 值
 */
fun JSONArray.pickNumber(index: Int): Number {
    return this.opt(index) as Number
}

/**
 * 获取指定下标的值
 *
 * @param index 下标
 * @param default 如果值为空时默认值
 * @return 值
 */
fun JSONArray.pickNumber(index: Int, default: Number): Number {
    val value = this.opt(index)
    return if (value == null) default else value as Number
}

/**
 * 获取指定下标的值
 *
 * @param index 下标
 * @return 值
 */
fun JSONArray.pickJSONArray(index: Int): JSONArray {
    return this.opt(index) as JSONArray
}

/**
 * 获取指定下标的值
 *
 * @param index 下标
 * @param default 如果值为空时默认值
 * @return 值
 */
fun JSONArray.pickJSONArray(index: Int, default: JSONArray): JSONArray {
    val value = this.opt(index)
    return if (value == null) default else value as JSONArray
}

/**
 * 获取指定下标的值
 *
 * @param index 下标
 * @return 值
 */
fun JSONArray.pickJSONObject(index: Int): JSONObject {
    return this.opt(index) as JSONObject
}

/**
 * 获取指定下标的值
 *
 * @param index 下标
 * @param default 如果值为空时默认值
 * @return 值
 */
fun JSONArray.pickJSONObject(index: Int, default: JSONObject): JSONObject {
    val value = this.opt(index)
    return if (value == null) default else value as JSONObject
}