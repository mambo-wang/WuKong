package com.wukong.provider.rabbit.object;

import com.rabbitmq.client.Channel;
import com.wukong.common.contants.Constant;
import com.wukong.provider.dto.AddScoreDTO;
import com.wukong.provider.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RabbitListener(queues = Constant.RabbitMQ.queueAddScore)
public class AddScoreReceiver {
    @Autowired
    private UserService userService;

    @RabbitHandler
    public void process(AddScoreDTO addScoreDTO, Message message, Channel channel) {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("收到用户增加积分消息 {}", addScoreDTO);
            userService.addScore(addScoreDTO.getUserId(), addScoreDTO.getScore());
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            try {
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException ex) {
                ex.printStackTrace();
                try {
                    channel.basicAck(deliveryTag, true);
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        }

    }

}
