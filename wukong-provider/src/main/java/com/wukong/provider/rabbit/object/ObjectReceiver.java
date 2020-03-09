package com.wukong.provider.rabbit.object;

import com.wukong.common.model.AddScoreDTO;
import com.wukong.common.model.UserVO;
import com.wukong.provider.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RabbitListener(queues = "object")
public class ObjectReceiver {


    @Autowired
    private UserService userService;

    @RabbitHandler
    public void process(AddScoreDTO addScoreDTO) {
        log.info("Receiver object : {}", addScoreDTO);
        userService.addScore(addScoreDTO);
    }

}
