package com.wukong.provider.rabbit.object;

import com.wukong.common.contants.Constant;
import com.wukong.common.model.PayDTO;
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
@RabbitListener(queues = "object")
public class ObjectReceiver {


    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitHandler
    public void process(PayDTO payDTO) {

        int num = orderService.createOrder(payDTO);

        if(num < 0){
            System.out.println("订单创建失败");
            return;
        }

        log.info("Receiver object : {}", payDTO);

        //step 1 pay  todo 付款方法
        boolean res = false;
        if(res){
            //step 2 update order state
            orderService.updateState(payDTO, Constant.Order.STAT_PAY);
            //step 3 add score
            userService.addScore(payDTO);

            //真正减库存 todo dubbo调用


        } else {
            //加库存
            stringRedisTemplate.opsForHash().increment(Constant.RedisKey.KEY_STOCK, payDTO.getGoods().getId().toString(), 1);
            //订单状态修改
            orderService.updateState(payDTO, Constant.Order.STAT_CANCEL);
        }

    }

}
