package com.wukong.common.dubbo;

import com.wukong.common.model.GoodsVO;

public interface DubboOrderService {

    /**
     * 创建订单
     * @param username
     */
    void addOrder(GoodsVO goodsVO, String username);
}
