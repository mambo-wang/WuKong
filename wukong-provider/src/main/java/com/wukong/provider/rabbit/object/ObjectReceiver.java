package com.wukong.provider.rabbit.object;

import com.wukong.common.contants.Constant;
import com.wukong.common.model.PayDTO;
import com.wukong.provider.service.OrderService;
import com.wukong.provider.service.UserService;
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

    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void process(PayDTO payDTO) {
        log.info("Receiver object : {}", payDTO);

        //step 1 pay  todo 付款方法
        boolean res = false;
        //step 2 update order state
        if(res){
            orderService.updateState(payDTO.getUsername(), payDTO.getGoodsId(), Constant.Order.STAT_PAY);
            //step 3 add score
            userService.addScore(payDTO);
        } else {
            orderService.updateState(payDTO.getUsername(), payDTO.getGoodsId(), Constant.Order.STAT_CANCEL);
        }

    }

}
