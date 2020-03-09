package com.wukong.consumer.service.impl;

import com.wukong.consumer.service.GoodsService;
import com.wukong.consumer.service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("secKillService")
public class SecKillServiceImpl implements SecKillService {

    @Autowired
    private GoodsService goodsService;

    @Override
    public void secKill(Long goodsId, String username) {

        //减库存
        goodsService.reduceStock(goodsId);


    }

}
