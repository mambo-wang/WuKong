package com.wukong.provider.rabbit.object;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wukong.common.contants.Constant;
import com.wukong.common.dubbo.DubboStockService;
import com.wukong.common.model.BaseResult;
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

    @Reference(retries = 2, timeout = 10000)
    private DubboStockService dubboStockService;

    @RabbitHandler
    public void process(PayDTO payDTO) {

        int num = orderService.createOrder(payDTO);

        if(num < 0){
            System.out.println("订单创建失败");
            //告知用户失败信息
            stringRedisTemplate.opsForHash().put(Constant.RedisKey.KEY_KILL_RESULT, String.format(Constant.RedisKey.KEY_RESULT_KEY, payDTO.getUsername(), payDTO.getGoods().getId()), Constant.SecKill.fail);
            //加库存
            stringRedisTemplate.opsForHash().increment(Constant.RedisKey.KEY_STOCK, payDTO.getGoods().getId().toString(), 1);
            return;
        }

        log.info("Receiver object : {}", payDTO);

        //step 1 pay  todo 付款方法
        boolean res = true;
        if(res){
            //step 2 update order state
            orderService.updateState(payDTO, Constant.Order.STAT_PAY);
            //step 3 add score
            userService.addScore(payDTO);
            //真正减库存
            BaseResult baseResult = dubboStockService.reduceStock(payDTO.getGoods().getId());
            //结果
            stringRedisTemplate.opsForHash().put(Constant.RedisKey.KEY_KILL_RESULT, String.format(Constant.RedisKey.KEY_RESULT_KEY, payDTO.getUsername(), payDTO.getGoods().getId()), Constant.SecKill.success);

        } else {
            //加库存
            stringRedisTemplate.opsForHash().increment(Constant.RedisKey.KEY_STOCK, payDTO.getGoods().getId().toString(), 1);
            //订单状态修改
            orderService.updateState(payDTO, Constant.Order.STAT_CANCEL);
            //结果
            stringRedisTemplate.opsForHash().put(Constant.RedisKey.KEY_KILL_RESULT, String.format(Constant.RedisKey.KEY_RESULT_KEY, payDTO.getUsername(), payDTO.getGoods().getId()), Constant.SecKill.fail);

        }

    }

}
