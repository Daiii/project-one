package cn.project.one.service;

import cn.project.one.api.annotation.Feign;
import cn.project.one.api.annotation.Mapping;

@Feign(name = "project-one-test")
public interface IndexService {

    @Mapping("/say")
    String sayHello();
}
