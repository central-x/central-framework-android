package central.bean.factory.config

/**
 * 标记指定组件是否延迟初始化
 *
 * @author Alan Yeh
 * @since 2022/12/21
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
annotation class LazyInit(
    /**
     * 是否延迟初始化
     */
    val value: Boolean = true
)
