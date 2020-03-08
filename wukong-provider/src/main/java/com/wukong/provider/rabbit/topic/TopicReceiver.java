package com.wukong.provider.rabbit.topic;

import com.wukong.common.utils.DateTimeTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "topic.message")
@Slf4j
public class TopicReceiver {

    @RabbitHandler
    public void process(String message) {
        log.info("Topic Receiver1  : {}, time :  {} " , message, DateTimeTool.formatFullDateTime(System.currentTimeMillis()));
    }

}
