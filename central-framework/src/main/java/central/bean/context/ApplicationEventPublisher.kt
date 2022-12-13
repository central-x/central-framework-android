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

package central.bean.context

/**
 * 应用事件发布器
 *
 * @author Alan Yeh
 * @since 2023/01/31
 */
interface ApplicationEventPublisher {

    /**
     * 发布事件
     *
     * @param event 事件
     */
    fun publishEvent(event: ApplicationEvent)

    /**
     * 添加事件监听器
     *
     * @param listener 事件监听器
     * @see removeApplicationListener
     */
    fun addApplicationListener(listener: ApplicationListener<*>)

    /**
     * 添加事件监听器名称
     *
     * @param listenerBeanName 事件监听器 Bean 名称
     * @see removeApplicationListenerBean
     */
    fun addApplicationListenerBean(listenerBeanName: String)

    /**
     * 移除事件监听器
     *
     * @param listener 事件监听器
     * @see addApplicationListener
     */
    fun removeApplicationListener(listener: ApplicationListener<*>)

    /**
     * 移除事件监听器
     *
     * @param listenerBeanName 事件监听器 Bean 名称
     * @see addApplicationListenerBean
     */
    fun removeApplicationListenerBean(listenerBeanName: String)

    /**
     * 清空所有事件监听器
     */
    fun removeAllListeners()
}