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

import central.bean.Validatable
import central.bean.factory.BeanConflictException
import central.bean.factory.config.BeanDefinition
import central.bean.factory.config.BeanDefinitionRegistry

/**
 * 标准的 Bean 定义注册中心
 *
 * @author Alan Yeh
 * @since 2023/02/17
 */
class GenericBeanDefinitionRegistry: BeanDefinitionRegistry {
    /**
     * Bean 定义
     */
    private val definitions = mutableMapOf<String, BeanDefinition>()

    override fun getDefinitionNames(): List<String> {
        return definitions.keys.toList()
    }

    override fun registerDefinition(definition: BeanDefinition) {
        // 校验
        (definition as? Validatable)?.validate()

        if (this.definitions.containsKey(definition.name)) {
            throw BeanConflictException("Cannot register bean definition for bean '${definition.name}'")
        }

        this.definitions[definition.name] = definition
    }

    override fun removeDefinition(name: String) {
        this.definitions.remove(name)
    }

    override fun getDefinition(name: String): BeanDefinition? {
        return this.definitions[name]
    }

    override fun getDefinitions(predicate: (BeanDefinition) -> Boolean): List<BeanDefinition> {
        return this.definitions.values.filter(predicate)
    }

    override fun containsDefinition(name: String): Boolean {
        return this.definitions.containsKey(name)
    }
}