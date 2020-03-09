package com.wukong.consumer.controller;

import com.wukong.consumer.kafka.RealDataProvider;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private KafkaProducer<String, String> kafkaProducer;

    @GetMapping("/message/send")
    public Boolean send(@RequestParam(name = "msg") String msg){
        kafkaTemplate.send("wukong",msg);
        ProducerRecord<String, String> record = new ProducerRecord<>("wukong", msg);
        kafkaProducer.send(record);
        return true;
    }

}