package central.bean.factory.config

import kotlin.reflect.KClass

/**
 * 引入指定类
 *
 * @author Alan Yeh
 * @since 2022/12/21
 * @see Configuration
 * @see Component
 */
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Import(
    /**
     * 用于引入指定的类，包括 [Configuration] 、[Component]或常规的组件类
     */
    vararg val value: KClass<*>
)
