package cn.project.one.core.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import cn.project.one.common.util.InetUtil;
import cn.project.one.core.registrar.AbstractRegistry;

/**
 * 监听关闭容器事件
 * 
 * @since 2023/7/28
 */
public class ProjectOneClosedListener implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    AbstractRegistry nodeRegistry;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        deregisterNode();
    }

    /**
     * 撤销服务
     */
    private void deregisterNode() {
        String id = InetUtil.getHost();
        nodeRegistry.deregister(id);
    }
}
