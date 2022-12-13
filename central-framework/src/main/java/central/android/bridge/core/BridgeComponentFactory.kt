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

package central.android.bridge.core

import android.app.Application
import central.android.Assetsx
import central.android.bridge.BridgeComponent
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.net.URI

/**
 * 桥接组件实例工厂
 *
 * @author Alan Yeh
 * @since 2022/12/20
 */
class BridgeComponentFactory {
    /**
     * 根据组件名称获取组件
     */
    fun getComponent(name: String): BridgeComponent? {
        return this.instances[name]
    }

    /**
     * 组件实例
     */
    private val instances = mutableMapOf<String, BridgeComponent>()

    /**
     * Context，用于获取资源
     */
    private lateinit var application: WeakReference<Application>

    /**
     * 用于标记录前组件工厂是否已经初始化了
     */
    private var initialized: Boolean = false

    /**
     * 组件初始化选项
     */
    private var options = JSONObject()

    /**
     * 初始化
     */
    fun initialize(application: Application) {
        if (initialized) {
            return
        }
        this.application = WeakReference(application)
        // 解析 manifest
        val manifest = Assetsx.getAsJSONObject(application, URI("res://manifest.json"))

        this.initConfig(application, manifest ?: JSONObject())
        this.initComponent(application)
    }


    /**
     * 初始化组件的配置信息
     */
    private fun initConfig(application: Application, manifest: JSONObject) {
        val internal = Assetsx.getAsJSONObject(application, URI("res://central/android/bridge/bridge.json")) ?: throw IllegalStateException("初始化错语：没有找到 res://central/android/bridge/bridge.json 文件")

        this.options = mergeConfig(internal.getJSONObject("components"), manifest.optJSONObject("components"))
    }

    /**
     * 初始化组件
     */
    private fun initComponent(application: Application) {
        this.options.keys().forEach { name ->
            val configuration = Assetsx.getAsJSONObject(application, URI("res://central/android/bridge/${name}.json"))
            if (configuration != null) {
                // 如果这个组件是单例，并且需要提前初始化，则开始初始化
                if (configuration.optBoolean("singleton", false) && configuration.optBoolean("instantiate", false)) {
                    val componentType = Class.forName(configuration.getString("class"))
                    val component = componentType.newInstance() as BridgeComponent
                    component.onCreate(application, this.options.optJSONObject(name) ?: JSONObject())

                    val identifier = configuration.optString("identifier") ?: throw IllegalStateException("配置文件错误: 缺失 identifier 属性")
                    this.instances[identifier] = component
                }
            }
        }
    }

    /**
     * 并合两个配置信息
     *
     * - 如果 source 里面有的配置，target 中没有，则保留 source 的配置
     * - 如果 source 里面没有的配置，target 中有，则保留 target 的配置
     * - 如果 source 里面有的配置，target 中也有，则保留 target 的配置
     */
    private fun mergeConfig(source: JSONObject, target: JSONObject?): JSONObject {
        if (target == null) {
            return JSONObject(source.toString())
        }

        val result = JSONObject()
        // 记录未处理的
        val remain = JSONObject(source.toString())

        // 处理合并
        target.keys().forEach { component ->
            if (source.has(component)) {
                // 如果两个都有，则合并
                val merged = resolveConflictConfig(source.optJSONObject(component), target.optJSONObject(component))

                result.put(component, merged)
                remain.remove(component)
            } else {
                result.put(component, target.optJSONObject(component))
            }
        }

        // 剩下的直接合并到配置中
        remain.keys().forEach {
            result.put(it, remain.opt(it))
        }

        return result
    }

    /**
     * 解决冲突
     *
     * 使用 target 的属性覆盖 source 的属性
     */
    private fun resolveConflictConfig(source: JSONObject?, target: JSONObject?): JSONObject {
        if (source == null) {
            return if (target != null) {
                JSONObject(target.toString())
            } else {
                JSONObject()
            }
        }

        val result = JSONObject(source.toString())
        if (target == null) {
            return result
        }

        target.keys().forEach { key ->
            var value = target.opt(key)
            if (value is JSONObject) {
                value = resolveConflictConfig(source.optJSONObject(key), value)
            }

            result.put(key, value)
        }

        return result
    }

}