package com.wukong.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wukong.common.exception.BusinessException;
import com.wukong.common.model.AddScoreDTO;
import com.wukong.common.model.UserVO;
import com.wukong.provider.config.redis.RedisConfig;
import com.wukong.provider.controller.vo.LoginVO;
import com.wukong.provider.dto.UserEditDTO;
import com.wukong.provider.entity.User;
import com.wukong.provider.mapper.UserMapper;
import com.wukong.provider.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<UserVO> queryAll() {
        log.info("走的数据库");
        return userMapper.getAll().stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Cacheable(value = "redis-user",key = "'user' + #id", condition = "#id>5")
    @Override
    public UserVO findById(Long id) {
        log.info("走的数据库");
        User user = userMapper.selectByPrimaryKey(id);
        if(Objects.isNull(user)){
            log.error("can not find an user who's id is {}", id);
        }
        return convertToVO(userMapper.selectByPrimaryKey(id));
    }

    @Override
    public UserVO findByUsername(String username) {
        log.info("走的数据库");
        return convertToVO(userMapper.selectByUsername(username));
    }

    @Override
    public UserVO addUser(UserEditDTO userEditDTO) {
        userMapper.insert(convertToDO(userEditDTO));
        return findByUsername(userEditDTO.getUsername());
    }

    @CacheEvict(value = "redis-user", key = "'user' + #userEditDTO.id")
    @Override
    public void modifyUser(UserEditDTO userEditDTO) {
        userMapper.updateByPrimaryKey(convertToDO(userEditDTO));
    }

    @Override
    public void removeUser(List<Long> ids) {
        ids.forEach(id -> userMapper.deleteByPrimaryKey(id));
    }

    @Override
    public String login(HttpServletResponse response, LoginVO loginVo) {
        if(loginVo ==null){
            throw  new BusinessException("", "失败");
        }

        String username =loginVo.getUsername();
        String password =loginVo.getPassword();
        User user = userMapper.selectByUsername(username);
        if(user == null || !password.equals(user.getPassword())) {
            throw new BusinessException("","失败");
        }

        //生成cookie 将session返回游览器 分布式session
        String token= UUID.randomUUID().toString();
        addCookie(response, token, user);
        return token;
    }

    @Override
    public User getByToken(HttpServletResponse response, String token) {

        if(StringUtils.isEmpty(token)){
            return null ;
        }
        User user = JSONObject.parseObject(String.valueOf(stringRedisTemplate.opsForHash().get(RedisConfig.REDIS_KEY_TOKEN, token)), User.class);
        if(user!=null) {
            addCookie(response, token, user);
        }
        return user ;
    }

    @Transactional
    @Override
    public void addScore(AddScoreDTO addScoreDTO) {
        User user = userMapper.selectByUsername(addScoreDTO.getUsername());
        user.setScore(user.getScore() + addScoreDTO.getScoreToAdd());
        userMapper.updateByPrimaryKey(user);
    }

    private void addCookie(HttpServletResponse response, String token, User user) {
        stringRedisTemplate.opsForHash().put(RedisConfig.REDIS_KEY_TOKEN, token, JSONObject.toJSONString(user));
        Cookie cookie = new Cookie("token", token);
        //设置有效期
        cookie.setMaxAge(20000);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private UserVO convertToVO(User user){
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO, "password");
        log.info("convertToVO user to uservo success");
        return userVO;
    }

    private User convertToDO(UserEditDTO userEditDTO){
        User user = new User();
        BeanUtils.copyProperties(userEditDTO, user);
        return user;
    }
}
