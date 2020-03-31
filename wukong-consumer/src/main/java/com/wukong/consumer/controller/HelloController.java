package com.wukong.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wukong.common.dubbo.DubboService;
import com.wukong.common.model.BaseResult;
import com.wukong.common.model.PayDTO;
import com.wukong.common.model.UserVO;
import com.wukong.consumer.rabbit.fanout.FanoutSender;
import com.wukong.consumer.rabbit.hello.HelloSender;
import com.wukong.consumer.rabbit.many.NeoSender;
import com.wukong.consumer.rabbit.many.NeoSender2;
import com.wukong.consumer.rabbit.object.ObjectSender;
import com.wukong.consumer.rabbit.topic.TopicSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private FanoutSender fanoutSender;
    @Autowired
    private HelloSender helloSender;
    @Autowired
    private NeoSender neoSender;
    @Autowired
    private NeoSender2 neoSender2;
    @Autowired
    private ObjectSender objectSender;
    @Autowired
    private TopicSender topicSender;

    /**
     * 测试rmq
     * @return
     */
    @GetMapping(value = "/rmq")
    public BaseResult rmq(@RequestParam(name = "seconds")long seconds, @RequestParam(name = "username") String username) {
        fanoutSender.send();
        helloSender.send();
        neoSender.send(1);
        neoSender2.send(2);
        topicSender.send();
        topicSender.send1();
        topicSender.send2();
        return BaseResult.success(null);
    }

    @GetMapping("/fail")
    public BaseResult fail(){
        int i = 1/0;
        return BaseResult.success(null);
    }

}
