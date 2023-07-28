package cn.project.one.core.listener;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.env.Environment;

import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.common.util.InetUtil;
import cn.project.one.core.consul.ConsulClient;

public class ProjectOneClosedListener implements ApplicationListener<ContextClosedEvent>, EnvironmentAware {

    private Environment environment;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        ProjectOneProperties properties =
            Binder.get(environment).bind(ProjectOneProperties.PREFIX, ProjectOneProperties.class).get();

        String id = InetUtil.getHost();
        ConsulClient.deregister(properties.getAddress(), properties.getPort(), id);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
