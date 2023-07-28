package cn.project.one.common.config;

import static cn.project.one.common.config.ConsulProperties.CONSUL;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = CONSUL)
public class ConsulProperties {

    public static final String CONSUL = "project.one.consul";

    private String address;

    private int port = 8500;
}
