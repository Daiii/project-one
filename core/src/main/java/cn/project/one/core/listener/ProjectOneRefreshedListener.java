package cn.project.one.core.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.project.one.common.Node;
import cn.project.one.common.util.InetUtil;
import cn.project.one.core.executor.RefreshServiceTimer;
import cn.project.one.core.registrar.AbstractRegistry;

/**
 * 监听容器刷新事件
 * 
 * @since 2023/7/28
 */
public class ProjectOneRefreshedListener implements ApplicationListener<ContextRefreshedEvent>, EnvironmentAware {

    private Environment environment;

    @Autowired
    AbstractRegistry nodeRegistry;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        registerNode();
        ThreadUtil.execAsync(new RefreshServiceTimer(nodeRegistry));
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
        nodeRegistry.register(node);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
