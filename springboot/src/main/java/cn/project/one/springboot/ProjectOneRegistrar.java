package cn.project.one.springboot;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import cn.hutool.core.util.ObjUtil;
import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.common.util.BeanUtil;
import cn.project.one.core.proxy.ServiceProxy;
import cn.project.one.core.registrar.NodeRegistryFactory;
import cn.project.one.springboot.processor.ProjectOneAutoConfigurationProcessor;

public class ProjectOneRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry,
        BeanNameGenerator importBeanNameGenerator) {
        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry,
            importBeanNameGenerator);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        ProjectOneProperties properties =
            Binder.get(environment).bind(ProjectOneProperties.PREFIX, ProjectOneProperties.class).get();

        // 自动装配
        BeanUtil.registerBeanDefinitionIfNotExists(registry, ProjectOneAutoConfigurationProcessor.class.getName(),
            ProjectOneAutoConfigurationProcessor.class);

        // 代理对象
        BeanUtil.registerBeanDefinitionIfNotExists(registry, ServiceProxy.class.getName(), ServiceProxy.class);

        // 节点注册器
        Class<?> nodeRegistry = NodeRegistryFactory.getNodeRegistry(properties.getRegistry());
        if (ObjUtil.isNull(nodeRegistry)) {
            throw new RuntimeException("node service registry type must be enum.");
        }
        BeanUtil.registerBeanDefinitionIfNotExists(registry, nodeRegistry.getName(), nodeRegistry);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
