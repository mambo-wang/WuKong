package com.wukong.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wukong.common.model.UserVO;
import com.wukong.common.utils.StringManager;
import com.wukong.provider.entity.User;
import com.wukong.provider.mapper.UserMapper;
import com.wukong.service.HelloService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//dubbo注解，暴露服务
@Service
@Component
public class HelloServiceImpl implements HelloService {

    @Autowired
    private UserMapper userMapper;

    StringManager sm = StringManager.getManager("common");

    @Override
    public UserVO sayHello(String username) {
        return convert(userMapper.selectByUsername(username));
    }

    private UserVO convert(User user){
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO, "password");
        userVO.setName("测试");
        userVO.setUsername("测试数据");
        return userVO;
    }
}
