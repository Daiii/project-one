package cn.project.one.core.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.project.one.common.instance.Instance;
import cn.project.one.core.registrar.AbstractRegistry;
import cn.project.one.core.service.ServiceList;

/**
 * 刷新节点任务
 * 
 * @since 2023/7/28
 */
public class RefreshServiceTimer implements Runnable {
    private final AbstractRegistry nodeRegistry;

    private static final Object LOCK = new Object();

    @Override
    public void run() {
        synchronized (LOCK) {
            HashMap<String, Instance> services = nodeRegistry.services();
            ServiceList.INSTANCES = services;
            Map<String, List<Instance>> map = new HashMap<>();
            Iterable<Map.Entry<String, Instance>> entries = services.entrySet();
            for (final Map.Entry<String, Instance> pair : entries) {
                final List<Instance> values = map.computeIfAbsent(pair.getValue().getService(), k -> new ArrayList<>());
                values.add(pair.getValue());
            }
            ServiceList.GROUP = map;
        }
    }

    public RefreshServiceTimer(AbstractRegistry nodeRegistry) {
        this.nodeRegistry = nodeRegistry;
    }
}
