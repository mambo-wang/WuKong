package com.wukong.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wukong.common.dubbo.DubboOrderService;
import com.wukong.common.model.BaseResult;
import com.wukong.common.model.GoodsVO;
import com.wukong.provider.service.OrderService;
import io.seata.core.context.RootContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Service(retries=2)
@Component
public class DubboOrderServiceImpl implements DubboOrderService {


    @Autowired
    private OrderService orderService;

    @Override
    public BaseResult addOrder(GoodsVO goodsVO, String username) {

        System.out.println("全局事务id ：" + RootContext.getXID());
        int num = orderService.createOrder(goodsVO, username);
        if(num > 0){
            return BaseResult.success(num);
        } else {
            return BaseResult.fail("500", "添加订单失败");
        }
    }
}
