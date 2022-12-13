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

package central.io

import java.net.URI

/**
 * 资源加载器
 *
 * @author Alan Yeh
 * @since 2022/12/22
 */
interface ResourceLoader {
    /**
     * 返回指位置的资源。该资源必须是可重复使用的，允许重复调用 [Resource.getInputStream]。
     * 本方法不合返回空的 Resource 对象，开发者可以通过 [Resource.isExists] 方法判断资源是否存在。
     * 如果同一路径下存在多个同名资源，则返回第一个资源。
     *
     * @param location 资源路径，需要协议名
     */
    fun getResource(location: URI): Resource

    /**
     * 返回指定路径的资源。该资源必须是可重复使用的，允许重复调用 [Resource.getInputStream]。
     * 本方法不会返回空的 Resource 对象，开发者可以通过 [Resource.isExists] 方法判断资源是否存在。
     *
     * @param location 资源路径，需要协议名
     */
    fun getResources(location: URI): List<Resource> {
        val resource = this.getResource(location)
        return if (resource.isExists()) {
            listOf(resource)
        } else {
            emptyList()
        }
    }

    /**
     * 返回指定位置的资源。该资源必须是可重复使用的，允许重复调用 [Resource.getInputStream]。
     * 本方法不会返回空的 Resource 对象，开发者可以通过 [Resource.isExists] 方法判断资源是否存在。
     * 如果同一路径下存在多个同名资源，则返回第一个资源。
     *
     * @param location 资源路径，不需协议名，只需要路径名即可
     */
    fun getResource(location: String): Resource

    /**
     * 返回指定路径的资源。该资源必须是可重复使用的，允许重复调用 [Resource.getInputStream]。
     * 本方法不会返回空的 Resource 对象，开发者可以通过 [Resource.isExists] 方法判断资源是否存在。
     *
     * @param location 资源路径，不需协议名，只需要路径名即可
     */
    fun getResources(location: String): List<Resource> {
        val resource = this.getResource(location)
        return if (resource.isExists()) {
            listOf(resource)
        } else {
            emptyList()
        }
    }
}