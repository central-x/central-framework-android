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
annotation class Autowired(
    /**
     * 声明该参数是否必要
     */
    val required: Boolean = true
)
