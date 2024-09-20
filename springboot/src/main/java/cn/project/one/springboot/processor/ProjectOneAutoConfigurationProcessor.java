package cn.project.one.springboot.processor;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import cn.project.one.api.annotation.Feign;
import cn.project.one.common.util.BeanUtil;
import cn.project.one.common.util.ClassUtil;
import cn.project.one.core.factory.FeignServiceFactoryBean;
import cn.project.one.core.scanner.InterfaceScanner;

public class ProjectOneAutoConfigurationProcessor
    implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, ResourceLoaderAware {

    private Environment environment;
    private ResourceLoader resourceLoader;
    private Class<?> startClass;
    private String[] scanPackages;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        startClass = ClassUtil.deduceMainApplicationClass();
        scanPackages = getScanPackages();
        registerService(beanDefinitionRegistry);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory)
        throws BeansException {

    }

    /**
     * 注入服务
     *
     * @param registry registry
     */
    private void registerService(BeanDefinitionRegistry registry) {
        DefaultBeanNameGenerator defaultBeanNameGenerator = new DefaultBeanNameGenerator();
        InterfaceScanner scanner = new InterfaceScanner(registry, false, environment, resourceLoader);
        Set<BeanDefinitionHolder> beanDefHolders = scanner.doScan(Feign.class, scanPackages);
        beanDefHolders.forEach(beanDefHolder -> {
            Class<?> feignService = BeanUtil.getClass(beanDefHolder);
            if (!ClassUtil.isInterface(feignService)) {
                throw new RuntimeException("feign service " + feignService.getSimpleName() + " must be interface");
            }

            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(FeignServiceFactoryBean.class);
            builder.addConstructorArgValue(feignService);
            AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
            beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            registry.registerBeanDefinition(defaultBeanNameGenerator.generateBeanName(beanDefinition, registry),
                beanDefinition);
        });
    }

    private void addComponentScanningPackages(Set<String> packages, AnnotationMetadata metadata) {
        AnnotationAttributes attributes =
            AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(ComponentScan.class.getName(), true));
        if (attributes != null) {
            addPackages(packages, attributes.getStringArray("value"));
            addPackages(packages, attributes.getStringArray("basePackages"));
            addClasses(packages, attributes.getStringArray("basePackageClasses"));
        }

        // merge SpringBootApplication 上定义的扫描范围
        attributes =
            AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(SpringBootApplication.class.getName(), true));
        if (attributes != null) {
            addPackages(packages, attributes.getStringArray("scanBasePackages"));
            addClasses(packages, attributes.getStringArray("scanBasePackageClasses"));
        }

        if (packages.isEmpty()) {
            packages.add(ClassUtils.getPackageName(metadata.getClassName()));
        }
    }

    private void addPackages(Set<String> packages, String[] values) {
        if (values != null) {
            Collections.addAll(packages, values);
        }
    }

    private void addClasses(Set<String> packages, String[] values) {
        if (values != null) {
            Arrays.stream(values).forEach(value -> packages.add(ClassUtils.getPackageName(value)));
        }
    }

    protected String[] getScanPackages() {
        AnnotationMetadata metadata = AnnotationMetadata.introspect(startClass);
        Set<String> result = new HashSet<>();
        addComponentScanningPackages(result, metadata);
        return result.toArray(new String[0]);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
