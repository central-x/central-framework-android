package central.bean.factory.config

/**
 * 标记当前类为配置类
 *
 * 配置类包含一个或多个带有 [Bean] 注解的方法，这些方法产生的实例将会生成相当定义并保存到容器中
 *
 * 使用方法:
 * ```
 * @Configuration
 * class ApplicationConfiguration {
 *     @Bean
 *     fun myBean(): MyBean {
 *         // 实始化，配置并返回 bean
 *     }
 * }
 * ```
 *
 * @author Alan Yeh
 * @since 2022/12/21
 * @see Bean
 */
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Configuration
