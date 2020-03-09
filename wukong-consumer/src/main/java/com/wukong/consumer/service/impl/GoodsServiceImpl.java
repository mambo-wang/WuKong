package com.wukong.consumer.service.impl;

import com.wukong.common.model.BasePage;
import com.wukong.common.model.GoodsVO;
import com.wukong.common.utils.Constant;
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
import java.util.stream.Collectors;

@Service("goodsService")
public class GoodsServiceImpl implements GoodsService, InitializingBean {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public BasePage<GoodsVO> queryPageable(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, new Sort(Sort.Direction.ASC, "id"));
        Page<Goods> page = goodsRepository.findAll(pageable);

        BasePage<GoodsVO> basePage = new BasePage<>(page.getTotalElements(), convert(page.getContent()));
        return basePage;
    }

    @Override
    public List<GoodsVO> queryAll() {
        HashOperations<String, Long, GoodsVO> hashOperations = stringRedisTemplate.opsForHash();
        List<GoodsVO>  goodsVOS = hashOperations.values(Constant.RedisKey.KEY_GOODS);
        if(CollectionUtils.isNotEmpty(goodsVOS)){
            return goodsVOS;
        }
        List<Goods> goods = goodsRepository.findByDeleted("n");
        return convert(goods);
    }

    @Override
    public GoodsVO getOne(Long goodsId) {
        HashOperations<String, Long, GoodsVO> hashOperations = stringRedisTemplate.opsForHash();
        return hashOperations.get(Constant.RedisKey.KEY_GOODS, goodsId);
    }

    @Override
    public void addGoods(GoodsVO goodsVO) {
        Goods saved = goodsRepository.save(convert(goodsVO));
        stringRedisTemplate.opsForHash().put(Constant.RedisKey.KEY_GOODS, saved.getId(), saved);
    }

    @Override
    public void modifyGoods(GoodsVO goodsVO) {
        Goods goods = convert(goodsVO);
        Goods saved = goodsRepository.save(goods);
        stringRedisTemplate.opsForHash().put(Constant.RedisKey.KEY_GOODS, saved.getId(), saved);
    }

    @Override
    public void removeGoods(List<Long> ids) {
        HashOperations<String, Long, GoodsVO> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.delete(Constant.RedisKey.KEY_GOODS, ids);
        ids.forEach(id -> goodsRepository.deleteById(id));
    }

    @Transactional
    @Override
    public void reduceStock(Long goodsId) {
        goodsRepository.reduceStock(goodsId);
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
        List<GoodsVO> goodsVOS = queryAll();

        goodsVOS.forEach(goods -> {
            stringRedisTemplate.opsForHash().put(Constant.RedisKey.KEY_GOODS, goods.getId(), goods);
            stringRedisTemplate.opsForHash().put(Constant.RedisKey.KEY_STOCK, goods.getId(), goods.getStock());
        });
    }
}
