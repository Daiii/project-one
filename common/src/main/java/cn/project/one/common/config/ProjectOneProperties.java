package cn.project.one.common.config;

import static cn.project.one.common.config.ProjectOneProperties.PREFIX;

import org.springframework.boot.context.properties.ConfigurationProperties;

import cn.project.one.common.constants.Registry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = PREFIX)
public class ProjectOneProperties {

    public static final String PREFIX = "project.one";

    public static final String CONSUL = "project.one.consul";

    public static final String NACOS = "project.one.nacos";

    /**
     * 服务名称
     */
    String name;

    /**
     * 服务刷新crontab表达式
     */
    String corn = "*/30 * * * * *";

    /**
     * 心跳crontab表达式
     */
    String beat = "*/30 * * * * *";

    /**
     * 注册中心类型
     */
    Registry registry = Registry.Consul;

    /**
     * consul配置
     */
    private ConsulProperties consul;

    /**
     * nacos配置
     */
    private NacosProperties nacos;
}
