package cn.project.one.core.executor;

import cn.project.one.common.Node;
import cn.project.one.common.util.InetUtil;
import cn.project.one.core.registrar.AbstractServiceRegistry;

public class BeatTask implements Runnable {

    private final AbstractServiceRegistry serviceRegistry;

    @Override
    public void run() {
        Node node = new Node(InetUtil.getHost(), "project-one-test", InetUtil.getHost(), 8080);
        serviceRegistry.beat(node);
    }

    public BeatTask(AbstractServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }
}
