package cn.project.one.core.registrar;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.bind.Binder;
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
import cn.project.one.common.instance.Instance;

public class ConsulRegistry extends AbstractRegistry {

    private static final String REGISTER = "/v1/agent/service/register";

    private static final String SERVICES = "/v1/agent/services";

    private static final String DEREGISTER = "/v1/agent/service/deregister/";

    private static final String CHECKS = "/v1/agent/checks";

    private static final String CHECK_DEREGISTER = "agent/check/deregister/";

    private static final String URL = "%s:%s";

    private static final int SUCCESS = 200;

    ProjectOneProperties properties;

    ConsulProperties consulProperties;

    @Override
    public void register(Node node) {
        String url = String.format(URL, consulProperties.getAddress(), consulProperties.getPort()) + REGISTER;
        HttpResponse response = HttpUtil.createRequest(Method.PUT, url).body(JSONUtil.toJsonStr(node)).executeAsync();
        if (response.getStatus() != SUCCESS) {
            Console.log(String.format("url : %s register error param : %s", url, node));
        }
    }

    @Override
    public void deregister(String id) {
        String url = String.format(URL, consulProperties.getAddress(), consulProperties.getPort()) + DEREGISTER + id;
        try {
            HttpResponse response = HttpUtil.createRequest(Method.PUT, url).executeAsync();
            if (response.getStatus() != SUCCESS) {
                Console.log(String.format("url : %s deregister error ", url));
            }
        } catch (Exception e) {
            Console.log(e);
        }
    }

    @Override
    public HashMap<String, Instance> services() {
        String url = String.format(URL, consulProperties.getAddress(), consulProperties.getPort()) + SERVICES;
        HashMap<String, Instance> map = new HashMap<>();
        HttpResponse response = HttpUtil.createRequest(Method.GET, url).executeAsync();
        if (response.getStatus() == 200) {
            String responseBody = response.body();
            JSONObject entries = JSONUtil.parseObj(responseBody);
            for (Map.Entry<String, Object> entry : entries) {
                String str = StrUtil.toString(entry.getValue());
                map.put(entry.getKey(), JSONUtil.toBean(str, Instance.class));
            }
        }
        return map;
    }

    public ConsulRegistry(Environment environment) {
        properties = Binder.get(environment).bind(ProjectOneProperties.PREFIX, ProjectOneProperties.class).get();
        consulProperties = properties.getConsul();
    }
}