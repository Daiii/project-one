package cn.project.one.common.config;

import static cn.project.one.common.config.NacosProperties.NACOS;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = NACOS)
public class NacosProperties {

    public static final String NACOS = "project.one.nacos";

    private String address;

    private int port = 8500;
}
