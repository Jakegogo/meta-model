package com.concur.meta.metadata.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 这个Bean需要被配置在spring的配置中
 * </pre>
 * @author jake
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware, BeanFactoryPostProcessor {

    private static ApplicationContext APPLICATION_CONTEXT;

    private static ConfigurableListableBeanFactory configurableListableBeanFactory;

    public static final ApplicationContext getContext() {
        return APPLICATION_CONTEXT;
    }

    public static ConfigurableListableBeanFactory getConfigurableListableBeanFactory() {
        return configurableListableBeanFactory;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.
     * ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory)
        throws BeansException {
        ApplicationContextUtils.configurableListableBeanFactory = configurableListableBeanFactory;
    }
}
