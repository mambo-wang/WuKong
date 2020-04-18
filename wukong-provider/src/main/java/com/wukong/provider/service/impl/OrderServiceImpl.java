package com.wukong.provider.service.impl;

import com.wukong.common.dubbo.DubboStockService;
import com.wukong.common.exception.BusinessException;
import com.wukong.common.model.BaseResult;
import com.wukong.common.model.GoodsVO;
import com.wukong.common.model.SecKillDTO;
import com.wukong.common.model.UserVO;
import com.wukong.common.contants.Constant;
import com.wukong.provider.controller.vo.OrderVO;
import com.wukong.provider.controller.vo.PayVO;
import com.wukong.provider.dto.AddScoreDTO;
import com.wukong.provider.entity.Order;
import com.wukong.provider.mapper.OrderMapper;
import com.wukong.provider.rabbit.object.AddScoreSender;
import com.wukong.provider.service.OrderService;
import com.wukong.provider.service.UserService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.rpc.cluster.loadbalance.RoundRobinLoadBalance;
import org.apache.dubbo.rpc.cluster.support.FailfastCluster;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Reference(retries = 2, timeout = 10000, loadbalance = RoundRobinLoadBalance.NAME, cluster = FailfastCluster.NAME)
    private DubboStockService dubboStockService;

    @Autowired
    private AddScoreSender addScoreSender;

    @Override
    public int createOrder(SecKillDTO secKillDTO) {
        GoodsVO goodsVO = secKillDTO.getGoods();
        if(Objects.isNull(goodsVO)){
            return -1;
        }
        String username = secKillDTO.getUsername();
        Long id = secKillDTO.getOrderId();
        log.info("add order");
        UserVO userVO = userService.findByUsername(username);
        Order order = new Order();
        order.setId(id);
        order.setAddress(userVO.getAddress());
        order.setCreateDate(new Date());
        order.setGoodsCount(1);
        order.setUserId(userVO.getId());
        order.setGoodsId(goodsVO.getId());
        order.setGoodsName(goodsVO.getName());
        order.setGoodsPrice(BigDecimal.valueOf(goodsVO.getPrice()));
        order.setStatus(Constant.Order.STAT_NOT_PAY);
        order.setPayDate(new Date());
        int num = orderMapper.insert(order);
        return num;
    }

    @Override
    public boolean updateState(Long orderId, Integer state) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if(Objects.isNull(order)){
            return false;
        }
        order.setStatus(state);
        orderMapper.updateByPrimaryKey(order);
        return true;
    }

    @GlobalTransactional(timeoutMills = 300000, name = "pay-seata-transaction")
    @Override
    public OrderVO payMoney(PayVO payVO) {

        Order order = orderMapper.selectByPrimaryKey(payVO.getOrderId());
        //校验订单有效性
        if(Objects.isNull(order) || order.getStatus() != Constant.Order.STAT_NOT_PAY){
            throw new BusinessException("500", "訂單状态异常");
        }
        log.info("订单状态校验通过");

        boolean flag = userService.checkPwd(order.getUserId(), payVO.getPassword());
        if(!flag){
            throw new BusinessException("500", "支付密码（同登录密码）输入错误");
        }
        log.info("支付密码校验通过");

        //校验库存
        BaseResult<Integer> baseResult = dubboStockService.queryStock(order.getGoodsId());
        if(baseResult.getType() <0 || baseResult.getData() <= 0){
            throw new BusinessException("500", "库存为空");
        }
        log.info("库存校验通过，还剩余库存：{}", baseResult.getData());

        //step 1 pay
        int res = userService.reduceBalance(order.getUserId(), order.getGoodsPrice());
        if(res > 0){

            log.info("支付成功");
            //真正减库存
            dubboStockService.reduceStock(order.getGoodsId());
            log.info("减库存成功");
            //step 2 update order state
            updateState(payVO.getOrderId(), Constant.Order.STAT_PAY);
            log.info("修改订单状态成功，修改为：{}",Constant.Order.STAT_PAY);

            //step 3 add score
            addScoreSender.send(new AddScoreDTO(order.getUserId(), order.getGoodsPrice().intValue()));
            log.info("发送用户增加积分消息成功");

            //商品售出数据统计
            if(redisTemplate.hasKey(Constant.RedisKey.KEY_SALES)){
                redisTemplate.opsForZSet().incrementScore(Constant.RedisKey.KEY_SALES, order.getGoodsId().toString(), 1);
            } else {
                redisTemplate.opsForZSet().add(Constant.RedisKey.KEY_SALES, order.getGoodsId().toString(), 1);
            }

        } else {
            log.info("支付失败");
            //加库存
            redisTemplate.opsForHash().increment(Constant.RedisKey.KEY_STOCK, order.getGoodsId().toString(), 1);
            //订单状态修改
            updateState(payVO.getOrderId(), Constant.Order.STAT_PAY_FAIL);
        }
        return convert(orderMapper.selectByPrimaryKey(payVO.getOrderId()));
    }

    @Override
    public OrderVO querySecKillResult(Long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if(Objects.isNull(order)){
            Object state = redisTemplate.opsForHash().get(Constant.RedisKey.KEY_KILL_RESULT, String.format(Constant.RedisKey.KEY_RESULT_KEY, orderId));

            if(Objects.isNull(state)){
                throw new BusinessException("500", "订单正在创建中，请稍后再查");
            }
            String stateDesc = String.valueOf(state);
            if(stateDesc.equals(Constant.SecKill.fail)){
                redisTemplate.opsForHash().delete(Constant.RedisKey.KEY_KILL_RESULT, String.format(Constant.RedisKey.KEY_RESULT_KEY, orderId));
                throw new BusinessException("500", "订单创建失败，秒杀失败");
            }
        }
        return convert(order);
    }

    private OrderVO convert(Order order){

        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);

        return orderVO;

    }
}
