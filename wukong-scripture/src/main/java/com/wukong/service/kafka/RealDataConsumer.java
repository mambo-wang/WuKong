package com.wukong.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Executors;

/**
 * 消费kafka数据
 *
 * @author wangbao6
 */
public class RealDataConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RealDataConsumer.class);
    private KafkaConsumer<String, String> consumer;
    private String charsetName = "UTF-8";
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public void init() {
        Properties props = new Properties();
        /**必须的配置， 代表该消费者所属的 consumer group*/
        props.put("group.id", "testGroup");
        props.put("bootstrap.servers", "121.43.191.104:9093");
        props.put("enable.auto.commit", "false");// 自动提交
        // props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "latest");// in("latest","earliest","none"),
        props.put("session.timeout.ms", "30000");//判断consumer是否存活，超过这个时间consumer会被踢出消费者组。 Consumer 端允许下游系统消费一批消息的最大时长
        props.put("request.timeout.ms", "40000");
        props.put("heartbeat.interval.ms", "10000");
        props.put("max.partition.fetch.bytes", "" + 1024 * 10);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList("tttopic"));
        charsetName = "UTF-8";

    }


    @PostConstruct
    public void start() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {

            public void run() {
                init();
                // 鉴权认证
                LOGGER.info("----------------begin receive data------------------------");
                try {
                    while (true) {
                        ConsumerRecords<String, String> records = consumer.poll(100);
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
