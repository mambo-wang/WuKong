package com.wukong.consumer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wukong.common.exception.BusinessException;
import com.wukong.common.model.SecKillDTO;
import com.wukong.common.model.GoodsVO;
import com.wukong.common.contants.Constant;
import com.wukong.common.utils.SnowFlake;
import com.wukong.consumer.rabbit.hello.HelloSender;
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

import java.util.concurrent.TimeUnit;

@Service("secKillService")
@Slf4j
public class SecKillServiceImpl implements SecKillService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectSender objectSender;

    @Autowired
    private HelloSender helloSender;

    private final SnowFlake snowFlake = new SnowFlake(1,1);

    /**
     * @param goodsId
     * @param username
     */
    @Override
    public Long secKill(Long goodsId, String username) {

        log.info("开始全局事务，XID = {}" ,RootContext.getXID());

        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        GoodsVO goodsVO = JSONObject.parseObject(hashOperations.get(Constant.RedisKey.KEY_GOODS, goodsId.toString()), GoodsVO.class);
        if(goodsVO == null){
            throw new BusinessException("500","该商品秒杀已结束");
        }

        //检查是否已经买过了（秒杀场景每人限量一份商品），下单后会写入redis ，此处读取redis
        boolean res = stringRedisTemplate.opsForValue().setIfAbsent("miaosha:" + goodsId  + ":" +username, username, 2, TimeUnit.MINUTES);
        if(!res){
            throw new BusinessException("500","不允许重复下单");
        }

        //检查是否还有库存,读取redis
        HashOperations<String, String, String> hashOperations1 = stringRedisTemplate.opsForHash();
        Integer stock = Integer.valueOf(hashOperations1.get(Constant.RedisKey.KEY_STOCK, goodsId.toString()));
        log.info("库存校验中，当前剩余库存：{}", stock);
        if(stock <= 0){
            throw new BusinessException("500","商品已售罄");
        }
        //预减库存，操作redis
        stringRedisTemplate.opsForHash().increment(Constant.RedisKey.KEY_STOCK, goodsId.toString(), -1);
        log.info("预减库存成功");
        //snowflake算法创建orderId
        Long orderId = snowFlake.nextId();

        SecKillDTO secKillDTO = new SecKillDTO(username, goodsVO, orderId);
        //创建订单、付款，修改订单状态、增加积分
        objectSender.send(secKillDTO);
        //超时未付款（检验订单状态）释放库存
        helloSender.sendDeadLetter(secKillDTO, TimeUnit.MINUTES.toSeconds(2));

        return orderId;
    }

}
