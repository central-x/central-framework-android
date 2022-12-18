package central.bean.factory.config

/**
 * 标记当前的 Bean 依赖
 *
 * @author Alan Yeh
 * @since 2022/12/19
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
annotation class DependsOn(
    vararg val value: String
)
