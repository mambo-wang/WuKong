package com.wukong.provider.service.impl;

import com.wukong.common.model.GoodsVO;
import com.wukong.common.model.PayDTO;
import com.wukong.common.model.UserVO;
import com.wukong.common.contants.Constant;
import com.wukong.provider.entity.Order;
import com.wukong.provider.mapper.OrderMapper;
import com.wukong.provider.service.OrderService;
import com.wukong.provider.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Service("orderService")
@Slf4j
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Override
    public Long createOrder(GoodsVO goodsVO, String username) {
        log.info("add order");
        UserVO userVO = userService.findByUsername(username);
        Order order = new Order();
        order.setAddress(userVO.getAddress());
        order.setCreateDate(new Date());
        order.setGoodsCount(1);
        order.setUserId(userVO.getId());
        order.setGoodsId(goodsVO.getId());
        order.setGoodsName(goodsVO.getName());
        order.setGoodsPrice(BigDecimal.valueOf(goodsVO.getPrice()));
        order.setStatus(Constant.Order.STAT_NOT_PAY);
        order.setPayDate(new Date());
        orderMapper.insert(order);
        if(redisTemplate.hasKey(Constant.RedisKey.KEY_SALES)){
            redisTemplate.opsForHash().increment(Constant.RedisKey.KEY_SALES, goodsVO.getId().toString(), 1);
        } else {
            redisTemplate.opsForHash().put(Constant.RedisKey.KEY_SALES, goodsVO.getId().toString(), "1");
        }
        return order.getId();
    }

    @Override
    public boolean updateState(PayDTO payDTO, Integer state) {
        Order order = orderMapper.selectByPrimaryKey(payDTO.getOrderId());
        if(Objects.isNull(order)){
            return false;
        }
        order.setStatus(state);
        orderMapper.updateByPrimaryKey(order);
        return true;
    }
}
