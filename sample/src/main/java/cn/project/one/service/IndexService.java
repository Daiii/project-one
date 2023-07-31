package cn.project.one.service;

import cn.project.one.api.annotation.Target;
import cn.project.one.api.annotation.Mapping;

@Target(name = "project-one-test")
public interface IndexService {

    @Mapping("/say")
    String sayHello();
}
