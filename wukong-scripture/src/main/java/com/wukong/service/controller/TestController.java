package com.wukong.service.controller;

import com.wukong.common.model.BaseResult;
import com.wukong.service.kafka.KafkaConsole;
import org.apache.kafka.clients.admin.TopicDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/test")
public class TestController {


    @Autowired
    private KafkaConsole kafkaConsole;

    @GetMapping("/topic")
    public BaseResult queryTopicDesc(@RequestParam(name = "topic", defaultValue = "wukong")String topic) throws ExecutionException, InterruptedException {

        Map<String, TopicDescription> map =  kafkaConsole.SelectTopicInfo(topic);

        return BaseResult.success(map);
    }

    @GetMapping("/partition")
    public BaseResult increasePartition(@RequestParam(name = "topic", defaultValue = "wukong")String topic,
                                        @RequestParam(name = "number")int number)  {

        kafkaConsole.increaseTopicPartitions(topic, number);

        return BaseResult.success(null);
    }

}
