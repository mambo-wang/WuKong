package com.wukong.provider.rabbit.object;

import com.wukong.common.contants.Constant;
import com.wukong.common.model.SecKillDTO;
import com.wukong.provider.dto.AddScoreDTO;
import com.wukong.provider.service.OrderService;
import com.wukong.provider.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RabbitListener(queues = Constant.RabbitMQ.queueAddScore)
public class AddScoreReceiver {
    @Autowired
    private UserService userService;

    @RabbitHandler
    public void process(AddScoreDTO addScoreDTO) {

        log.info("收到用户增加积分消息 {}", addScoreDTO);
        userService.addScore(addScoreDTO.getUserId(), addScoreDTO.getScore());

    }

}
