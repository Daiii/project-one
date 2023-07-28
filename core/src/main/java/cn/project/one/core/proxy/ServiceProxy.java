package cn.project.one.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.springframework.core.DefaultParameterNameDiscoverer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.project.one.api.Feign;
import cn.project.one.api.Mapping;
import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.common.instance.Instance;
import cn.project.one.core.loadbalance.RandomBalance;
import cn.project.one.core.service.Instances;

/**
 * 代理执行类
 */
public class ServiceProxy implements InvocationHandler {

    private DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Feign feign = method.getDeclaringClass().getAnnotation(Feign.class);
        Mapping mapping = method.getAnnotation(Mapping.class);
        Instance instance = RandomBalance.get(Instances.getGroup(feign.name()));
        String url = instance.getAddress() + ":" + instance.getPort();
        HttpRequest request = HttpUtil.createRequest(mapping.method(), url + mapping.value());
        HashMap<String, String> headers = new HashMap<>();
        String body = null;

        if (args != null) {
            String[] parameterNames = discoverer.getParameterNames(method);
        }

        body = request.addHeaders(headers).executeAsync().body();

        return body;
    }

    public ServiceProxy(ProjectOneProperties properties) {}
}
