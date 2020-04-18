package com.wukong.provider.rabbit.object;

import com.rabbitmq.client.Channel;
import com.wukong.common.contants.Constant;
import com.wukong.common.exception.BusinessException;
import com.wukong.provider.dto.AddScoreDTO;
import com.wukong.provider.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RabbitListener(queues = Constant.RabbitMQ.queueAddScore)
public class AddScoreReceiver {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitHandler
    public void process(AddScoreDTO addScoreDTO, Message message, Channel channel) {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String id = message.getMessageProperties().getCorrelationId();
            if(redisTemplate.hasKey(Constant.RedisKey.KEY_IDEMPOTENT + ":" + id)){
                log.error("消息已经消费过了，不再处理");
                channel.basicAck(deliveryTag, true);
                return;
            }
            log.info("收到用户增加积分消息 {}", addScoreDTO);
            userService.addScore(addScoreDTO.getUserId(), addScoreDTO.getScore());
            //冪等性
            redisTemplate.opsForValue().set(Constant.RedisKey.KEY_IDEMPOTENT + ":" + id, id, 1, TimeUnit.DAYS);

            if(Math.random() > 0.1){
                throw new BusinessException("200", "幂等性测试");
            }
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            try {
                log.error("消息消费失败 {}", e.toString());
                channel.basicNack(deliveryTag, false, true);
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
