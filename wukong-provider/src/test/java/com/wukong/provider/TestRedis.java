package com.wukong.provider;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {com.wukong.provider.DubboProviderApplication.class})
public class TestRedis {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
	@Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() throws Exception {
        stringRedisTemplate.opsForValue().set("aaa", "111");
        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
    }
    
    @Test
    public void testObj() throws Exception {
    }

    @Test
    public void geoTest() {
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
        GeoResults<RedisGeoCommands.GeoLocation<String>> res = redisTemplate.opsForGeo().radius("person", "xiaoming", 5000);

        for (int i = 1; i < res.getContent().size(); i++) {
            System.out.println("小明附近的人：" + res.getContent().get(i));
        }
    }

}