package cn.project.one.core.registrar;

import cn.project.one.common.constants.Registry;

public class NodeRegistryFactory {

    public static Class<?> getNodeRegistry(Registry registry) {
        if (registry.equals(Registry.Consul)) {
            return ConsulRegistry.class;
        } else if (registry.equals(Registry.Nacos)) {
            return NacosRegistry.class;
        }
        return null;
    }
}
