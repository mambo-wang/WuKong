package com.wukong.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wukong.common.dubbo.DubboOrderService;
import com.wukong.common.model.GoodsVO;
import com.wukong.provider.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Service(retries=2)
@Component
public class DubboOrderServiceImpl implements DubboOrderService {


    @Autowired
    private OrderService orderService;

    @Override
    public void addOrder(GoodsVO goodsVO, String username) {
        orderService.createOrder(goodsVO, username);
    }
}
