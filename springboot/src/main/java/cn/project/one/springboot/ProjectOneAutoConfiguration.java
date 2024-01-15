package cn.project.one.springboot;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.core.executor.RefreshServiceTask;
import cn.project.one.core.listener.ProjectOneClosedListener;
import cn.project.one.core.listener.ProjectOneRefreshedListener;
import cn.project.one.core.registrar.AbstractServiceRegistry;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(value = {ProjectOneProperties.class})
public class ProjectOneAutoConfiguration {

    @Bean
    public ProjectOneRefreshedListener fetchService() {
        return new ProjectOneRefreshedListener();
    }

    @Bean
    public ProjectOneClosedListener listener() {
        return new ProjectOneClosedListener();
    }

    @Bean
    public RefreshServiceTask refreshService(ProjectOneProperties properties, AbstractServiceRegistry nodeRegistry) {
        return new RefreshServiceTask(properties, nodeRegistry);
    }
}