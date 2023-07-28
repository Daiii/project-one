package cn.project.one.api.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Invocation {

    private String serviceName;

    private String requestMapping;

    private String param;
}
