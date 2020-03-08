package com.wukong.provider.config.interceptor;

import com.alibaba.fastjson.JSON;
import com.wukong.common.exception.CommonErrorCode;
import com.wukong.provider.config.redis.RedisConfig;
import com.wukong.provider.entity.User;
import com.wukong.provider.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;


/**
 * todo 没走进来
 */
@Service
public class AccessInterceptor  extends HandlerInterceptorAdapter {

	private static Logger logger = LoggerFactory.getLogger(AccessInterceptor.class);

	@Autowired
	UserService userService;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		/**
		 * 获取调用 获取主要方法
		 */
		if(handler instanceof HandlerMethod) {
			logger.info("打印拦截方法handler ：{} ",handler);
			HandlerMethod hm = (HandlerMethod)handler;
			//根据token获取user信息放入threadLocal
			User user = getUser(request, response);
			UserContext.setUser(user);

			AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
			if(accessLimit == null) {
				return true;
			}
			int seconds = accessLimit.seconds();
			int maxCount = accessLimit.maxCount();
			boolean needLogin = accessLimit.needLogin();
			String key = request.getRequestURI();
			if(needLogin) {
				if(user == null) {
					render(response, CommonErrorCode.SESSION_ERROR);
					return false;
				}
				key += "_" + user.getUsername();
			}else {
				//do nothing
			}
			String count = stringRedisTemplate.opsForValue().get(RedisConfig.REDIS_KEY_ACCESS + key);
	    	if(count  == null) {
				stringRedisTemplate.opsForValue().increment(RedisConfig.REDIS_KEY_ACCESS+ key, 1);

				stringRedisTemplate.expire(RedisConfig.REDIS_KEY_ACCESS+ key, seconds, TimeUnit.SECONDS);
			}else if(Integer.valueOf(count) < maxCount) {
	    		 stringRedisTemplate.opsForValue().increment(RedisConfig.REDIS_KEY_ACCESS+ key, 1);
	    	}else {
	    		render(response, CommonErrorCode.ACCESS_LIMIT_REACHED);
	    		return false;
	    	}
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		super.afterCompletion(request, response, handler, ex);
		UserContext.removeUser();
	}

	private void render(HttpServletResponse response, CommonErrorCode cm)throws Exception {
		response.setContentType("application/json;charset=UTF-8");
		OutputStream out = response.getOutputStream();
		String str  = JSON.toJSONString(cm);
		out.write(str.getBytes("UTF-8"));
		out.flush();
		out.close();
	}

	private User getUser(HttpServletRequest request, HttpServletResponse response) {
		String paramToken = request.getParameter("token");
		String cookieToken = getCookieValue(request, "token");
		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		return userService.getByToken(response, token);
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
}
