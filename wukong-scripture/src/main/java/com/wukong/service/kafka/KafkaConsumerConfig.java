package com.wukong.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(KafkaConsumerProperties.class)
public class KafkaConsumerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerConfig.class);
    private String charsetName = "UTF-8";

    @Autowired
    private KafkaConsumerProperties kafkaConsumerProperties;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaConsumer<String, String> kafkaConsumer() {
        Properties props = new Properties();
        /**必须的配置， 代表该消费者所属的 consumer group*/
        props.put("group.id", "testGroup");
        props.put("bootstrap.servers", bootstrapServers);
        props.put("enable.auto.commit", kafkaConsumerProperties.getEnableAutoCommit());// 自动提交
         props.put("auto.commit.interval.ms", kafkaConsumerProperties.getAutoCommitInterval());
        props.put("auto.offset.reset", kafkaConsumerProperties.getAutoOffsetReset());// in("latest","earliest","none"),
        props.put("key.deserializer", kafkaConsumerProperties.getKeyDeserializer());
        props.put("value.deserializer", kafkaConsumerProperties.getValueDeserializer());
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
