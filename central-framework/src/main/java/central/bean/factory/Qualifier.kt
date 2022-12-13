package central.bean.factory

/**
 * 此注解用于在注入时限定候选 Bean 的名称
 *
 * @author Alan Yeh
 * @since 2022/12/27
 */
@MustBeDocumented
@Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier(
    val value: String = ""
)
