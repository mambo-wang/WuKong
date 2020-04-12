package com.wukong.consumer.service.impl;

import com.wukong.common.dubbo.DubboStockService;
import com.wukong.common.model.BaseResult;
import com.wukong.common.model.GoodsVO;
import com.wukong.consumer.service.GoodsService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Service(retries=3, timeout = 50000)
@Component
public class DubboStockServiceImpl implements DubboStockService {

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

    @Override
    public BaseResult<Integer> queryStock(Long goodsId) {
        try {
            GoodsVO goodsVO = goodsService.getOne(goodsId);
            return BaseResult.success(goodsVO.getStock());
        } catch (Exception e) {
            return BaseResult.fail("500", "查询出错");
        }
    }
}
