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

package central.bean

/**
 * Bean Initialize Exception
 *
 * @author Alan Yeh
 * @since 2022/12/07
 */
class InitializeException(message: String, cause: Throwable?) : RuntimeException(message, cause) {
    companion object {
        private const val serialVersionUID: Long = 2236376075122161491L
    }

    constructor(message: String) : this(message, null)

    constructor(beanType: Class<*>, message: String) : this(beanType, message, null)

    constructor(beanType: Class<*>, cause: Throwable) : this(beanType, cause.localizedMessage ?: "", cause)

    constructor(beanType: Class<*>, message: String, cause: Throwable?) : this("Cannot initialize ${beanType.name}" + if (message.isBlank()) "" else ": $message", cause)
}