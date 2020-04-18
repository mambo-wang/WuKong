package com.wukong.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.wukong.common.contants.Constant;
import com.wukong.common.dubbo.DubboTestService;
import com.wukong.common.exception.BusinessException;
import com.wukong.common.model.UserVO;
import com.wukong.common.utils.ExcelTool;
import com.wukong.provider.controller.vo.LoginVO;
import com.wukong.provider.controller.vo.UserImportVO;
import com.wukong.provider.controller.vo.UserEditVO;
import com.wukong.provider.dto.UserImportDTO;
import com.wukong.provider.entity.User;
import com.wukong.provider.mapper.UserMapper;
import com.wukong.provider.service.MailService;
import com.wukong.provider.service.UserService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.rpc.cluster.loadbalance.RoundRobinLoadBalance;
import org.apache.dubbo.rpc.cluster.support.FailfastCluster;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService, InitializingBean {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name = "redisTemplate")
    ValueOperations<String, UserVO> opsForValue;

    @Resource(name = "redisTemplate")
    HashOperations<String, String, UserVO> opsForHash;


    @Autowired
    private MailService mailService;

    @Reference(retries = 2, timeout = 10000, loadbalance = RoundRobinLoadBalance.NAME, cluster = FailfastCluster.NAME)
    private DubboTestService dubboTestService;

    //创建一个插入对象为四百，误报率为0.4%的布隆过滤器
    private BloomFilter<CharSequence> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.forName("utf-8")), 400, 0.004);


    @Override
    public List<UserVO> queryAll() {
        return userMapper.getAll().stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Cacheable(value = "redis-user",key = "'user' + #id", condition = "#id>0")
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
        UserVO user = opsForHash.get(Constant.RedisKey.KEY_USER_USERNAME, username);
        if(user != null){
            return user;
        } else if(bloomFilter.mightContain(username)){
            //竞争锁
            Boolean result = redisTemplate.opsForValue().setIfAbsent("lock_username", "1", 5, TimeUnit.SECONDS);
            if(result){
                UserVO userVO = convertToVO(userMapper.selectByUsername(username));
                opsForHash.put(Constant.RedisKey.KEY_USER_USERNAME, username, userVO);
                return userVO;
            }
        }
        throw new BusinessException("500","布隆过滤器说用户不存在，请稍后查询");
    }

    @Override
    public UserVO addUser(UserEditVO userEditVO) {
        User user = convertToDO(userEditVO);
        userMapper.insert(user);
        bloomFilter.put(user.getUsername());
        return findById(user.getId());
    }

    @CacheEvict(value = "redis-user", key = "'user' + #userEditVO.id")
    @Override
    public void modifyUser(UserEditVO userEditVO) {
        opsForHash.delete(Constant.RedisKey.KEY_USER_USERNAME, userEditVO.getUsername());
        userMapper.updateByPrimaryKey(convertToDO(userEditVO));
    }

    @Override
    public void removeUser(List<Long> ids) {//todo 删除缓存
        ids.forEach(this::removeUser);
    }

    @CacheEvict(value = "redis-user", key = "'user' + #.id")
    public void removeUser(Long id){
        User user = userMapper.selectByPrimaryKey(id);
        opsForHash.delete(Constant.RedisKey.KEY_USER_USERNAME, user.getUsername());
        userMapper.deleteByPrimaryKey(id);
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
        addCookie(response, token, convertToVO(user));
        return token;
    }

    @Override
    public UserVO getByToken(HttpServletResponse response, String token) {

        if(StringUtils.isEmpty(token)){
            return null ;
        }
        UserVO user = opsForValue.get(Constant.RedisKey.KEY_TOKEN + token);
        if(user!=null) {
            addCookie(response, token, user);
        }
        return user ;
    }

    @Transactional
    @Override
    public void addScore(Long userId, Integer score) {
        User user = userMapper.selectByPrimaryKey(userId);
        log.info("------add score method invoke {}", System.currentTimeMillis());
        user.setScore(user.getScore() + score);
        log.info("------before db method invoke {}", System.currentTimeMillis());
        userMapper.updateByPrimaryKey(user);
        log.info("------after db method invoke {}", System.currentTimeMillis());

//        mailService.sendSimpleMail("mambo1991@163.com", "【悟空秒杀】积分增加通知","亲爱的" + user.getName() + "恭喜您下单成功，"
//                + score+"积分已到账,目前共有积分" + user.getScore()+"\n--悟空商城");
    }

    @Override
    public int reduceBalance(Long userId, BigDecimal price) {
        return userMapper.reduceBalance(userId, price);
    }

    @Override
    public Workbook createExcelTemplate() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        List<String> commonHeaders = Arrays.asList("姓名|（必填）","用户名|（必填）","收货地址|（必填）","电话|（选填）","邮箱|（必填）");
        ExcelTool.createSheetWithRichTextHeader(workbook, "template", ExcelTool.convertToRichText(workbook, commonHeaders));
        ExcelTool.MergeHeader mergeHeader = new ExcelTool.MergeHeader(ExcelTool.convertToRichText(workbook, "请按要求如实填写基本信息"), 5);
        ExcelTool.createVariableCellSizeHeaderWithRichText(workbook, "template", Arrays.asList(mergeHeader));
        ExcelTool.createTitleRow(workbook, "template", "用户信息表", 5);
        return workbook;
    }

    public UserImportVO uploadExcel(MultipartFile file) throws InstantiationException, IllegalAccessException, IOException {
        Workbook workbook = ExcelTool.createWorkbook(file.getOriginalFilename(), file.getInputStream());
        if(Objects.isNull(workbook)){
            throw new BusinessException("300001", "文件读取失败");
        }
        List<UserImportDTO> userImportDTOS = new ExcelTool<UserImportDTO>().getDataFromExcel(workbook, UserImportDTO.class);

        List<UserImportDTO> errorData = userImportDTOS.stream().filter(this::checkDataOfVillageIfError).collect(Collectors.toList());

        if(CollectionUtils.isEmpty(errorData)){
            //导入
            importData(userImportDTOS);
            return new UserImportVO(true, userImportDTOS);
        } else {
            return new UserImportVO(false, errorData);
        }

    }

    @Override
    public boolean checkPwd(Long userId, String password) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null || !password.equals(user.getPassword())) {
            return false;
        }
        return true;
    }

    @GlobalTransactional
    @Override
    public void testSeata(String username, String phoneNumber, Long goods) {

        User user = this.userMapper.selectByUsername(username);
        user.setPhoneNumber(phoneNumber);
        userMapper.updateByPrimaryKey(user);

        dubboTestService.reduceStock(goods);
    }

    private void importData(List<UserImportDTO> userImportDTOS){

        for (UserImportDTO userImportDTO : userImportDTOS) {
            User user = new User();
            BeanUtils.copyProperties(userImportDTO, user);
            user.setScore(0);
            user.setPassword("123456");
            userMapper.insert(user);
        }
    }

    /**
     * 如果数据不合法，返回true
     * @param userImportDTO
     * @return
     */
    private boolean checkDataOfVillageIfError(UserImportDTO userImportDTO) {
        if(StringUtils.isEmpty(userImportDTO.getName()) ||
                StringUtils.isEmpty(userImportDTO.getUsername()) ||
                StringUtils.isEmpty(userImportDTO.getAddress()) ||
                StringUtils.isEmpty(userImportDTO.getPhoneNumber())){
            return true;
        }
        return false;
    }

    private void addCookie(HttpServletResponse response, String token, UserVO user) {
        opsForValue.set(Constant.RedisKey.KEY_TOKEN + token, user, 30 , TimeUnit.MINUTES);
        Cookie cookie = new Cookie("token", token);
        //设置有效期
        cookie.setMaxAge(30 * 1000 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private UserVO convertToVO(User user){
        if(Objects.isNull(user)){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO, "password");
        log.info("convertToVO user to uservo success");
        return userVO;
    }

    private User convertToDO(UserEditVO userEditVO){
        User user = new User();
        if(Objects.nonNull(userEditVO.getId())){
            user = userMapper.selectByPrimaryKey(userEditVO.getId());
        }
        BeanUtils.copyProperties(userEditVO, user, "score");
        return user;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<User> users = userMapper.getAll();
        users.forEach(user -> bloomFilter.put(user.getUsername()));
    }
}
