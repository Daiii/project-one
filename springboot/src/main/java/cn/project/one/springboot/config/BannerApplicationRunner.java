package cn.project.one.springboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import cn.hutool.core.lang.Console;
import cn.project.one.common.util.InetUtil;

@Component
public class BannerApplicationRunner implements ApplicationRunner {

    @Value("${server.port}")
    private String port;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String serviceAddr = "http://" + InetUtil.getHost() + ":" + port;
        
        Console.log("\n----------------------------------------------------------\n\n\t" +
                            "项目启动成功！\n\t" +
                            "项目地址: \t{} \n\t" +
                            "服务地址: \t{} \n\t" +
                            "\n----------------------------------------------------------",
                    "https://github.com/Daiii/project-one",
                            serviceAddr);
    }
}
