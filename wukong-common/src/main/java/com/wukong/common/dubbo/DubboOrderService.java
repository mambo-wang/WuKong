package com.wukong.common.dubbo;

import com.wukong.common.model.BaseResult;
import com.wukong.common.model.GoodsVO;

public interface DubboOrderService {

    /**
     * 创建订单
     * @param username
     */
    BaseResult addOrder(GoodsVO goodsVO, String username);
}
