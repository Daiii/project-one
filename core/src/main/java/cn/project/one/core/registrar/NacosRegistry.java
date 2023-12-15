package cn.project.one.core.registrar;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.project.one.common.Node;
import cn.project.one.common.config.NacosProperties;
import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.common.instance.Instance;
import cn.project.one.common.util.InetUtil;

public class NacosRegistry extends AbstractRegistry implements EnvironmentAware {

    private static final String INSTANCE = "/nacos/v1/ns/instance";

    private static final String LIST = "/nacos/v1/ns/instance/list";

    private static final String BEAT = "/nacos/v1/ns/instance/beat";

    private static final String URL = "%s:%s";

    private static final int SUCCESS = 200;

    private ProjectOneProperties properties;

    private NacosProperties nacosProperties;

    private Environment environment;

    @Override
    public void register(Node node) {
        String url = String.format(URL, nacosProperties.getAddress(), nacosProperties.getPort()) + INSTANCE;
        Map<String, String> formData = new HashMap<>();
        formData.put("serviceName", node.getName());
        formData.put("ip", node.getAddress());
        formData.put("port", String.valueOf(node.getPort()));
        formData.put("ephemeral", "false");
        HttpResponse response = HttpUtil.createPost(url).formStr(formData).executeAsync();
        if (response.getStatus() != SUCCESS) {
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
        if (response.getStatus() != SUCCESS) {
            Console.error(String.format("url : %s deregister error, form data = %s ", url, formData));
        }
    }

    @Override
    public HashMap<String, Instance> services() {
        // TODO
        // 先service/list
        // 再根据返回的名称/instance/list
        String url = String.format(URL, nacosProperties.getAddress(), nacosProperties.getPort()) + LIST;
        HashMap<String, Instance> map = new HashMap<>();
        return map;
    }

    @Override
    public void beat(Node node) {
        String url = String.format(URL, nacosProperties.getAddress(), nacosProperties.getPort()) + BEAT;
        Map<String, String> formData = new HashMap<>();
        formData.put("serviceName", node.getName());
        formData.put("ip", node.getAddress());
        formData.put("port", String.valueOf(node.getPort()));
        formData.put("ephemeral", "false");
        HttpResponse response = HttpUtil.createRequest(Method.PUT, url).formStr(formData).executeAsync();
        if (response.getStatus() != SUCCESS) {
            Console.error(String.format("url : %s beat error param : %s", url, node));
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        properties = Binder.get(environment).bind(ProjectOneProperties.PREFIX, ProjectOneProperties.class).get();
        nacosProperties = properties.getNacos();
    }
}
