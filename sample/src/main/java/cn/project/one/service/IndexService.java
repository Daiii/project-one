package cn.project.one.service;

import cn.project.one.api.Feign;
import cn.project.one.api.Mapping;
import cn.project.one.api.Header;

@Feign(name = "project-one-test")
public interface IndexService {

    @Mapping("/say")
    String sayHello(@Header(name = "name") String name);
}
