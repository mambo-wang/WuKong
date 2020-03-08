package com.wukong.provider.rabbit.topic;

import com.wukong.common.utils.DateTimeTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "topic.messages")
public class TopicReceiver2 {

    @RabbitHandler
    public void process(String message) {
        log.info("Topic Receiver2  : {}, time :  {} " , message, DateTimeTool.formatFullDateTime(System.currentTimeMillis()));
    }

}
