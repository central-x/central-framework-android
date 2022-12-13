package central.android.bridge.core.annotation

import android.Manifest

/**
 * 标注组件方法在调用时需要的权限
 *
 * @author Alan Yeh
 * @since 2022/12/09
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class BridgePermission(
    /**
     * 权限表列
     *
     * @see Manifest.permission
     */
    val value: Array<String>
)
