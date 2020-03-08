package com.wukong.common.request;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public interface RequestMgr {

    void register(String method, RequestReceiver requestReceiver);

    String postRequest(String method, Map<String, String> data) throws InvalidKeySpecException, NoSuchMethodException, NoSuchAlgorithmException, IOException;

}
