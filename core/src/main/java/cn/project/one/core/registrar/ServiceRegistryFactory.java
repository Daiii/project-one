package cn.project.one.core.registrar;

import cn.project.one.common.constants.Registry;

public class ServiceRegistryFactory {

    public static Class<?> getNodeRegistry(Registry registry) {
        if (registry.equals(Registry.Consul)) {
            return ConsulServiceServiceRegistry.class;
        } else if (registry.equals(Registry.Nacos)) {
            return NacosServiceServiceRegistry.class;
        }
        return ConsulServiceServiceRegistry.class;
    }
}
