package com.wukong.consumer.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * 消费kafka数据
 *
 * @author wangbao6
 */
@Component
public class RealDataProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(RealDataProvider.class);
    private KafkaProducer<String, String> producer;
    private String charsetName = "UTF-8";
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public void init() {
        Properties props = new Properties();
        /**必须的配置， 代表该消费者所属的 consumer group*/
        props.put("bootstrap.servers", "121.43.191.104:9093");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(props);
        charsetName = "UTF-8";
    }


    @PostConstruct
    public void start() {
        init();;
        producer.send(new ProducerRecord<>("tttopic", "lfsjadlfjsdlajfldsajfldsf"));
    }

    public void send(String content){
        producer.send(new ProducerRecord<>("tttopic", content));

    }

}
