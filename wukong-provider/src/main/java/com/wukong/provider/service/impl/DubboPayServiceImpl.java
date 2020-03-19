package com.wukong.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wukong.common.dubbo.DubboPayService;
import com.wukong.common.model.BaseResult;
import io.seata.core.context.RootContext;
import org.springframework.stereotype.Component;

@Service(retries=2)
@Component
public class DubboPayServiceImpl implements DubboPayService {
    @Override
    public BaseResult payMoney(Double price, String username) {

        System.out.println("全局事务id ：" + RootContext.getXID());
        return BaseResult.fail("500","支付失败");
//        return BaseResult.success(1);
    }
}
