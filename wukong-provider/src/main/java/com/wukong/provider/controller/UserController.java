package com.wukong.provider.controller;

import com.wukong.common.model.BaseResult;
import com.wukong.common.model.UserVO;
import com.wukong.provider.config.interceptor.AccessLimit;
import com.wukong.provider.controller.vo.LoginVO;
import com.wukong.provider.dto.UserEditDTO;
import com.wukong.provider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    BaseResult<List<UserVO>> queryAll(){
        return BaseResult.success(userService.queryAll());
    }

    @AccessLimit(seconds = 60, maxCount = 5, needLogin = true)
    @GetMapping("/{id}")
    BaseResult<UserVO> findById(@PathVariable(name = "id") Long id){
        return BaseResult.success(userService.findById(id));
    }

    @GetMapping("/byUsername")
    BaseResult<UserVO> findByUsername(@RequestParam(name = "username") String username){
        return BaseResult.success(userService.findByUsername(username));
    }

    @PostMapping
    BaseResult addUser(@RequestBody UserEditDTO userEditDTO){
        userService.addUser(userEditDTO);
        return BaseResult.success(null);
    }

    @PutMapping
    BaseResult modifyUser(@RequestBody UserEditDTO userEditDTO){
        userService.modifyUser(userEditDTO);
        return BaseResult.success(null);
    }

    @GetMapping("/delete")
    BaseResult removeUser(@RequestParam(name = "ids") List<Long> ids){
        userService.removeUser(ids);
        return BaseResult.success(null);
    }

    @PostMapping("/login")
    BaseResult removeUser(@RequestBody LoginVO loginVO, HttpServletResponse response){
        String token = userService.login(response, loginVO);
        return BaseResult.success(token);
    }
}
