package central.bean.factory.config

/**
 * 用记标记指定 Bean 的作用范围
 *
 * @author Alan Yeh
 * @since 2022/12/21
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
annotation class Scope(
    /**
     * 标记是否单例
     */
    val singleton: Boolean = true
)
