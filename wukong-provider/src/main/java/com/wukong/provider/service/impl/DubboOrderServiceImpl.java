package com.wukong.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wukong.common.dubbo.DubboOrderService;
import com.wukong.common.model.GoodsVO;
import com.wukong.common.model.UserVO;
import com.wukong.common.utils.Constant;
import com.wukong.provider.entity.Order;
import com.wukong.provider.service.OrderService;
import com.wukong.provider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

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
