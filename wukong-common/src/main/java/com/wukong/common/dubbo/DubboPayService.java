package com.wukong.common.dubbo;

import com.wukong.common.model.BaseResult;

public interface DubboPayService {

    BaseResult payMoney(Double price, String username);
}
