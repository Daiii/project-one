package cn.project.one.core.scanner;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * 注解扫描类
 * 
 * @since 2023/7/28
 */
public class InterfaceScanner extends ClassPathBeanDefinitionScanner {

    public InterfaceScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment,
        ResourceLoader resourceLoader) {
        super(registry, useDefaultFilters, environment, resourceLoader);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return (metadata.isIndependent());
    }

    public <T extends Annotation> Set<BeanDefinitionHolder> doScan(Class<T> annotation, String... basePackages) {
        addIncludeFilter(new AnnotationTypeFilter(annotation));
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        beanDefinitionHolders.forEach(h -> getRegistry().removeBeanDefinition(h.getBeanName()));
        return beanDefinitionHolders;
    }
}
