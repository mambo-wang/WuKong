package com.wukong.provider.controller;

import com.wukong.common.model.BaseResult;
import com.wukong.provider.config.rest.RestConnection;
import com.wukong.provider.service.MailService;
import com.wukong.provider.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(value = "测试控制器")
@RestController
@RequestMapping("/test")
public class TestController {


    @Autowired
    private RestConnection restConnection;

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @GetMapping("/seata")
    public BaseResult testSeata(@RequestParam(name = "username")String username,
                                @RequestParam(name = "phone")String phoneNumber,
                                @RequestParam(name = "goods")Long goods){
        userService.testSeata(username, phoneNumber, goods);
        return BaseResult.success(null);
    }

    @GetMapping
    public BaseResult hello(){

        ResponseEntity<BaseResult> resultResponseEntity =  restConnection.get("/logs", ParameterizedTypeReference.forType(BaseResult.class));

        return resultResponseEntity.getBody();
    }

    /**
     * sendAttachmentsMail
     */
    @PostMapping
    public  BaseResult mail(){

        mailService.sendSimpleMail("mambo1991@163.com", "测试", "测试事实上司是");
        return BaseResult.success(null);
    }

    @GetMapping("/geo")
    public void testGeo(){
        Map<String, Point> map = new HashMap<>();
        // 添加小明的位置
        map.put("xiaoming", new Point(116.404269, 39.913164));
        // 添加小红的位置
        map.put("xiaohong", new Point(116.36, 39.922461));
        // 添加小美的位置
        map.put("xiaomei", new Point(116.499705, 39.874635));
        // 添加小二
        map.put("xiaoer", new Point(116.193275, 39.996348));
        redisTemplate.opsForGeo().add("person", map);
        // 查询小明和小红的直线距离
        System.out.println("小明和小红相距：" + redisTemplate.opsForGeo().distance("person", "xiaoming",
                "xiaohong"));


        // 查询小明附近 5 公里的人
        GeoResults<RedisGeoCommands.GeoLocation<String>> res = redisTemplate.opsForGeo().radius("person", "xiaoming", 5);

        for (int i = 1; i < res.getContent().size(); i++) {
            System.out.println("小明附近的人：" + res.getContent().get(i).getContent());
        }
    }

}
