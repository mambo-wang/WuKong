package com.wukong.provider.rabbit.object;

import com.wukong.common.model.UserVO;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "object")
public class ObjectReceiver {

    @RabbitHandler
    public void process(UserVO user) {
        System.out.println("Receiver object : " + user);
    }

}
