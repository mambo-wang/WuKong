package com.wukong.provider.service;

import com.wukong.common.model.SecKillDTO;
import com.wukong.provider.controller.vo.OrderVO;
import com.wukong.provider.controller.vo.PayVO;

public interface OrderService {

    int createOrder(SecKillDTO secKillDTO);

    boolean updateState(Long orderId, Integer state);

    OrderVO payMoney(PayVO payVO);

    OrderVO querySecKillResult(Long orderId);

}
