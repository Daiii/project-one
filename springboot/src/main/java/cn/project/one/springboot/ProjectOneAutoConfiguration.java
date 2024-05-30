package cn.project.one.springboot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.core.executor.RefreshServiceTask;
import cn.project.one.core.listener.ProjectOneClosedListener;
import cn.project.one.core.listener.ProjectOneRefreshedListener;
import cn.project.one.core.registrar.AbstractServiceRegistry;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "project.one", value = "enable", matchIfMissing = true)
@EnableConfigurationProperties(value = {ProjectOneProperties.class})
public class ProjectOneAutoConfiguration {

    @Bean
    public ProjectOneRefreshedListener projectOneRefreshedListener() {
        return new ProjectOneRefreshedListener();
    }

    @Bean
    public ProjectOneClosedListener projectOneClosedListener() {
        return new ProjectOneClosedListener();
    }

    @Bean
    public RefreshServiceTask refreshServiceTask(ProjectOneProperties properties,
        AbstractServiceRegistry serviceRegistry) {
        return new RefreshServiceTask(properties, serviceRegistry);
    }
}