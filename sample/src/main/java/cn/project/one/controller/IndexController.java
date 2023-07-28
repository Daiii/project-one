package cn.project.one.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.project.one.service.IndexService;

@RestController
public class IndexController {

    @Autowired
    IndexService indexService;

    @RequestMapping(value = "/")
    @ResponseBody
    public String index() {
        return "ok";
    }

    @RequestMapping(value = "/sayHello")
    @ResponseBody
    public String sayHello(@RequestHeader(name = "name", defaultValue = "zhangbo") String name) {
        return indexService.sayHello(name);
    }

    @RequestMapping(value = "/say")
    @ResponseBody
    public String say() {
        return "hello world";
    }
}
