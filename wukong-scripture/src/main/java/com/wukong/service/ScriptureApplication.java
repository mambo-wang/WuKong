package com.wukong.service;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubboConfiguration
@SpringBootApplication
public class ScriptureApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScriptureApplication.class, args);
    }
}
