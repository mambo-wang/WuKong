package com.wukong.consumer.service;

import com.wukong.common.model.BasePage;
import com.wukong.common.model.GoodsVO;

import java.util.List;

public interface GoodsService {

     BasePage<GoodsVO> queryPageable(int pageNo, int pageSize);

     List<GoodsVO> queryAll();

     GoodsVO getOne(Long goodsId);

     void addGoods(GoodsVO goodsVO);

     void modifyGoods(GoodsVO goodsVO);

     void removeGoods(List<Long> ids);

     int reduceStock(Long goodsId);

 }
