package central.android.bridge

/**
 * 参数
 *
 * @author Alan Yeh
 * @since 2022/12/11
 */
@MustBeDocumented
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class BridgeParam(
    /**
     * 标记该参数是否是必要参数
     */
    val require: Boolean = true
)
