package cn.project.one.core.listener;

import javax.annotation.Resource;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.project.one.common.Node;
import cn.project.one.common.util.InetUtil;
import cn.project.one.core.executor.RefreshServiceTimer;
import cn.project.one.core.registrar.AbstractServiceRegistry;

/**
 * 监听容器刷新事件
 *
 * @since 2024/7/15
 */
@Component
public class ProjectOneRefreshedListener implements EnvironmentAware {

    private Environment environment;
    @Resource
    AbstractServiceRegistry serviceRegistry;

    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshed(ContextRefreshedEvent event) {
        registerNode();
        ThreadUtil.execute(() -> new RefreshServiceTimer(serviceRegistry));
    }

    /**
     * 注册服务
     */
    private void registerNode() {
        String name = environment.getProperty("spring.application.name");
        String address = InetUtil.getHost();
        int port = Convert.toInt(environment.getProperty("server.port"), 8080);
        String id = InetUtil.getHost();
        Node node = new Node(id, name, address, port);
        serviceRegistry.register(node);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
