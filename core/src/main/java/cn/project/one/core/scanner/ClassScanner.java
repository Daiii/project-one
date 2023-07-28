package cn.project.one.core.scanner;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * 注解扫描类
 * <p>
 * 扫描后加入到容器中
 * </p>
 */
public class ClassScanner extends ClassPathBeanDefinitionScanner {

    public ClassScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment,
        ResourceLoader resourceLoader) {
        super(registry, useDefaultFilters, environment, resourceLoader);
    }

    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

    public <T extends Annotation> Set<BeanDefinitionHolder> doScan(Class<T> annotation, String... basePackages) {
        addIncludeFilter(new AnnotationTypeFilter(annotation));
        return super.doScan(basePackages);
    }
}