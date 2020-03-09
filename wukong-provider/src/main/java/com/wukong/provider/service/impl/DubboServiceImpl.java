package com.wukong.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wukong.common.dubbo.DubboService;
import com.wukong.common.model.UserVO;
import com.wukong.provider.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Service
@Component
public class DubboServiceImpl implements DubboService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserVO getUser(String username) {
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
