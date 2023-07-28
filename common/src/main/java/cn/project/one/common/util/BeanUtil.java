package cn.project.one.common.util;

import java.util.Objects;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.ClassUtils;

public class BeanUtil {

    /**
     * 注入BeanDefinition
     * 
     * @param registry registry
     * @param beanName Class.name
     * @param beanClass Class
     */
    public static void registerBeanDefinitionIfNotExists(BeanDefinitionRegistry registry, String beanName,
        Class<?> beanClass) {
        if (registry.containsBeanDefinition(beanName)) {
            return;
        }

        String[] candidates = registry.getBeanDefinitionNames();

        for (String candidate : candidates) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(candidate);
            if (Objects.equals(beanDefinition.getBeanClassName(), beanClass.getName())) {
                return;
            }
        }

        BeanDefinition annotationProcessor = BeanDefinitionBuilder.genericBeanDefinition(beanClass).getBeanDefinition();
        registry.registerBeanDefinition(beanName, annotationProcessor);
    }

    /**
     * 获取class
     * 
     * @param holder BeanDefinitionHolder
     * @return Class
     */
    public static Class<?> getClass(BeanDefinitionHolder holder) {
        String beanClassName = holder.getBeanDefinition().getBeanClassName();
        try {
            return ClassUtils.forName(beanClassName, null);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("class not found", ex);
        }
    }
}
