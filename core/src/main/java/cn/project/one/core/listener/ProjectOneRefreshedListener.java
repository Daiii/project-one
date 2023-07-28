package cn.project.one.core.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import cn.hutool.core.convert.Convert;
import cn.project.one.api.common.ConsulRegisterParam;
import cn.project.one.common.config.ConsulProperties;
import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.common.util.InetUtil;
import cn.project.one.core.consul.ConsulClient;
import cn.project.one.core.executor.RefreshTimer;

public class ProjectOneRefreshedListener implements ApplicationListener<ContextRefreshedEvent>, EnvironmentAware {

    private Environment environment;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ProjectOneProperties properties = event.getApplicationContext().getBean(ProjectOneProperties.class);
        registerService(properties);
        new RefreshTimer(properties).run();
    }

    /**
     * 注册服务
     */
    private void registerService(ProjectOneProperties properties) {
        String name = environment.getProperty("spring.application.name");
        String address = InetUtil.getHost();
        int port = Convert.toInt(environment.getProperty("server.port"), 8080);
        String id = InetUtil.getHost();
        ConsulRegisterParam consulRegisterParam = new ConsulRegisterParam(id, name, address, port);
        ConsulProperties consul = properties.getConsul();
        ConsulClient.register(consul.getAddress(), consul.getPort(), consulRegisterParam);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
