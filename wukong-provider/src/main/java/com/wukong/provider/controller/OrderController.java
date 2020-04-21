package com.wukong.provider.controller;

import com.wukong.common.model.BaseResult;
import com.wukong.provider.controller.vo.*;
import com.wukong.provider.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "订单控制器")
@RestController
@RequestMapping("/order")
@Validated
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/pay")
    public  BaseResult<OrderVO> findById(@RequestBody PayVO payVO){

        OrderVO orderVO = orderService.payMoney(payVO);
        return BaseResult.success(orderVO);
    }

    @ApiOperation(value = "查询秒杀结果")
    @GetMapping("/result")
    public BaseResult result(@RequestParam(name = "orderId")Long orderId){
        OrderVO result = orderService.querySecKillResult(orderId);
        return BaseResult.success(result);
    }

    @GetMapping
    public BaseResult queryByUserId(@RequestParam(name = "userId")Long userId){
        List<OrderVO> orderVOS = orderService.queryOrderList(userId);
        return BaseResult.success(orderVOS);
    }
}
