package cn.project.one.core.consul;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.project.one.api.common.RegisterParam;
import cn.project.one.common.instance.Instance;

/**
 * consul操作类
 * 
 * @since 2023/7/28
 */
public class ConsulClient {

    private static final String REGISTER = "/v1/agent/service/register";

    private static final String SERVICES = "/v1/agent/services";

    private static final String DEREGISTER = "/v1/agent/service/deregister/";

    private static final String CHECKS = "/v1/agent/checks";

    private static final String CHECK_DEREGISTER = "agent/check/deregister/";

    private static final String URL = "%s:%s";

    private static final int SUCCESS = 200;

    /**
     * 注册服务
     * 
     * @param host 注册中心地址
     * @param port 注册中心端口
     * @param param 注册节点信息
     */
    public static void register(String host, int port, RegisterParam param) {
        String url = String.format(URL, host, port) + REGISTER;
        try {
            HttpResponse response =
                HttpUtil.createRequest(Method.PUT, url).body(JSONUtil.toJsonStr(param)).executeAsync();
            if (response.getStatus() != SUCCESS) {
                Console.log(String.format("url : %s register error param : %s", url, param));
            }
        } catch (Exception e) {
            Console.log(e);
        }
    }

    /**
     * 注销服务
     * 
     * @param host 注册中心地址
     * @param port 注册中心端口
     * @param id 节点ID
     */
    public static void deregister(String host, int port, String id) {
        String url = String.format(URL, host, port) + DEREGISTER + id;
        try {
            HttpResponse response = HttpUtil.createRequest(Method.PUT, url).executeAsync();
            if (response.getStatus() != SUCCESS) {
                Console.log(String.format("url : %s deregister error ", url));
            }
        } catch (Exception e) {
            Console.log(e);
        }
    }

    /**
     * 拉取所有服务
     * 
     * @param host 注册中心地址
     * @param port 注册中心端口
     * @return Instance
     */
    public static HashMap<String, Instance> services(String host, int port) {
        String url = String.format(URL, host, port) + SERVICES;
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

    private ConsulClient() {

    }
}
