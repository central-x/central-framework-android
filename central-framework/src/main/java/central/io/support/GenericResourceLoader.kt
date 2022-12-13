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

package central.io.support

import central.io.ProtocolResourceLoader
import central.io.Resource
import java.net.URI

/**
 * 标准资源加载器
 *
 * @author Alan Yeh
 * @since 2023/02/13
 */
class GenericResourceLoader : ProtocolResourceLoader {

    private val resourceLoaders = mutableListOf<ProtocolResourceLoader>()

    fun addResourceLoader(resourceLoader: ProtocolResourceLoader) {
        this.resourceLoaders.add(resourceLoader)
    }

    /**
     * 移除资源加载器
     */
    fun removeResourceLoader(resourceLoader: ProtocolResourceLoader) {
        this.resourceLoaders.remove(resourceLoader)
    }

    override fun support(protocol: String): Boolean {
        return resourceLoaders.any { it.support(protocol) }
    }

    override fun getResource(location: URI): Resource {
        for (resourceLoader in this.resourceLoaders) {
            // 只返回指定协议的资源
            if (resourceLoader.support(location)) {
                return resourceLoader.getResource(location)
            }
        }
        return EmptyResource(location)
    }

    override fun getResources(location: URI): List<Resource> {
        val resources = mutableListOf<Resource>()
        for (resourceLoader in this.resourceLoaders) {
            // 只返回指定协议的资源
            if (resourceLoader.support(location)) {
                resourceLoader.getResources(location).forEach {
                    if (it.isExists()) {
                        // 存在的资源才加入返回的列表里
                        resources.add(it)
                    }
                }
            }
        }
        return resources
    }

    override fun getResource(location: String): Resource {
        return this.resourceLoaders.firstOrNull()?.getResource(location) ?: EmptyResource(URI(location))
    }

    override fun getResources(location: String): List<Resource> {
        val resources = mutableListOf<Resource>()
        for (resourceLoader in this.resourceLoaders) {
            resourceLoader.getResources(location).forEach {
                if (it.isExists()) {
                    // 存在的资源才加入返回的列表里
                    resources.add(it)
                }
            }
        }
        return resources
    }
}