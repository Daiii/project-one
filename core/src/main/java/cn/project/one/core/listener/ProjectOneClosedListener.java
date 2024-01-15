package cn.project.one.core.listener;

import cn.project.one.common.util.InetUtil;
import cn.project.one.core.registrar.AbstractServiceRegistry;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import javax.annotation.Resource;

/**
 * 监听关闭容器事件
 *
 * @since 2023/7/28
 */
public class ProjectOneClosedListener implements ApplicationListener<ContextClosedEvent> {

    @Resource
    AbstractServiceRegistry nodeRegistry;

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
