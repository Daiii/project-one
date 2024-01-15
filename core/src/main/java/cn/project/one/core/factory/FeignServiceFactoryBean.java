package cn.project.one.core.factory;

import javax.annotation.Resource;

import org.springframework.beans.factory.FactoryBean;

import cn.hutool.aop.ProxyUtil;
import cn.project.one.core.proxy.ServiceProxy;

/**
 * 代理对象
 *
 * @since 2023/7/28
 */
public class FeignServiceFactoryBean implements FactoryBean<Object> {

    @Resource
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
