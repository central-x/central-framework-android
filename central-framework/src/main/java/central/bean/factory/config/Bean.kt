package central.bean.factory.config

/**
 * 用于标记带有 [Configuration] 注解的类的内部方法，标记该方法返回的对象将被容器管理。
 *
 * 使用方法:
 * ```
 * @Bean
 * fun myBean(): MyBean {
 *     // 实例化，配置并返回对象
 * }
 * ```
 * 使用本注解标记的方法产生的例实，默认为单例。如果需要改变其作用范围，可以通过 [Scope] 注解完成:
 * ```
 * @Bean
 * @Scope(singleton = false)
 * fun myBean(): MyBean {
 *     // 实例化，配置并返回对象
 * }
 * ```
 * 如果需要延迟初始化该实例，可以通过 [LazyInit] 注解完成:
 * ```
 * @Bean
 * @Lazy
 * fun myBean(): MyBean {
 *     // 实例化，配置并返回对象
 * }
 * ```
 *
 * @author Alan Yeh
 * @since 2022/12/21
 * @see Configuration
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Bean(
    /**
     * 标记 Bean 的名称
     */
    val name: String = ""
)
