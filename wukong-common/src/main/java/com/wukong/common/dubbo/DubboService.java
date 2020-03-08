package com.wukong.common.dubbo;

import com.wukong.common.model.UserVO;

public interface DubboService {

    UserVO getUser(String username);

}
