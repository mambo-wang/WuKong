package com.wukong.consumer.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayConfig {


    public static final String EXCHANGE_DELAY="exchange.delay.wukong";
    public static final String QUEUE_DELAY = "queue.delay.wukong";
    public static final String ROUTINGKEY_DELAY = "routing.delay.wukong";

    public static final String ROUTINGKEY_DELAY_TIMED = "routing.delay.timed";
    public static final String QUEUE_DELAY_TIMED = "queue.delay.timed";
    public static final String EXCHANGE_DELAY_TIMED = "exchange.delay.timed";

    /**延迟队列配置*/
    @Bean
    public Queue delayProcessQueue(){
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", EXCHANGE_DELAY_TIMED);
        params.put("x-dead-letter-routing-key", ROUTINGKEY_DELAY_TIMED);
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

    @Bean
    public Queue queueDelayTimed() {
        return new Queue(QUEUE_DELAY_TIMED);
    }
    @Bean
    TopicExchange exchangeDelayTimed() {
        return new TopicExchange(EXCHANGE_DELAY_TIMED);
    }
    @Bean
    Binding bingDelayTimed(Queue queueDelayTimed, TopicExchange exchangeDelayTimed) {
        return BindingBuilder.bind(queueDelayTimed).to(exchangeDelayTimed).with(ROUTINGKEY_DELAY_TIMED);
    }
}
