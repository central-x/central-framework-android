package central.bean.factory

/**
 * 用于标记构造函数、字段、Setter、方法参数，表示由容器来决定该如何注入这些参数
 *
 * @author Alan Yeh
 * @since 2022/12/27
 */
@MustBeDocumented
@Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Value(
    /**
     * 使用占位符如 `${my.app.myProp}` 的方式表示取值
     */
    val value: String,

    /**
     * 是否是必要
     */
    val required: Boolean = true
)
