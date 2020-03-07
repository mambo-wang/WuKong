package com.wukong.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wukong.common.model.BaseResult;
import com.wukong.common.model.UserVO;
import com.wukong.service.HelloService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Reference
    private HelloService helloService;

    @RequestMapping(value = "/hello")
    public BaseResult hello() {
        UserVO hello = helloService.sayHello("world");
        return BaseResult.success(hello);
    }

}
