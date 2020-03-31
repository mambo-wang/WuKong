package com.wukong.provider.service;

import com.wukong.common.model.GoodsVO;
import com.wukong.common.model.PayDTO;

public interface OrderService {

    int createOrder(PayDTO payDTO);

    boolean updateState(PayDTO payDTO, Integer state);
}
