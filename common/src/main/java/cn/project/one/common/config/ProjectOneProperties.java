package cn.project.one.common.config;

import static cn.project.one.common.config.ProjectOneProperties.PREFIX;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = PREFIX)
public class ProjectOneProperties {

    public static final String PREFIX = "project.one";

    String name;

    String address;

    int port = 8500;

    String corn = "*/10 * * * * *";
}
