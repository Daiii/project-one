package cn.project.one.core.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.project.one.common.config.ConsulProperties;
import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.common.instance.Instance;
import cn.project.one.core.consul.ConsulClient;
import cn.project.one.core.service.Instances;

public class RefreshTimer implements Runnable {

    private final ProjectOneProperties properties;

    private static final Object LOCK = new Object();

    @Override
    public void run() {
        ConsulProperties consul = properties.getConsul();
        HashMap<String, Instance> services = ConsulClient.services(consul.getAddress(), consul.getPort());
        Instances.INSTANCES = services;
        Map<String, List<Instance>> map = new HashMap<>();
        Iterable<Map.Entry<String, Instance>> entries = services.entrySet();
        for (final Map.Entry<String, Instance> pair : entries) {
            final List<Instance> values = map.computeIfAbsent(pair.getValue().getService(), k -> new ArrayList<>());
            values.add(pair.getValue());
        }
        Instances.GROUP = map;
    }

    public RefreshTimer(ProjectOneProperties properties) {
        this.properties = properties;
    }
}
