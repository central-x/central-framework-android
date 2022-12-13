package central.bean.factory.config

/**
 * 当出现多个候选 Bean 时，可以通过本注解用于指定哪些 Bean 为主
 * @author Alan Yeh
 * @since 2022/12/19
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
annotation class Primary
