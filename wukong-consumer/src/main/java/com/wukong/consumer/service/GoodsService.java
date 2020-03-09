package com.wukong.consumer.service;

import com.wukong.common.model.BasePage;
import com.wukong.consumer.controller.GoodsVO;

import java.util.List;

public interface GoodsService {

     BasePage<GoodsVO> queryPageable(int pageNo, int pageSize);

     void addGoods(GoodsVO goodsVO);

     void modifyGoods(GoodsVO goodsVO);

     void removeGoods(List<Long> ids);

     void reduceStock(Long goodsId);

 }
