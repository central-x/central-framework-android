package central.validation

import kotlin.reflect.KClass

/**
 * 校验参数
 *
 * @author Alan Yeh
 * @since 2022/12/09
 */
@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Validated(
    /**
     * 校验分组
     */
    val value: Array<KClass<*>> = []
)
