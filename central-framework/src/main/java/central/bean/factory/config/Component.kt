package central.bean.factory.config

/**
 * 标记指定类为一个组件，被本注解标记的类会被容器托管
 *
 * @author Alan Yeh
 * @since 2022/12/21
 */
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Component(
    /**
     * 用于指定组件的名称。如果没有指定组件的名称，将通过类名生成对象的组件名
     */
    val value: String = ""
)
