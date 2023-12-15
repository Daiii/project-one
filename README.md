# 基于spring boot实现的一个小工程

## 概要信息

* 兼容Spring Boot，服务注册(consul、nacos)，RPC调用(http)。
* 序列化方式：JSON

## 指南

### 目录

* api 注解
* common 通用工具
* core 核心
* sample 演示
* springboot Spring Boot支持

# project-one 快速入门

## 本地开发流程

### 参数配置

```properties
project.one.registry=Consul
project.one.consul.address=127.0.0.1
project.one.consul.port=8500
project.one.cron=*/10 * * * * *
```

> 说明：consul注册地址

### 编写调用对象

```java
import cn.project.one.api.annotation.Feign;

@Feign(service = "project-one-test")
public interface IndexService() {

    @Mapping('/sayHello')
    String sayHello();
}
```

> 说明：@Target必须是接口，参数service是被调用的工程名。