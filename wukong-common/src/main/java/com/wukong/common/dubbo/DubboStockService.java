package com.wukong.common.dubbo;


import com.wukong.common.model.BaseResult;

public interface DubboStockService {

    BaseResult reduceStock(Long goodsId);

    BaseResult<Integer> queryStock(Long goodsId);
}
