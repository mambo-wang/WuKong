package com.wukong.consumer.controller;

import com.wukong.common.annotations.AccessLimit;
import com.wukong.common.model.BaseResult;
import com.wukong.consumer.service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secKill")
public class SecKillController {

    @Autowired
    private SecKillService secKillService;

    /**
     * 秒杀接口
     * @param goodsId 商品id
     * @param username 用户名
     * @return 结果
     */
    @AccessLimit(seconds = 60, maxCount = 5, needLogin = false)
    @GetMapping()
    public BaseResult secKill(@RequestParam(name = "goodsId")Long goodsId, @RequestParam(name = "username")String username){
        secKillService.secKill(goodsId, username);
        return BaseResult.success(null);
    }
}
