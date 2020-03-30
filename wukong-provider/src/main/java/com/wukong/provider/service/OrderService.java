package com.wukong.provider.service;

import com.wukong.common.model.GoodsVO;
import com.wukong.common.model.PayDTO;

public interface OrderService {

    Long createOrder(GoodsVO goodsVO, String username);

    boolean updateState(PayDTO payDTO, Integer state);
}
