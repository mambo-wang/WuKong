package com.wukong.consumer.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

@Slf4j
public class KafkaCallback implements Callback {
    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {

        if(metadata != null){
            log.info("kafka 发送消息成功！{}", metadata.toString());
        } else {
            log.error("kafka消息发送失败！ cause {}", exception.toString());
        }
    }
}
