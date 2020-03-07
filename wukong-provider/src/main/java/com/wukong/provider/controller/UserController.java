package com.wukong.provider.controller;

import com.wukong.common.model.BaseResult;
import com.wukong.common.model.UserVO;
import com.wukong.provider.dto.UserEditDTO;
import com.wukong.provider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
