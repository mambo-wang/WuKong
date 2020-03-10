package com.wukong.consumer.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wukong.common.annotations.AccessLimit;
import com.wukong.common.contants.Constant;
import com.wukong.common.exception.BusinessException;
import com.wukong.common.exception.CommonErrorCode;
import com.wukong.common.model.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * aop采集日志(接口请求参数，接口调用时间)
 *
 * @author wangbao6
 * @date 2019/7/31 17:50
 */
@Aspect
@Component
@Slf4j
public class RestEasyAop {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private static String[] types = {"java.lang.Integer", "java.lang.Double",
            "java.lang.Float", "java.lang.Long", "java.lang.Short",
            "java.lang.Byte", "java.lang.Boolean", "java.lang.Char",
            "java.lang.String", "int", "double", "long", "short", "byte",
            "boolean", "char", "float"};

    /**
     * 拦截所有带AccessLimit注解的方法
     */
    @Pointcut("@annotation(com.wukong.common.annotations.AccessLimit)")
    public void accessAnno(){

    }

    @Before("accessAnno()")
    public void doBefore(JoinPoint point) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        UserVO user = getUser(request, response);

        Signature signature = point.getSignature();//此处joinPoint的实现类是MethodInvocationProceedingJoinPoint
        MethodSignature methodSignature = (MethodSignature) signature;//获取参数名
        AccessLimit accessLimit = methodSignature.getMethod().getAnnotation(AccessLimit.class);

        if(accessLimit == null) {
            throw new BusinessException(CommonErrorCode.ACCESS_LIMIT_REACHED.getCode(), CommonErrorCode.ACCESS_LIMIT_REACHED.getMsg());
        }
        int seconds = accessLimit.seconds();
        int maxCount = accessLimit.maxCount();
        boolean needLogin = accessLimit.needLogin();
        String key = request.getRequestURI();
        if(needLogin) {
            if(user == null) {
                render(response, CommonErrorCode.SESSION_ERROR);
            }
            key += "_" + user.getUsername();
        }else {
            //do nothing
        }
        String count = stringRedisTemplate.opsForValue().get(Constant.RedisKey.KEY_ACCESS + key);
        if(count  == null) {
            stringRedisTemplate.opsForValue().increment(Constant.RedisKey.KEY_ACCESS+ key, 1);

            stringRedisTemplate.expire(Constant.RedisKey.KEY_ACCESS+ key, seconds, TimeUnit.SECONDS);
        }else if(Integer.valueOf(count) < maxCount) {
            stringRedisTemplate.opsForValue().increment(Constant.RedisKey.KEY_ACCESS+ key, 1);
        }else {
//            render(response, CommonErrorCode.ACCESS_LIMIT_REACHED);
            throw new BusinessException(CommonErrorCode.ACCESS_LIMIT_REACHED.getCode(), CommonErrorCode.ACCESS_LIMIT_REACHED.getMsg());
        }
    }


    private void render(HttpServletResponse response, CommonErrorCode cm)throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str  = JSON.toJSONString(cm);
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private UserVO getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter("token");
        String cookieToken = getCookieValue(request, "token");
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return getByToken(response, token);
    }

    public UserVO getByToken(HttpServletResponse response, String token) {

        if(StringUtils.isEmpty(token)){
            return null ;
        }

        UserVO user = JSONObject.parseObject(String.valueOf(stringRedisTemplate.opsForHash().get(Constant.RedisKey.KEY_TOKEN, token)), UserVO.class);
        if(user!=null) {
            stringRedisTemplate.opsForHash().put(Constant.RedisKey.KEY_TOKEN, token, JSONObject.toJSONString(user));
            Cookie cookie = new Cookie("token", token);
            //设置有效期
            cookie.setMaxAge(20000);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return user ;
    }

    private String getCookieValue(HttpServletRequest request, String cookiName) {
        Cookie[]  cookies = request.getCookies();
        if(cookies == null || cookies.length <= 0){
            return null;
        }
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(cookiName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private String getMethodInfo(JoinPoint point) {
        String[] parameterNames = ((MethodSignature) point.getSignature()).getParameterNames();
        StringBuilder sb = null;
        if (Objects.nonNull(parameterNames)) {
            sb = new StringBuilder();
            for (int i = 0; i < parameterNames.length; i++) {
                // 对参数解析(参数有可能为基础数据类型，也可能为一个对象，若为对象则需要解析对象中变量名以及值)
                String value = "";
                if (point.getArgs()[i] == null) {
                    value = "null";
                } else {
                    // 获取对象类型
                    String typeName = point.getArgs()[i].getClass().getTypeName();
                    boolean flag = false;
                    for (String t : types) {
                        //1 判断是否是基础类型
                        if (t.equals(typeName)) {
                            value = point.getArgs()[i].toString();
                            flag = true;
                        }
                        if (flag) {
                            break;
                        }
                    }
                    if (!flag) {
                        //2 通过反射获取实体类属性
                        value = getFieldsValue(point.getArgs()[i]);
                    }
                }
                if(value.length() > 1024){
                    StringUtils.substring(value, 0, 1023);
                }
                sb.append(value);
            }
        }
        sb = sb == null ? new StringBuilder() : sb;
        String info = String.format("request param: %s", sb.toString());
        return info;
    }

    /**
     * 解析实体类，获取实体类中的属性
     */
    public static String getFieldsValue(Object obj) {
        //通过反射获取所有的字段，getFileds()获取public的修饰的字段
        //getDeclaredFields获取private protected public修饰的字段
        Field[] fields = obj.getClass().getDeclaredFields();
        String typeName = obj.getClass().getTypeName();
        for (String t : types) {
            if (t.equals(typeName)) {
                return "";
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Field f : fields) {
            //在反射时能访问私有变量
            f.setAccessible(true);
            try {
                for (String str : types) {
                    //这边会有问题，如果实体类里面继续包含实体类，这边就没法获取。
                    //其实，我们可以通递归的方式去处理实体类包含实体类的问题。
                    if (f.getType().getName().equals(str)) {
                        sb.append(f.getName() + " : " + f.get(obj) + ", ");
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
