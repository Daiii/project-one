package cn.project.one.core.registrar;

import cn.project.one.common.constants.Registry;

public class ServiceRegistryFactory {

    public static Class<? extends AbstractServiceRegistry> getServiceRegistry(Registry registry) {
        if (registry.equals(Registry.Consul)) {
            return ConsulServiceRegistry.class;
        } else if (registry.equals(Registry.Nacos)) {
            return NacosServiceRegistry.class;
        }
        return ConsulServiceRegistry.class;
    }
}
