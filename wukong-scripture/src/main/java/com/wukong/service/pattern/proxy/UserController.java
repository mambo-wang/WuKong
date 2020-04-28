package com.wukong.service.pattern.proxy;

import com.wukong.common.model.BaseResult;
import com.wukong.common.model.UserVO;

public class UserController implements IUserController {
    @Override
    public BaseResult<UserVO> getById(int id) {
     	if (id < 0) {
            throw new IllegalArgumentException("id不能为负数");
        }
        return BaseResult.success(new UserVO());
    }
}
