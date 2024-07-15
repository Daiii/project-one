package cn.project.one.core.listener;

import javax.annotation.Resource;

import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import cn.project.one.common.util.InetUtil;
import cn.project.one.core.registrar.AbstractServiceRegistry;

/**
 * 监听关闭容器事件
 *
 * @since 2024/7/15
 */
@Component
public class ProjectOneClosedListener {

    @Resource
    AbstractServiceRegistry serviceRegistry;

    @EventListener(ContextClosedEvent.class)
    public void contextClosedEvent(ContextClosedEvent event) {
        deregisterNode();
    }

    /**
     * 撤销服务
     */
    private void deregisterNode() {
        String id = InetUtil.getHost();
        serviceRegistry.deregister(id);
    }
}
