package com.wukong.provider.service;

import com.wukong.common.model.AddScoreDTO;
import com.wukong.common.model.UserVO;
import com.wukong.provider.controller.vo.LoginVO;
import com.wukong.provider.dto.UserEditDTO;
import com.wukong.provider.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {

    List<UserVO> queryAll();

    UserVO findById(Long id);

    UserVO findByUsername(String username);

    UserVO addUser(UserEditDTO userEditDTO);

    void modifyUser(UserEditDTO userEditDTO);

    void removeUser(List<Long> ids);

    String login(HttpServletResponse response, LoginVO loginVo);

    User getByToken(HttpServletResponse response, String token);

    void addScore(AddScoreDTO addScoreDTO);

}
