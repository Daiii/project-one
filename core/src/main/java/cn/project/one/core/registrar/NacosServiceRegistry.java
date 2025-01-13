package cn.project.one.core.registrar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.project.one.common.Node;
import cn.project.one.common.config.NacosProperties;
import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.common.instance.Instance;
import cn.project.one.common.util.InetUtil;

/**
 * nacos
 * 
 * @author zhangbo
 */
public class NacosServiceRegistry extends AbstractServiceRegistry implements EnvironmentAware {

    private static final String INSTANCE = "/nacos/v1/ns/instance";
    private static final String INSTANCE_LIST = "/nacos/v1/ns/instance/list";
    private static final String SERVICE_LIST = "/nacos/v1/ns/service/list";
    private static final String BEAT = "/nacos/v1/ns/instance/beat";
    private static final String URL = "%s:%s";
    private NacosProperties nacosProperties;
    private Environment environment;

    @Override
    public void register(Node node) {
        String url = String.format(URL, nacosProperties.getAddress(), nacosProperties.getPort()) + INSTANCE;
        Map<String, String> formData = new HashMap<>();
        formData.put("serviceName", node.getName());
        formData.put("ip", node.getAddress());
        formData.put("port", StrUtil.toString(node.getPort()));
        formData.put("ephemeral", "false");
        HttpResponse response = HttpUtil.createPost(url).formStr(formData).executeAsync();
        if (response.getStatus() != HttpStatus.OK.value()) {
            Console.error(String.format("url : %s register error param : %s", url, node));
        }
    }

    @Override
    public void deregister(String id) {
        String url = String.format(URL, nacosProperties.getAddress(), nacosProperties.getPort()) + INSTANCE;
        Map<String, String> formData = new HashMap<>();
        formData.put("serviceName", environment.getProperty("spring.application.name"));
        formData.put("ip", InetUtil.getHost());
        formData.put("port", environment.getProperty("server.port"));
        formData.put("ephemeral", "false");
        HttpResponse response = HttpUtil.createRequest(Method.DELETE, url).formStr(formData).executeAsync();
        if (response.getStatus() != HttpStatus.OK.value()) {
            Console.error(String.format("url : %s deregister error, form data = %s ", url, formData));
        }
    }

    @Override
    public HashMap<String, Instance> services() {
        HashMap<String, Instance> map = new HashMap<>();
        String serviceListUrl =
            String.format(URL, nacosProperties.getAddress(), nacosProperties.getPort()) + SERVICE_LIST;
        String instanceListUrl =
            String.format(URL, nacosProperties.getAddress(), nacosProperties.getPort()) + INSTANCE_LIST;
        Map<String, String> params = new HashMap<>();
        params.put("pageNo", "1");
        params.put("pageSize", "10");
        HttpResponse listServiceResp =
            HttpUtil.createRequest(Method.GET, serviceListUrl).formStr(params).executeAsync();
        if (listServiceResp.getStatus() == HttpStatus.OK.value()) {
            JSONObject jsonObj = JSONUtil.parseObj(listServiceResp.body());
            Integer count = jsonObj.getInt("count");
            List<String> doms = JSONUtil.toList(jsonObj.getJSONArray("doms"), String.class);
            for (String serviceName : doms) {
                HttpResponse instanceResp =
                    HttpUtil.createRequest(Method.GET, instanceListUrl + "?serviceName=" + serviceName).executeAsync();
                if (instanceResp.getStatus() == HttpStatus.OK.value()) {
                    JSONObject instance = JSONUtil.parseObj(instanceResp.body());
                    JSONArray hosts = instance.getJSONArray("hosts");
                    for (Object item : hosts) {
                        JSONObject host = JSONUtil.parseObj(item);
                        String address = host.getStr("ip");
                        Integer port = host.getInt("port");
                        Instance info = new Instance();
                        info.setID(address);
                        info.setAddress(address);
                        info.setService(serviceName);
                        info.setPort(port);
                        map.put(host.getStr("ip"), info);
                    }
                }
            }
        }
        return map;
    }

    @Override
    public void beat(Node node) {
        String url = String.format(URL, nacosProperties.getAddress(), nacosProperties.getPort()) + BEAT;
        Map<String, String> formData = new HashMap<>();
        formData.put("serviceName", node.getName());
        formData.put("ip", node.getAddress());
        formData.put("port", StrUtil.toString(node.getPort()));
        formData.put("ephemeral", "false");
        JSONObject beat = new JSONObject();
        beat.putOpt("timestamp", DateUtil.current());
        HttpResponse response = HttpUtil.createRequest(Method.PUT, url + "?" + MapUtil.join(formData, "&", "="))
            .body(beat.toJSONString(0)).executeAsync();
        if (response.getStatus() != HttpStatus.OK.value()) {
            Console.error(String.format("url : %s beat error param : %s", url, node));
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        ProjectOneProperties properties =
            Binder.get(environment).bind(ProjectOneProperties.PREFIX, ProjectOneProperties.class).get();
        nacosProperties = properties.getNacos();
    }
}
