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

/**
 * JSONObject 扩展
 *
 * @author Alan Yeh
 * @since 2023/04/24
 */

/**
 * JSONObject 转 Map
 */
fun JSONObject.toMap(): Map<String, Any?> {
    val result = mutableMapOf<String, Any?>()
    this.forEach { key, value ->
        result[key] = when (value) {
            is JSONObject -> value.toMap()
            is JSONArray -> value.toList()
            else -> value
        }
    }
    return result
}

/**
 * 遍历 JSONObject
 */
fun JSONObject.forEach(action: (String, Any?) -> Unit) {
    this.keys().forEach {
        action(it, this.opt(it))
    }
}

/**
 * 获取指定 Key 的值
 *
 * @param key 键
 * @return 值
 */
fun JSONObject.pick(key: String): Any {
    return this.opt(key)!!
}

/**
 * 获取指定 Key 的值
 *
 * @param key 键
 * @param default 如果值为空时默认值
 * @return 值
 */
fun JSONObject.pick(key: String, default: Any): Any {
    return this.opt(key) ?: default
}

/*
 * 获取指定 Key 的值
 *
 * @param key 键
 * @return 值
 */
fun JSONObject.pickString(key: String): String {
    return this.opt(key)!!.toString()
}

/**
 * 获取指定 Key 的值
 *
 * @param key 键
 * @param default 如果值为空时默认值
 * @return 值
 */
fun JSONObject.pickString(key: String, default: String): String {
    return this.opt(key)?.toString() ?: default
}

/**
 * 获取指定 Key 的值
 *
 * @param key 键
 * @return 值
 */
fun JSONObject.pickBoolean(key: String): Boolean {
    val value = this.opt(key)
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
 * 获取指定 Key 的值
 *
 * @param key 键
 * @param default 如果值为空时默认值
 * @return 值
 */
fun JSONObject.pickBoolean(key: String, default: Boolean): Boolean {
    val value = this.opt(key)
    return if (value == null) default
    else this.pickBoolean(key)
}

/**
 * 获取指定 Key 的值
 *
 * @param key 键
 * @return 值
 */
fun JSONObject.pickNumber(key: String): Number {
    return this.opt(key) as Number
}

/**
 * 获取指定 Key 的值
 *
 * @param key 键
 * @param default 如果值为空时默认值
 * @return 值
 */
fun JSONObject.pickNumber(key: String, default: Number): Number {
    val value = this.opt(key)
    return if (value == null) default else value as Number
}

/**
 * 获取指定的 Key 的值
 *
 * @param key 键
 * @return 值
 */
fun JSONObject.optNumber(key: String): Number? {
    val value = this.opt(key)
    return if (value == null) null else value as Number
}

/**
 * 获取指定 Key 的值
 *
 * @param key 键
 * @return 值
 */
fun JSONObject.pickJSONArray(key: String): JSONArray {
    return this.opt(key) as JSONArray
}

/**
 * 获取指定 Key 的值
 *
 * @param key 键
 * @param default 如果值为空时默认值
 * @return 值
 */
fun JSONObject.pickJSONArray(key: String, default: JSONArray): JSONArray {
    val value = this.opt(key)
    return if (value == null) default else value as JSONArray
}

/**
 * 获取指定 Key 的值
 *
 * @param key 键
 * @return 值
 */
fun JSONObject.pickJSONObject(key: String): JSONObject {
    return this.opt(key) as JSONObject
}

/**
 * 获取指定 Key 的值
 *
 * @param key 键
 * @param default 如果值为空时默认值
 * @return 值
 */
fun JSONObject.pickJSONObject(key: String, default: JSONObject): JSONObject {
    val value = this.opt(key)
    return if (value == null) default else value as JSONObject
}