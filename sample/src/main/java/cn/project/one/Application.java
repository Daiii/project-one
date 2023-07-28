package cn.project.one;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cn.hutool.extra.spring.EnableSpringUtil;
import cn.project.one.springboot.annotation.ProjectOne;

@SpringBootApplication
@ProjectOne
@EnableSpringUtil
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
