package com.wukong.consumer.service.impl;

import com.wukong.common.dubbo.DubboTestService;
import com.wukong.common.model.BaseResult;
import com.wukong.consumer.service.GoodsService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Service(retries=3, timeout = 50000, interfaceClass = DubboTestService.class)
@Component
public class DubboTestServiceImpl implements DubboTestService {
    @Autowired
    private GoodsService goodsService;

    @Transactional
    @Override
    public BaseResult reduceStock(Long goodsId) {

        int num = goodsService.reduceStock(goodsId);
        if(num <= 0){
            return BaseResult.fail("500","减库存失败");
        }
        return BaseResult.success(num);
    }
}
