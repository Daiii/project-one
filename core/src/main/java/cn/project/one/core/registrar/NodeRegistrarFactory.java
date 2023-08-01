package cn.project.one.core.registrar;

import cn.project.one.common.Registrar;

public class NodeRegistrarFactory {

    public static Class<?> getNodeRegistrar(Registrar registrar) {
        if (registrar.equals(Registrar.Consul)) {
            return ConsulRegistrar.class;
        }
        return null;
    }
}
