package com.wukong.consumer.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.wukong.common.dubbo.DubboOrderService;
import com.wukong.common.exception.BusinessException;
import com.wukong.common.model.PayDTO;
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

import java.util.concurrent.TimeUnit;

@Service("secKillService")
@Slf4j
public class SecKillServiceImpl implements SecKillService {

    @Autowired
    private GoodsService goodsService;

    @Reference(timeout=10000, retries=2)
    private DubboOrderService dubboOrderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectSender objectSender;

    /**
     * seata at 分布式事务秒杀接口
     * todo tcc：锁定库存--减库存--失败加库存  创建订单--创建订单--删除订单
     * @param goodsId
     * @param username
     */
    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-gts-seata-example")
    public void secKill(Long goodsId, String username) {

        System.out.println("开始全局事务，XID = " + RootContext.getXID());

        //检查是否已经买过了（秒杀场景每人限量一份商品），下单后会写入redis ，此处读取redis
        boolean res = stringRedisTemplate.opsForValue().setIfAbsent("miaosha:" + goodsId  + ":" +username, username, 5, TimeUnit.MINUTES);
        if(!res){
            throw new BusinessException("500","不允许重复下单");
        }

        //检查是否还有库存,读取redis
        HashOperations<String, String, String> hashOperations1 = stringRedisTemplate.opsForHash();
        Integer stock = Integer.valueOf(hashOperations1.get(Constant.RedisKey.KEY_STOCK, goodsId.toString()));
        if(stock <= 0){
            throw new BusinessException("500","商品已售罄");
        }

        //预减库存，操作redis
        stringRedisTemplate.opsForHash().increment(Constant.RedisKey.KEY_STOCK, goodsId.toString(), -1);

        //创建订单，dubbo调用，操作数据库
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        GoodsVO goodsVO = JSONObject.parseObject(hashOperations.get(Constant.RedisKey.KEY_GOODS, goodsId.toString()), GoodsVO.class);
        BaseResult baseResult = dubboOrderService.addOrder(goodsVO, username);

        if(baseResult.getType() < 0){
            throw new BusinessException("500","秒杀失败");
        }

        //付款，修改订单状态、增加积分
        objectSender.send(new PayDTO(username, goodsVO.getId(), goodsVO.getPrice()));

    }

}
