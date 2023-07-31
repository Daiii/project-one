package cn.project.one.core.factory;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import cn.project.one.core.proxy.ServiceProxy;

/**
 * 代理对象
 * 
 * @since 2023/7/28
 * @param <T> @Target接口类
 */
public class TargetServiceFactoryBean<T> implements FactoryBean<T> {

    @Autowired
    ServiceProxy serviceProxy;

    private final Class<?> targetService;

    public TargetServiceFactoryBean(Class<?> targetService) {
        this.targetService = targetService;
    }

    @Override
    public T getObject() throws Exception {
        return (T)Proxy.newProxyInstance(targetService.getClassLoader(), new Class[] {targetService}, serviceProxy);
    }

    @Override
    public Class<?> getObjectType() {
        return targetService;
    }

    @Override
    public boolean isSingleton() {
        return Boolean.TRUE;
    }
}
