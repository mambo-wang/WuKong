package com.wukong.consumer.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.wukong.common.dubbo.DubboOrderService;
import com.wukong.common.dubbo.DubboPayService;
import com.wukong.common.exception.BusinessException;
import com.wukong.common.model.AddScoreDTO;
import com.wukong.common.model.BaseResult;
import com.wukong.common.model.GoodsVO;
import com.wukong.common.contants.Constant;
import com.wukong.consumer.rabbit.object.ObjectSender;
import com.wukong.consumer.service.GoodsService;
import com.wukong.consumer.service.SecKillService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service("secKillService")
@Slf4j
public class SecKillServiceImpl implements SecKillService {

    @Autowired
    private GoodsService goodsService;

    @Reference(timeout=10000, retries=2)
    private DubboOrderService dubboOrderService;

    @Reference(timeout=10000, retries=2)
    private DubboPayService dubboPayService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectSender objectSender;

    /**
     * seata分布式事务秒杀接口
     * @param goodsId
     * @param username
     */
    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-gts-seata-example")
    public void secKill(Long goodsId, String username) {

        System.out.println("开始全局事务，XID = " + RootContext.getXID());
        //检查是否已经买过了（秒杀场景每人限量一份商品），下单后会写入redis ，此处读取redis

        //检查是否还有库存,读取redis
        HashOperations<String, String, String> hashOperations1 = stringRedisTemplate.opsForHash();
        Integer stock = Integer.valueOf(hashOperations1.get(Constant.RedisKey.KEY_STOCK, goodsId.toString()));
        if(stock <= 0){
            throw new BusinessException("500","寿光");
        }

        //预减库存，操作redis
        stringRedisTemplate.opsForHash().increment(Constant.RedisKey.KEY_STOCK, goodsId.toString(), -1);

        //查询商品详情,操作redis
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        GoodsVO goodsVO = JSONObject.parseObject(hashOperations.get(Constant.RedisKey.KEY_GOODS, goodsId.toString()), GoodsVO.class);

        //减库存
        int num = goodsService.reduceStock(goodsId);

        //创建订单
        log.info("---before dubbo call order add method");
        BaseResult baseResult = dubboOrderService.addOrder(goodsVO, username);
        log.info("---after dubbo call order add method");

        //支付（todo 半个小时期限，支付后修改订单状态，增加积分）
        BaseResult baseResult1 = dubboPayService.payMoney(goodsVO.getPrice(), username);

        if(num <= 0 || baseResult.getType() < 0 || baseResult1.getType() < 0){
            throw new BusinessException("500","秒杀失败");
        }

        //增加积分
        objectSender.send(new AddScoreDTO(username, goodsVO.getPrice().intValue()));

    }

}
