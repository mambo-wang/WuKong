package com.wukong.provider.rabbit;

import com.wukong.common.contants.Constant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    @Bean
    public Queue objectQueue() {
        return new Queue(Constant.RabbitMQ.queueAddScore);
    }


}
