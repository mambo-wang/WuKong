package com.wukong.consumer.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitConfig {

    @Bean
    public Queue helloQueue() {
        return new Queue("hello");
    }

    @Bean
    public Queue neoQueue() {
        return new Queue("neo");
    }

    @Bean
    public Queue objectQueue() {
        return new Queue("object");
    }

    @Bean
    public RabbitTemplate getTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);

        // 消息发送到交换器Exchange后触发回调
        template.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                //  可以进行消息入库操作
                log.info("消息唯一标识 correlationData = {}", correlationData);
                log.info("确认结果 ack = {}", ack);
                log.info("失败原因 cause = {}", cause);
            }
        });

        // 配置这个，下面的ReturnCallback 才会起作用
        template.setMandatory(true);
        // 如果消息从交换器发送到对应队列失败时触发（比如 根据发送消息时指定的routingKey找不到队列时会触发）
        template.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                //  可以进行消息入库操作
                log.info("消息主体 message = {}", message);
                log.info("回复码 replyCode = {}", replyCode);
                log.info("回复描述 replyText = {}", replyText);
                log.info("交换机名字 exchange = {}", exchange);
                log.info("路由键 routingKey = {}", routingKey);
            }
        });

        return template;
    }


}
