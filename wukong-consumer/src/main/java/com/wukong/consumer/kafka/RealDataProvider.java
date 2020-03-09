package com.wukong.consumer.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;

/**
 * 消费kafka数据
 *
 * @author wangbao6
 */
@Configuration
public class RealDataProvider {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaProducer<String, String> kafkaProducer() {
        Properties props = new Properties();
        /**必须的配置， 代表该消费者所属的 consumer group*/
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("client.id", "ctm-producer-client-id-1");
        // 0：这意味着生产者producer不等待来自broker同步完成的确认继续发送下一条（批）消息。此选项提供最低的延迟但最弱的耐久性保证（当服务器发生故障时某些数据会丢失，如leader已死，但producer并不知情，发出去的信息broker就收不到）。
        // 1：这意味着producer在leader已成功收到的数据并得到确认后发送下一条message。此选项提供了更好的耐久性为客户等待服务器确认请求成功（被写入死亡leader但尚未复制将失去了唯一的消息）。
        // -1：这意味着producer在follower副本确认接收到数据后才算一次发送完成。
        // 此选项提供最好的耐久性，我们保证没有信息将丢失，只要至少一个同步副本保持存活。
        props.put("request.required.acks", "0");
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
        return producer;
    }

}
