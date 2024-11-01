package cn.project.one.core.listener;

import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import cn.project.one.common.util.InetUtil;
import cn.project.one.core.registrar.AbstractServiceRegistry;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProjectOneClosedListener {

    private final AbstractServiceRegistry serviceRegistry;

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
