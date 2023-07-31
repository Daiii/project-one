package cn.project.one.springboot;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.core.executor.RefreshServiceExecutor;
import cn.project.one.core.listener.ProjectOneClosedListener;
import cn.project.one.core.listener.ProjectOneRefreshedListener;

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
    public RefreshServiceExecutor refreshService(ProjectOneProperties properties) {
        return new RefreshServiceExecutor(properties);
    }
}