package cn.project.one.core.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import cn.hutool.core.convert.Convert;
import cn.project.one.common.Node;
import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.common.util.InetUtil;
import cn.project.one.core.executor.RefreshServiceTimer;
import cn.project.one.core.registrar.AbstractRegistrar;

/**
 * 监听容器刷新事件
 * 
 * @since 2023/7/28
 */
public class ProjectOneRefreshedListener implements ApplicationListener<ContextRefreshedEvent>, EnvironmentAware {

    private Environment environment;

    @Autowired
    AbstractRegistrar nodeRegistrar;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ProjectOneProperties properties = event.getApplicationContext().getBean(ProjectOneProperties.class);
        registerService(properties);
        new RefreshServiceTimer(properties, nodeRegistrar).run();
    }

    /**
     * 注册服务
     */
    private void registerService(ProjectOneProperties properties) {
        String name = environment.getProperty("spring.application.name");
        String address = InetUtil.getHost();
        int port = Convert.toInt(environment.getProperty("server.port"), 8080);
        String id = InetUtil.getHost();
        Node node = new Node(id, name, address, port);
        nodeRegistrar.register(node);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
