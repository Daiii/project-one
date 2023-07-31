package cn.project.one.service;

import cn.project.one.api.annotation.Feign;
import cn.project.one.api.annotation.Mapping;

@Feign(service = "project-one-test")
public interface IndexService {

    @Mapping("/say")
    String sayHello();
}
