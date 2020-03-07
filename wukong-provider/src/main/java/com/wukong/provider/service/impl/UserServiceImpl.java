package com.wukong.provider.service.impl;

import com.wukong.common.model.UserVO;
import com.wukong.provider.dto.UserEditDTO;
import com.wukong.provider.entity.User;
import com.wukong.provider.mapper.UserMapper;
import com.wukong.provider.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("userService")
@Slf4j
@CacheConfig(cacheNames = "user")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Cacheable
    @Override
    public List<UserVO> queryAll() {
        log.info("走的数据库");
        return userMapper.getAll().stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Cacheable(key = "#result.username", condition = "#result!=null")
    @Override
    public UserVO findById(Long id) {
        log.info("走的数据库");
        User user = userMapper.selectByPrimaryKey(id);
        if(Objects.isNull(user)){
            log.error("can not find an user who's id is {}", id);
        }
        return convertToVO(userMapper.selectByPrimaryKey(id));
    }

    @Cacheable(key = "#username", condition = "#result!=null")
    @Override
    public UserVO findByUsername(String username) {
        log.info("走的数据库");
        return convertToVO(userMapper.selectByUsername(username));
    }

    /**
     * todo 貌似缓存没加进去
     * @param userEditDTO
     */
    @CachePut(key = "#result.username")
    @Override
    public UserVO addUser(UserEditDTO userEditDTO) {
        userMapper.insert(convertToDO(userEditDTO));
        return findByUsername(userEditDTO.getUsername());
    }

    @CacheEvict(key = "#userEditDTO.username")
    @Override
    public void modifyUser(UserEditDTO userEditDTO) {
        userMapper.updateByPrimaryKey(convertToDO(userEditDTO));
    }

    @CacheEvict(allEntries = true)
    @Override
    public void removeUser(List<Long> ids) {
        ids.forEach(id -> userMapper.deleteByPrimaryKey(id));
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
