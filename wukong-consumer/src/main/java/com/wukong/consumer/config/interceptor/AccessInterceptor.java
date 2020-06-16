package com.wukong.consumer.config.interceptor;

import com.wukong.common.annotations.AccessLimit;
import com.wukong.common.contants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AccessInterceptor  implements HandlerInterceptor {

	@Resource
	private StringRedisTemplate stringRedisTemplate;//todo 注入的bean为null

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		log.info("pre handle exe");
		if (handler instanceof HandlerMethod) {
			log.info("pre handle handler method");
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			if (!method.isAnnotationPresent(AccessLimit.class)) {
				return true;
			}
			AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
			int seconds = accessLimit.seconds();
			int maxCount = accessLimit.maxCount();
			String key = request.getRequestURI();
			if(stringRedisTemplate == null){
				log.error("stringRedisTemplate 是空的");
			}
			String count = stringRedisTemplate.opsForValue().get(Constant.RedisKey.KEY_ACCESS + key);
			log.info("pre handle count is {}", count);
			if(count == null) {
				stringRedisTemplate.opsForValue().increment(Constant.RedisKey.KEY_ACCESS+ key, 1);
				stringRedisTemplate.expire(Constant.RedisKey.KEY_ACCESS+ key, seconds, TimeUnit.SECONDS);
			}else if(Integer.valueOf(count) < maxCount) {
				stringRedisTemplate.opsForValue().increment(Constant.RedisKey.KEY_ACCESS+ key, 1);
			}else {
				log.error("限流。。。");
				return false;
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}
}
