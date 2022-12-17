package central.bean.factory

/**
 * BeanFactory 注入
 *
 * @author Alan Yeh
 * @since 2022/12/22
 */
interface BeanFactoryAware : Aware {
    /**
     * BeanFactory 注入
     */
    var beanFactory: BeanFactory
}
