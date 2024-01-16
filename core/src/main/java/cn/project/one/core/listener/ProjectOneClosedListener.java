package cn.project.one.core.listener;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import cn.project.one.common.util.InetUtil;
import cn.project.one.core.registrar.AbstractServiceRegistry;

/**
 * 监听关闭容器事件
 *
 * @since 2023/7/28
 */
public class ProjectOneClosedListener implements ApplicationListener<ContextClosedEvent> {

    @Resource
    AbstractServiceRegistry serviceRegistry;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
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
