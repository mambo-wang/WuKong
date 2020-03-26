package com.wukong.service.kafka;

import com.alibaba.fastjson.JSONObject;
import com.wukong.common.concurrent.WuKongExecutorServices;
import com.wukong.service.repository.LogRepository;
import com.wukong.service.repository.entity.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class KafkaConsumers {

    @Autowired
    @Qualifier("kafkaConsumer")
    private KafkaConsumer<String, String> kafkaConsumer;

    @Autowired
    @Qualifier("secondaryKafkaConsumer")
    private KafkaConsumer<String, String> secondaryKafkaConsumer;

    @Autowired
    private LogRepository logRepository;

    private String charsetName = "UTF-8";

    ExecutorService executors = WuKongExecutorServices.get().getIoBusyService();

    @PostConstruct
    public void receiveWuKongMsg(){
        kafkaConsumer.subscribe(Arrays.asList("wukong"));
        executors.execute(() -> start(kafkaConsumer, "kafkaConsumer"));

        secondaryKafkaConsumer.subscribe(Arrays.asList("wukong"));
        executors.execute(() -> start(secondaryKafkaConsumer, "secondaryKafkaConsumer"));
    }

    public void start(KafkaConsumer consumer, String metadata) {
        try {
            while (true) {
                ConsumerRecords records = consumer.poll(Duration.ofSeconds(2));
                if (records != null && !records.isEmpty()) {
                    log.info("----------------poll msg success from {} ------------------------", metadata);
                    process(records);
                    consumer.commitAsync();
                }
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
