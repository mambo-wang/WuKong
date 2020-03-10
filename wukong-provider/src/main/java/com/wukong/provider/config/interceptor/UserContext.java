package com.wukong.provider.config.interceptor;

import com.wukong.common.model.UserVO;
import com.wukong.provider.entity.User;

public class UserContext {
	
	private static ThreadLocal<UserVO> userHolder = new ThreadLocal<UserVO>();
	
	public static void setUser(UserVO user) {
		userHolder.set(user);
	}
	
	public static UserVO getUser() {
		return userHolder.get();
	}

	public static void removeUser() {
		userHolder.remove();
	}

}
