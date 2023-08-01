package cn.project.one.common.config;

import static cn.project.one.common.config.ProjectOneProperties.PREFIX;

import org.springframework.boot.context.properties.ConfigurationProperties;

import cn.project.one.common.Registrar;
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

    String name;

    String corn = "*/10 * * * * *";

    Registrar registrar = Registrar.Consul;

    private ConsulProperties consul;
}
