package com.wukong.service.kafka;

import com.alibaba.fastjson.JSONObject;
import com.wukong.common.concurrent.WuKongExecutorServices;
import com.wukong.service.repository.LogRepository;
import com.wukong.service.repository.entity.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Autowired
    private KafkaConsumerProperties kafkaConsumerProperties;

    private String charsetName = "UTF-8";

    ExecutorService executors = WuKongExecutorServices.get().getIoBusyService();

    @PostConstruct
    public void receiveWuKongMsg(){
        kafkaConsumer.subscribe(Arrays.asList("wukong"));
        executors.execute(() -> start(kafkaConsumer, "kafkaConsumer"));

        secondaryKafkaConsumer.subscribe(Arrays.asList("wukong"));
        executors.execute(() -> start(secondaryKafkaConsumer, "secondaryKafkaConsumer"));
    }

    /**
     * 重设位移
     * 报错：java.lang.IllegalStateException: No current assignment for partition wukong-0
     * 脚本：./kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group testGroup --reset-offsets --by-duration PT10H30M0S --execute --all-topics
     *
     * @param topic
     */
    public void seek(String topic){
        Properties consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerProperties.getGroupId());
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "121.43.191.104:9092");

        try (final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties)) {
            consumer.subscribe(Collections.singleton(topic));
            consumer.poll(0);

            for (PartitionInfo info : consumer.partitionsFor(topic)) {
                TopicPartition tp = new TopicPartition(topic, info.partition());
                // 假设向前跳123条消息
                long targetOffset = consumer.committed(tp).offset() + 123L;
                consumer.seek(tp, targetOffset);
            }
        }
    }

    private void start(KafkaConsumer consumer, String metadata) {
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
