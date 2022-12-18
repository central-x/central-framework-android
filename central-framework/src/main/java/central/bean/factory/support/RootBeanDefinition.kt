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

package central.bean.factory.support

import central.bean.factory.FactoryBean
import central.bean.factory.config.*

/**
 * 用于指定 Bean 定义
 *
 * @author Alan Yeh
 * @since 2023/02/10
 */
class RootBeanDefinition(
    override var type: Class<*>,
    override val name: String = (type.getAnnotation(Component::class.java)?.value ?: "").ifEmpty { type.simpleName.replaceFirstChar { it.lowercaseChar() } },
    override var singleton: Boolean = type.getAnnotation(Scope::class.java)?.singleton ?: true,
    override var lazyInit: Boolean = type.getAnnotation(LazyInit::class.java)?.value ?: false,
    override var dependsOn: List<String> = type.getAnnotation(DependsOn::class.java)?.value?.toList() ?: emptyList(),
    override var primary: Boolean = type.getAnnotation(Primary::class.java) != null,
    override var factory: FactoryBean<*> = ConstructorInvokingFactoryBean(type)
) : BeanDefinition {

    override fun toString(): String {
        return "${RootBeanDefinition::class.java.simpleName}(name=${this.name}, type=${this.type.name}, singleton=${this.singleton}, lazy=${this.lazyInit}, dependsOn=${this.dependsOn}, primary=${this.primary})"
    }
}