package cn.project.one.core.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.core.executor.RefreshTimer;

public class ProjectOneRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ProjectOneProperties properties = event.getApplicationContext().getBean(ProjectOneProperties.class);
        new RefreshTimer(properties).run();
    }
}
