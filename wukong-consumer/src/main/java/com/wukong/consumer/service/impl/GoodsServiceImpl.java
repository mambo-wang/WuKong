package com.wukong.consumer.service.impl;

import com.wukong.common.model.BasePage;
import com.wukong.consumer.controller.GoodsVO;
import com.wukong.consumer.repository.GoodsRepository;
import com.wukong.consumer.repository.entity.Goods;
import com.wukong.consumer.service.GoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Override
    public BasePage<GoodsVO> queryPageable(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, new Sort(Sort.Direction.ASC, "id"));
        Page<Goods> page = goodsRepository.findAll(pageable);

        BasePage<GoodsVO> basePage = new BasePage<>(page.getTotalElements(), convert(page.getContent()));
        return basePage;
    }

    @Override
    public void addGoods(GoodsVO goodsVO) {

    }

    @Override
    public void modifyGoods(GoodsVO goodsVO) {
        Goods goods = convert(goodsVO);
        goodsRepository.save(goods);
    }

    @Override
    public void removeGoods(List<Long> ids) {

    }

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
}
