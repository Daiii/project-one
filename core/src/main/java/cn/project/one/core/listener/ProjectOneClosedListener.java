package cn.project.one.core.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.env.Environment;

import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.common.util.InetUtil;
import cn.project.one.core.registrar.AbstractRegistry;

/**
 * 监听关闭容器事件
 * 
 * @since 2023/7/28
 */
public class ProjectOneClosedListener implements ApplicationListener<ContextClosedEvent>, EnvironmentAware {

    private Environment environment;

    @Autowired
    AbstractRegistry nodeRegistry;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        ProjectOneProperties properties =
            Binder.get(environment).bind(ProjectOneProperties.PREFIX, ProjectOneProperties.class).get();

        String id = InetUtil.getHost();
        nodeRegistry.deregister(id);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
