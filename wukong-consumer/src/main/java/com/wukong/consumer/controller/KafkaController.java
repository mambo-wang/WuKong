package com.wukong.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @GetMapping("/message/send")
    public Boolean send(@RequestParam(name = "msg") String msg){
        kafkaTemplate.send("tttopic",msg);
        return true;
    }

}