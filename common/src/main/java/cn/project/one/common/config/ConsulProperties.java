package cn.project.one.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = ProjectOneProperties.CONSUL)
public class ConsulProperties {

    private String address = "127.0.0.1";

    private int port = 8500;
}
