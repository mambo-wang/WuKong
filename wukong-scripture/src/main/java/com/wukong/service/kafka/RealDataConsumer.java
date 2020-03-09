package com.wukong.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Executors;

/**
 * 消费kafka数据
 *
 * @author wangbao6
 */
@Configuration
public class RealDataConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RealDataConsumer.class);
    private String charsetName = "UTF-8";

    @Bean
    public KafkaConsumer<String, String> kafkaConsumer() {
        Properties props = new Properties();
        /**必须的配置， 代表该消费者所属的 consumer group*/
        props.put("group.id", "testGroup");
        props.put("bootstrap.servers", "121.43.191.104:9092");
        props.put("enable.auto.commit", "false");// 自动提交
        // props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "latest");// in("latest","earliest","none"),
        props.put("session.timeout.ms", "30000");//判断consumer是否存活，超过这个时间consumer会被踢出消费者组。 Consumer 端允许下游系统消费一批消息的最大时长
        props.put("request.timeout.ms", "40000");
        props.put("heartbeat.interval.ms", "10000");
//        props.put("max.partition.fetch.bytes", "" + 1024 * 10);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList("wukong"));
        start(consumer);
        return consumer;
    }


    public void start(KafkaConsumer consumer) {

        Executors.newSingleThreadExecutor().submit(() -> {
            // 鉴权认证
            LOGGER.info("----------------begin receive data------------------------");
            try {
                while (true) {
                    ConsumerRecords records = consumer.poll(Duration.ofSeconds(2));
                    process(records);
                    consumer.commitAsync();
                }
            } catch (Throwable e) {
                LOGGER.error("consumer exception", e);
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

                LOGGER.info("receive a message: {}", message);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
