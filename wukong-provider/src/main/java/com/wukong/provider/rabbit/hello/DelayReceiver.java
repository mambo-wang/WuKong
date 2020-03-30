package com.wukong.provider.rabbit.hello;

import com.wukong.common.model.PayDTO;
import com.wukong.common.utils.DateTimeTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "queue.delay.timed")
@Slf4j
public class DelayReceiver {

    @RabbitHandler
    public void process(PayDTO message) {
        //todo 超时未支付回补库存
        log.info("Delay payDTO : {}, time :  {} " , message, DateTimeTool.formatFullDateTime(System.currentTimeMillis()));
    }

}
