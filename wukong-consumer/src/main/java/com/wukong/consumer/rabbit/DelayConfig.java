package com.wukong.consumer.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayConfig {


    public static final String EXCHANGE_DELAY="exchange.delay.wukong";
    public static final String QUEUE_DELAY = "queue.delay.wukong";
    public static final String ROUTINGKEY_DELAY = "routing.delay.wukong";



    /**延迟队列配置*/
    @Bean
    public Queue delayProcessQueue(){
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", "topicExchange");
        params.put("x-dead-letter-routing-key", "topic.message");
        return new Queue(QUEUE_DELAY, true, false, true, params);

    }

    @Bean
    public DirectExchange delayExchange(){
        return new DirectExchange(EXCHANGE_DELAY);
    }

    @Bean
    public Binding dlxBinding(){
        return BindingBuilder.bind(delayProcessQueue()).to(delayExchange()).with(ROUTINGKEY_DELAY);
    }


}
