package cn.project.one.core.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import cn.hutool.aop.ProxyUtil;
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
        return ProxyUtil.newProxyInstance(target.getClassLoader(), serviceProxy, target);
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
