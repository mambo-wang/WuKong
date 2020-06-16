package com.wukong.consumer.service.impl;

import com.wukong.common.exception.BusinessException;
import com.wukong.common.model.SecKillDTO;
import com.wukong.common.model.GoodsVO;
import com.wukong.common.contants.Constant;
import com.wukong.common.utils.SnowFlake;
import com.wukong.consumer.rabbit.hello.HelloSender;
import com.wukong.consumer.rabbit.object.ObjectSender;
import com.wukong.consumer.service.SecKillService;
import com.wukong.consumer.utils.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service("secKillService")
@Slf4j
public class SecKillServiceImpl implements SecKillService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, GoodsVO> opsForHashGoods;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Integer> opsForHashStock;

    @Resource
    private RedisLock redisLock;

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

        GoodsVO goodsVO = opsForHashGoods.get(Constant.RedisKey.KEY_GOODS, goodsId.toString());
        if(goodsVO == null){
            throw new BusinessException("500","该商品秒杀已结束");
        }

        //防止重复提交表单或者重复下单
//        boolean res = redisTemplate.opsForValue().setIfAbsent("miaosha:" + goodsId  + ":" +username, username, 2, TimeUnit.SECONDS);
//        if(!res){
//            throw new BusinessException("500","不允许重复下单");
//        }

        //预减库存，操作redis
        log.info("预减库存中");
        boolean result = redisLock.getLock("goods-lock:" + goodsId, 1, 50, 50);
        if(result){
            try {
                long stock1 = redisTemplate.opsForHash().increment(Constant.RedisKey.KEY_STOCK, goodsId.toString(), -1);
                if (stock1 < 0) {
                    redisTemplate.opsForHash().increment(Constant.RedisKey.KEY_STOCK, goodsId.toString(), 1);
                    throw new BusinessException("500", "商品已售罄");
                }
            } finally {
                redisLock.releaseLock("goods-lock:" + goodsId);
            }
        } else {
            throw new BusinessException("500","系统繁忙，请稍后再试");
        }

        //snowflake算法创建orderId
        Long orderId = snowFlake.nextId();

        SecKillDTO secKillDTO = new SecKillDTO(username, goodsVO, orderId);
        //创建订单、付款，修改订单状态、增加积分
        objectSender.send(secKillDTO);
        //超时未付款（检验订单状态）释放库存
        helloSender.sendDeadLetter(secKillDTO, TimeUnit.MINUTES.toSeconds(5));

        return orderId;
    }

}
