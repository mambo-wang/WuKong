package com.wukong.service.kafka;

import com.alibaba.fastjson.JSONObject;
import com.wukong.service.repository.LogRepository;
import com.wukong.service.repository.entity.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class KafkaConsumers {

    @Autowired
    private KafkaConsumer<String, String> kafkaConsumer;

    @Autowired
    private LogRepository logRepository;

    private String charsetName = "UTF-8";

    @PostConstruct
    public void receiveWuKongMsg(){
        kafkaConsumer.subscribe(Arrays.asList("wukong"));
        start(kafkaConsumer);
    }

    public void start(KafkaConsumer consumer) {

        Executors.newSingleThreadExecutor().submit(() -> {
            // 鉴权认证
            log.info("----------------begin receive data------------------------");
            try {
                while (true) {
                    ConsumerRecords records = consumer.poll(Duration.ofSeconds(2));
                    process(records);
                    consumer.commitAsync();
                }
            } catch (Throwable e) {
                log.error("consumer exception", e);
            } finally {
                try {
                    consumer.commitSync();
                } finally {
                    consumer.close();
                }
            }
        });

    }


    private void process(ConsumerRecords<String, String> records) {
        try {
            for (ConsumerRecord<String, String> record : records) {
                // 1.获取kafkajsong数据
                String message = new String(record.value().getBytes(), charsetName);

                log.info("receive a message: {}", message);
                OperationLog operationLog = JSONObject.parseObject(message, OperationLog.class);
                logRepository.saveLog(operationLog);

            }
        } catch (Exception e) {
            log.error("convert message fail", e.toString());
        }
    }
}
