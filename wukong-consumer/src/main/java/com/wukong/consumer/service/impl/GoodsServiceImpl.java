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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service("goodsService")
public class GoodsServiceImpl implements GoodsService, InitializingBean {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, GoodsVO> opsForHashGoods;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Integer> opsForHashStock;

    @Override
    public BasePage<GoodsVO> queryPageable(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo -1, pageSize, new Sort(Sort.Direction.ASC, "id"));
        Page<Goods> page = goodsRepository.findAll(pageable);

        BasePage<GoodsVO> basePage = new BasePage<>(page.getTotalElements(), convert(page.getContent()));
        return basePage;
    }

    @Override
    public List<GoodsVO> queryAll() {
        if(redisTemplate.hasKey(Constant.RedisKey.KEY_GOODS)){
            return opsForHashGoods.values(Constant.RedisKey.KEY_GOODS);
        }
        // 强制路由主库
//        HintManager.getInstance().setMasterRouteOnly();
        List<Goods> goods = goodsRepository.findByDeleted("n");
        List<GoodsVO> goodsVOS = convert(goods);

        addCache(goodsVOS);

        return goodsVOS;
    }

    @Override
    public GoodsVO getOne(Long goodsId) {
        return convert(goodsRepository.findById(goodsId).orElseThrow(() -> new BusinessException("500", "找不到该商品")));
    }

    @Override
    public void addGoods(GoodsVO goodsVO) {
        Goods saved = goodsRepository.save(convert(goodsVO));
        opsForHashGoods.put(Constant.RedisKey.KEY_GOODS, saved.getId().toString(), convert(saved));
    }

    @Override
    public void modifyGoods(GoodsVO goodsVO) {
        Goods goods = convert(goodsVO);
        Goods saved = goodsRepository.save(goods);
        opsForHashGoods.put(Constant.RedisKey.KEY_GOODS, saved.getId().toString(), goodsVO);
    }

    @Override
    public void removeGoods(List<Long> ids) {
        opsForHashGoods.delete(Constant.RedisKey.KEY_GOODS, ids.toArray());
        ids.forEach(id -> goodsRepository.deleteById(id));
    }

    @Transactional
    @Override
    public int reduceStock(Long goodsId) {
        int num = goodsRepository.reduceStock(goodsId);
        return num;
    }

    private List<GoodsVO> convert(List<Goods> goods){
        return goods.stream().map(this::convert).collect(Collectors.toList());
    }

    private GoodsVO convert(Goods goods){
        GoodsVO goodsVO = new GoodsVO();
        BeanUtils.copyProperties(goods, goodsVO);
        return goodsVO;
    }

    private Goods convert(GoodsVO goodsVO){
        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsVO, goods);
        return goods;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //放入缓存
        queryAll();
    }

    private void addCache(List<GoodsVO> goodsVOS){
        goodsVOS.forEach(goods -> {
            opsForHashGoods.put(Constant.RedisKey.KEY_GOODS, goods.getId().toString(), goods);
            opsForHashStock.put(Constant.RedisKey.KEY_STOCK, goods.getId().toString(), goods.getStock());
        });
        redisTemplate.expire(Constant.RedisKey.KEY_GOODS, 30, TimeUnit.MINUTES);
    }

//    /** boss直聘笔试题
//     * 砍价接口
//     * @param goodsId 商品id
//     * @param userId 用户Id
//     * @param wxId 微信用户唯一标识
//     * @return 提示信息
//     */
//    public String bargain(Long goodsId, Long userId, Long wxId){
//
//        /*设计砍价表
//      CREATE TABLE `bargain` (
//	`id` BIGINT NOT NULL AUTO_INCREMENT,
//	`goods_id` BIGINT NOT NULL DEFAULT '0',
//	`user_id` BIGINT NOT NULL DEFAULT '0',
//	`price` DECIMAL(10,0) NOT NULL DEFAULT '0',
//	`cut_count` int(2) NOT NULL DEFAULT '0',
//	PRIMARY KEY (`id`)) */
//
//        // 前提：第一步，分享到微信的时候在bargain表中创建一条记录，价格初始值为商品原价
//        // 第二步，微信好友点击砍价进入该方法，校验当前是否已经到达底价，方法根据goodsId和userId查 bargain表，查看min_price是否为y
//        Integer cutCount = bargainRepository.findMinPriceByGoodsIdAndUserId(goodsId, userId);
//
//        //如果到达底价，返回合适的提示信息
//        if(cutCount != null && cutCount > 11){
//            return "砍价结束";
//        }
//
//        //第三步，查询该人是否已经砍过
//        String value = redisTemplate.opsForHash().get("goodsId + userId", wxId);
//        if(value != null){
//            return "不能重复砍价";
//        }
//
//        //第四步，砍价
//        redisTemplate.opsForHash().put("goodsId + userId", wxId, "1");
//
//        Bargain bargain = bargainRepository.findByGoodsId(goodsId);
//        //修改砍价次数和价格
//        bargain.setCutCount(bargain.getCutCount +1);
//        Integer cutPrice = 11 - (bargain.getCutCount);
//        bargain.setPrice(bargain.getPrice() - cutPrice);
//        bargainRepository.save(bargain);
//
//        return "恭喜您，成功砍掉" + cutPrice + "元";
//    }
}
