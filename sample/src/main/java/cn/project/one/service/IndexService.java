package cn.project.one.service;

import cn.project.one.api.Feign;
import cn.project.one.api.FeignMapping;
import cn.project.one.api.Header;

@Feign(name = "project-one-test")
public interface IndexService {

    @FeignMapping("/say")
    String sayHello(@Header(name = "name") String name);
}
