package central.android.bridge.core.annotation

/**
 * 标注组件需要暴露的 Getter 方法
 *
 * @author Alan Yeh
 * @since 2022/12/09
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class BridgeGetter()
