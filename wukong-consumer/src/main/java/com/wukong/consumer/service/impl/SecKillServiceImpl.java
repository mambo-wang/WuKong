package com.wukong.consumer.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.wukong.common.dubbo.DubboOrderService;
import com.wukong.common.exception.BusinessException;
import com.wukong.common.model.AddScoreDTO;
import com.wukong.common.model.GoodsVO;
import com.wukong.common.contants.Constant;
import com.wukong.consumer.rabbit.object.ObjectSender;
import com.wukong.consumer.service.GoodsService;
import com.wukong.consumer.service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service("secKillService")
public class SecKillServiceImpl implements SecKillService {

    @Autowired
    private GoodsService goodsService;

    @Reference(timeout=10000, retries=2)
    private DubboOrderService dubboOrderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectSender objectSender;

    @Override
    public void secKill(Long goodsId, String username) {

        //检查是否已经买过了（秒杀场景每人限量一份商品），下单后会写入redis ，此处读取redis

        //检查是否还有库存,读取redis
        HashOperations<String, String, String> hashOperations1 = stringRedisTemplate.opsForHash();
        Integer stock = Integer.valueOf(hashOperations1.get(Constant.RedisKey.KEY_STOCK, goodsId.toString()));
        if(stock <= 0){
            throw new BusinessException("500","寿光");
        }

        //预减库存，操作redis
        stringRedisTemplate.opsForHash().increment(Constant.RedisKey.KEY_STOCK, goodsId.toString(), -1);

        //rabbitMQ发送下单消息，削峰填谷（todo）

        //查询商品详情,操作redis
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        GoodsVO goodsVO = JSONObject.parseObject(hashOperations.get(Constant.RedisKey.KEY_GOODS, goodsId.toString()), GoodsVO.class);

        //减库存
        goodsService.reduceStock(goodsId);

        //创建订单
        dubboOrderService.addOrder(goodsVO, username);

        //支付（todo 半个小时期限，支付后修改订单状态，增加积分）

        //增加积分
        objectSender.send(new AddScoreDTO(username, goodsVO.getPrice().intValue()));

    }

}
