package cn.project.one.core.executor;

import cn.project.one.common.Node;
import cn.project.one.common.util.InetUtil;
import cn.project.one.core.registrar.AbstractRegistry;

public class BeatTask implements Runnable {

    private final AbstractRegistry nodeRegistry;

    @Override
    public void run() {
        Node node = new Node(InetUtil.getHost(), "project-one-test", InetUtil.getHost(), 8080);
        nodeRegistry.beat(node);
    }

    public BeatTask(AbstractRegistry nodeRegistry) {
        this.nodeRegistry = nodeRegistry;
    }
}
