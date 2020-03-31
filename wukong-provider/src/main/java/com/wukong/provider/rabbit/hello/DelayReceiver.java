package com.wukong.provider.rabbit.hello;

import com.wukong.common.contants.Constant;
import com.wukong.common.model.SecKillDTO;
import com.wukong.common.utils.DateTimeTool;
import com.wukong.provider.controller.vo.OrderVO;
import com.wukong.provider.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "queue.delay.timed")
@Slf4j
public class DelayReceiver {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitHandler
    public void process(SecKillDTO message) {
        //todo 超时未支付回补库存
        log.info("Delay payDTO : {}, time :  {} " , message, DateTimeTool.formatFullDateTime(System.currentTimeMillis()));

        OrderVO orderVO = orderService.querySecKillResult(message.getOrderId());
        if(orderVO.getStatus() == Constant.Order.STAT_NOT_PAY){
            //加库存
            log.info("超时未支付，回补库存 {}", message);
            stringRedisTemplate.opsForHash().increment(Constant.RedisKey.KEY_STOCK,message.getGoods().getId().toString(), 1);
        }
    }

}
