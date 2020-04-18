package com.wukong.consumer.controller;

import com.wukong.common.contants.Constant;
import com.wukong.common.model.BasePage;
import com.wukong.common.model.BaseResult;
import com.wukong.common.model.GoodsVO;
import com.wukong.consumer.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author 王宝
 * @description 商品管理接口
 * @date 20200313
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping
    public BaseResult queryPageable(@RequestParam(name = "pageNo")Integer pageNo, @RequestParam(name = "pageSize")Integer pageSize){
        BasePage<GoodsVO> goodsVOBasePage = goodsService.queryPageable(pageNo, pageSize);
        return BaseResult.success(goodsVOBasePage);
    }

    @PostMapping
    public BaseResult add(@RequestBody GoodsVO goodsVO){

        goodsService.addGoods(goodsVO);
        return BaseResult.success(null);
    }

    @GetMapping("/top")
    public BaseResult top(){
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet().reverseRangeWithScores(Constant.RedisKey.KEY_SALES, 0, 4);
        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            System.out.println(tuple.getValue() + " : " + tuple.getScore());
        }
        return BaseResult.success(tuples);
    }
}
