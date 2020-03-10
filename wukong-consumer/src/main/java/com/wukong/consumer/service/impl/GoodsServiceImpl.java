package com.wukong.consumer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wukong.common.exception.BusinessException;
import com.wukong.common.model.BasePage;
import com.wukong.common.model.GoodsVO;
import com.wukong.common.contants.Constant;
import com.wukong.consumer.repository.GoodsRepository;
import com.wukong.consumer.repository.entity.Goods;
import com.wukong.consumer.service.GoodsService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service("goodsService")
public class GoodsServiceImpl implements GoodsService, InitializingBean {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public BasePage<GoodsVO> queryPageable(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, new Sort(Sort.Direction.ASC, "id"));
        Page<Goods> page = goodsRepository.findAll(pageable);

        BasePage<GoodsVO> basePage = new BasePage<>(page.getTotalElements(), convert(page.getContent()));
        return basePage;
    }

    @Override
    public List<GoodsVO> queryAll() {
        if(redisTemplate.hasKey(Constant.RedisKey.KEY_GOODS)){
            HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
            List<String>  goodsVOS = hashOperations.values(Constant.RedisKey.KEY_GOODS);
            if(CollectionUtils.isNotEmpty(goodsVOS)){
                return goodsVOS.stream().map(s -> JSONObject.parseObject(s, GoodsVO.class)).collect(Collectors.toList());
            }
        }
        List<Goods> goods = goodsRepository.findByDeleted("n");
        List<GoodsVO> goodsVOS = convert(goods);

        addCache(goodsVOS);

        return goodsVOS;
    }

    @Override
    public GoodsVO getOne(Long goodsId) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        return JSONObject.parseObject(hashOperations.get(Constant.RedisKey.KEY_GOODS, goodsId.toString()), GoodsVO.class);
    }

    @Override
    public void addGoods(GoodsVO goodsVO) {
        Goods saved = goodsRepository.save(convert(goodsVO));
        redisTemplate.opsForHash().put(Constant.RedisKey.KEY_GOODS, saved.getId().toString(), JSONObject.toJSONString(saved));
    }

    @Override
    public void modifyGoods(GoodsVO goodsVO) {
        Goods goods = convert(goodsVO);
        Goods saved = goodsRepository.save(goods);
        redisTemplate.opsForHash().put(Constant.RedisKey.KEY_GOODS, saved.getId().toString(), JSONObject.toJSONString(saved));
    }

    @Override
    public void removeGoods(List<Long> ids) {
        HashOperations<String, Long, GoodsVO> hashOperations = redisTemplate.opsForHash();
        //todo
        hashOperations.delete(Constant.RedisKey.KEY_GOODS, ids.toArray());
        ids.forEach(id -> goodsRepository.deleteById(id));
    }

    @Transactional
    @Override
    public void reduceStock(Long goodsId) {
        int num = goodsRepository.reduceStock(goodsId);
        if(num == 0){
            throw new BusinessException("5555","无库存");
        }
        Goods goods = goodsRepository.findById(goodsId).get();
        redisTemplate.opsForHash().put(Constant.RedisKey.KEY_GOODS, goods.getId().toString(), JSONObject.toJSONString(goods));
    }

    private List<GoodsVO> convert(List<Goods> goods){
        return goods.stream().map(this::convert).collect(Collectors.toList());
    }

    private GoodsVO convert(Goods goods){
        GoodsVO goodsVO = new GoodsVO();
        BeanUtils.copyProperties(goods, goodsVO);
        return goodsVO;
    }

    private Goods convert(GoodsVO goods){
        Goods goodsVO = new Goods();
        BeanUtils.copyProperties(goods, goodsVO);
        return goodsVO;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //放入缓存
        queryAll();
    }

    private void addCache(List<GoodsVO> goodsVOS){
        goodsVOS.forEach(goods -> {
            redisTemplate.opsForHash().put(Constant.RedisKey.KEY_GOODS, goods.getId().toString(), JSONObject.toJSONString(goods));
            redisTemplate.opsForHash().put(Constant.RedisKey.KEY_STOCK, goods.getId().toString(), goods.getStock().toString());
        });
        redisTemplate.expire(Constant.RedisKey.KEY_GOODS, 10, TimeUnit.MINUTES);
        redisTemplate.expire(Constant.RedisKey.KEY_STOCK, 10, TimeUnit.MINUTES);
    }
}
