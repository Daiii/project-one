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
public class FeignServiceFactoryBean implements FactoryBean<Object> {

    @Autowired
    ServiceProxy serviceProxy;

    private final Class<?> target;

    public FeignServiceFactoryBean(Class<?> target) {
        this.target = target;
    }

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(target.getClassLoader(), new Class[] {target}, serviceProxy);
    }

    @Override
    public Class<?> getObjectType() {
        return target;
    }

    @Override
    public boolean isSingleton() {
        return Boolean.TRUE;
    }
}
