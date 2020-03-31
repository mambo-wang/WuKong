package com.wukong.provider.rabbit.object;

import com.wukong.common.contants.Constant;
import com.wukong.common.model.SecKillDTO;
import com.wukong.provider.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RabbitListener(queues = "object")
public class SecKillReceiver {


    @Autowired
    private OrderService orderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitHandler
    public void process(SecKillDTO secKillDTO) {

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
    }

}
