package com.wukong.service.pattern.proxy;

import com.wukong.common.model.BaseResult;
import com.wukong.common.model.UserVO;

import java.lang.reflect.Proxy;

public class Test {

    public static void main(String[] args) {
        UserController target = new UserController();
        IUserController userControllerDynamicProxy = (IUserController) Proxy.newProxyInstance(UserController.class.getClassLoader(), new Class[]{IUserController.class}, new MethodInvokeHandler(target));
        BaseResult<UserVO> returnData = userControllerDynamicProxy.getById(12);
        System.out.println("调用结果：" + returnData);
        System.out.println("========================");
        returnData = userControllerDynamicProxy.getById(-12);
        System.out.println("调用结果：" + returnData);
    }

}
