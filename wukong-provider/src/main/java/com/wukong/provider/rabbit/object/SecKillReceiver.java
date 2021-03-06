package com.wukong.provider.rabbit.object;

import com.rabbitmq.client.Channel;
import com.wukong.common.contants.Constant;
import com.wukong.common.model.SecKillDTO;
import com.wukong.provider.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Random;

@Component
@Slf4j
@RabbitListener(queues = "object")
public class SecKillReceiver {


    @Autowired
    private OrderService orderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitHandler
    public void process(SecKillDTO secKillDTO, Message message, Channel channel) {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("---消费消息---------deliveryTag = {} ,  secKillDTO: {}",deliveryTag ,secKillDTO);
            int num = orderService.createOrder(secKillDTO);

            if(num < 0){
                log.error("订单创建失败");
                //应该使用websocket告知用户失败信息
                stringRedisTemplate.opsForHash().put(Constant.RedisKey.KEY_KILL_RESULT, String.format(Constant.RedisKey.KEY_RESULT_KEY, secKillDTO.getOrderId()), Constant.SecKill.fail);
                //加库存
                stringRedisTemplate.opsForHash().increment(Constant.RedisKey.KEY_STOCK, secKillDTO.getGoods().getId().toString(), 1);
            } else {
                log.info("订单创建成功 ：{}", secKillDTO);
            }
            // 成功确认消息
            channel.basicAck(deliveryTag, true);

        } catch (IOException e) {
            log.error("确认消息时抛出异常 ， e = {}", e.toString());
            // 重新确认
            try {
                Thread.sleep(50);
                channel.basicAck(deliveryTag, true);
            } catch (IOException | InterruptedException e1) {
                log.error("确认消息时抛出异常 ， e = {}", e1.toString());
                // 可以考虑入库
            }

        } catch (Exception e) {
            try {
                log.error(" 订 单 创 建 失 败 {}", e.toString());
                //应该使用websocket告知用户失败信息
                stringRedisTemplate.opsForHash().put(Constant.RedisKey.KEY_KILL_RESULT, String.format(Constant.RedisKey.KEY_RESULT_KEY, secKillDTO.getOrderId()), Constant.SecKill.fail);
                //加库存
                stringRedisTemplate.opsForHash().increment(Constant.RedisKey.KEY_STOCK, secKillDTO.getGoods().getId().toString(), 1);
                // 失败确认
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException e1) {
                log.error("消息失败确认失败 ， e1 = {}", e1.toString());
            }
        }
    }

}
