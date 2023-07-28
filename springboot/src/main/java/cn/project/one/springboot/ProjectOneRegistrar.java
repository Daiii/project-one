package cn.project.one.springboot;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import cn.project.one.common.util.BeanUtil;
import cn.project.one.core.proxy.ServiceProxy;
import cn.project.one.springboot.processor.ProjectOneAutoConfigurationProcessor;

public class ProjectOneRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry,
        BeanNameGenerator importBeanNameGenerator) {
        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry,
            importBeanNameGenerator);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        // 自动装配
        BeanUtil.registerBeanDefinitionIfNotExists(registry, ProjectOneAutoConfigurationProcessor.class.getName(),
            ProjectOneAutoConfigurationProcessor.class);

        // 代理对象
        BeanUtil.registerBeanDefinitionIfNotExists(registry, ServiceProxy.class.getName(), ServiceProxy.class);
    }
}
