package com.wukong.common.request;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@FunctionalInterface
public interface RequestReceiver {

    /** 处理请求的方法 */
    String handleRequest(Map<String, String> data) throws NoSuchAlgorithmException, NoSuchMethodException, InvalidKeySpecException, IOException;

}
