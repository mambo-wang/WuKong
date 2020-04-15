package com.wukong.provider.rabbit.hello;

import com.rabbitmq.client.Channel;
import com.wukong.common.contants.Constant;
import com.wukong.common.model.SecKillDTO;
import com.wukong.common.utils.DateTimeTool;
import com.wukong.provider.controller.vo.OrderVO;
import com.wukong.provider.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RabbitListener(queues = "queue.delay.timed")
@Slf4j
public class DelayReceiver {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitHandler
    public void process(SecKillDTO secKillDTO, Message message, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //todo 超时未支付回补库存
            log.info("Delay payDTO : {}, time :  {} " , secKillDTO, DateTimeTool.formatFullDateTime(System.currentTimeMillis()));

            OrderVO orderVO = orderService.querySecKillResult(secKillDTO.getOrderId());
            if(orderVO.getStatus() == Constant.Order.STAT_NOT_PAY){
                //加库存
                log.info("超时未支付，回补库存 {}", secKillDTO);
                stringRedisTemplate.opsForHash().increment(Constant.RedisKey.KEY_STOCK,secKillDTO.getGoods().getId().toString(), 1);

                orderService.updateState(secKillDTO.getOrderId(), Constant.Order.STAT_TIMEOUT);
            }
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            e.printStackTrace();
            try {
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
