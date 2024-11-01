package cn.project.one.core.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import cn.hutool.core.thread.ThreadUtil;
import cn.project.one.common.Node;
import cn.project.one.common.util.InetUtil;
import cn.project.one.core.executor.RefreshServiceTimer;
import cn.project.one.core.registrar.AbstractServiceRegistry;
import lombok.RequiredArgsConstructor;

/**
 * 监听容器刷新事件
 *
 * @since 2024/7/15
 */
@Component
@RequiredArgsConstructor
public class ProjectOneRefreshedListener {

    private final AbstractServiceRegistry serviceRegistry;

    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${server.port}")
    private Integer serverPort;

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent(ContextRefreshedEvent event) {
        registerNode();
        ThreadUtil.execute(() -> new RefreshServiceTimer(serviceRegistry));
    }

    /**
     * 注册服务
     */
    private void registerNode() {
        String address = InetUtil.getHost();
        String id = InetUtil.getHost();
        Node node = new Node(id, applicationName, address, serverPort);
        serviceRegistry.register(node);
    }
}
