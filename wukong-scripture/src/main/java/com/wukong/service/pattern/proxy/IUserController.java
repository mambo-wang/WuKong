package com.wukong.service.pattern.proxy;

import com.wukong.common.model.BaseResult;
import com.wukong.common.model.UserVO;

public interface IUserController {
    public BaseResult<UserVO> getById(int id);
}
