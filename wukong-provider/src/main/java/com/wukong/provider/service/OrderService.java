package com.wukong.provider.service;

import com.wukong.common.model.GoodsVO;

public interface OrderService {

    int createOrder(GoodsVO goodsVO, String username);
}
