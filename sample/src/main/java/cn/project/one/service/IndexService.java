package cn.project.one.service;

import cn.project.one.api.Feign;
import cn.project.one.api.Mapping;

@Feign(name = "project-one-test")
public interface IndexService {

    @Mapping("/say")
    String sayHello();
}
