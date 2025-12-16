package cn.project.one.springboot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.core.executor.RefreshServiceBehavior;
import cn.project.one.core.listener.ProjectOneRefreshedBehavior;
import cn.project.one.core.registrar.AbstractServiceRegistry;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "project.one", value = "enable", matchIfMissing = true)
@EnableConfigurationProperties(value = {ProjectOneProperties.class})
public class ProjectOneAutoConfiguration {

    @Bean
    public ProjectOneRefreshedBehavior projectOneRefreshedListener(AbstractServiceRegistry serviceRegistry) {
        return new ProjectOneRefreshedBehavior(serviceRegistry);
    }

    @Bean
    public RefreshServiceBehavior refreshServiceTask(ProjectOneProperties properties,
                                                     AbstractServiceRegistry serviceRegistry) {
        return new RefreshServiceBehavior(properties, serviceRegistry);
    }
}