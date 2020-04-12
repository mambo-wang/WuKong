package com.wukong.common.dubbo;

import com.wukong.common.model.BaseResult;

public interface DubboTestService {

    BaseResult reduceStock(Long goodsId);

}
