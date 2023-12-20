package cn.project.one.core.registrar;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.project.one.common.Node;
import cn.project.one.common.config.ConsulProperties;
import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.common.constants.ResultCodeEnum;
import cn.project.one.common.instance.Instance;

public class ConsulRegistry extends AbstractRegistry implements EnvironmentAware {

    private static final String REGISTER = "/v1/agent/service/register";

    private static final String SERVICES = "/v1/agent/services";

    private static final String DEREGISTER = "/v1/agent/service/deregister/";

    private static final String CHECKS = "/v1/agent/checks";

    private static final String CHECK_DEREGISTER = "/v1/agent/check/deregister/";

    private static final String URL = "%s:%s";

    ProjectOneProperties properties;

    ConsulProperties consulProperties;

    Environment environment;

    @Override
    public void register(Node node) {
        String url = String.format(URL, consulProperties.getAddress(), consulProperties.getPort()) + REGISTER;
        HttpResponse response = HttpUtil.createRequest(Method.PUT, url).body(JSONUtil.toJsonStr(node)).executeAsync();
        if (response.getStatus() != ResultCodeEnum.SUCCESS.getCode()) {
            Console.error(String.format("url : %s register error param : %s", url, node));
        }
    }

    @Override
    public void deregister(String id) {
        String url = String.format(URL, consulProperties.getAddress(), consulProperties.getPort()) + DEREGISTER + id;
        HttpResponse response = HttpUtil.createRequest(Method.PUT, url).executeAsync();
        if (response.getStatus() != ResultCodeEnum.SUCCESS.getCode()) {
            Console.error(String.format("url : %s deregister error ", url));
        }
    }

    @Override
    public HashMap<String, Instance> services() {
        String url = String.format(URL, consulProperties.getAddress(), consulProperties.getPort()) + SERVICES;
        HashMap<String, Instance> map = new HashMap<>();
        HttpResponse response = HttpUtil.createRequest(Method.GET, url).executeAsync();
        if (response.getStatus() == ResultCodeEnum.SUCCESS.getCode()) {
            String responseBody = response.body();
            JSONObject entries = JSONUtil.parseObj(responseBody);
            for (Map.Entry<String, Object> entry : entries) {
                String str = StrUtil.toString(entry.getValue());
                map.put(entry.getKey(), JSONUtil.toBean(str, Instance.class));
            }
        }
        return map;
    }

    @Override
    public void beat(Node node) {
        // TODO
    }

    @PostConstruct
    public void init() {
        properties = Binder.get(environment).bind(ProjectOneProperties.PREFIX, ProjectOneProperties.class).get();
        consulProperties = properties.getConsul();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
