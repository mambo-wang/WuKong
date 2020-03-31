package com.wukong.consumer.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.wukong.common.dubbo.DubboOrderService;
import com.wukong.common.exception.BusinessException;
import com.wukong.common.model.PayDTO;
import com.wukong.common.model.BaseResult;
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
    private GoodsService goodsService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectSender objectSender;

    @Autowired
    private HelloSender helloSender;

    private final SnowFlake snowFlake = new SnowFlake(1,1);

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

        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        GoodsVO goodsVO = JSONObject.parseObject(hashOperations.get(Constant.RedisKey.KEY_GOODS, goodsId.toString()), GoodsVO.class);
        if(goodsVO == null){
            throw new BusinessException("500","该商品秒杀已结束");
        }

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

        //snowflake算法创建orderId
        Long orderId = snowFlake.nextId();

        PayDTO payDTO = new PayDTO(username, goodsVO, orderId);
        //创建订单、付款，修改订单状态、增加积分
        objectSender.send(payDTO);
        //超时未付款（检验订单状态）释放库存
        helloSender.sendDeadLetter(payDTO, TimeUnit.MINUTES.toSeconds(1));
        //结果
        stringRedisTemplate.opsForHash().put(Constant.RedisKey.KEY_KILL_RESULT, String.format(Constant.RedisKey.KEY_RESULT_KEY, payDTO.getUsername(), goodsId), Constant.SecKill.processing);

    }

    @Override
    public String querySecKillResult(Long goodsId, String username) {
        Object re  = stringRedisTemplate.opsForHash().get(Constant.RedisKey.KEY_KILL_RESULT, String.format(Constant.RedisKey.KEY_RESULT_KEY, username, goodsId));
        return re.toString();
    }

}
