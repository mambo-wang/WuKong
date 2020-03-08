package com.wukong.common.request;

import com.wukong.common.concurrent.VDIExecutorServices;
import com.wukong.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service("requestMgr")
public class RequestMgrImpl implements RequestMgr {


    private ExecutorService ioService = VDIExecutorServices.get().getIoBusyService();

    private Map<String, RequestReceiver> listeners = new ConcurrentHashMap<>();

    /**
     * 变种的观察者模式实现简单的网关
     * 注册观察者
     * @param method 请求唯一标识
     * @param requestReceiver
     */
    @Override
    public void register(String method, RequestReceiver requestReceiver) {

        if(CollectionUtils.isEmpty(listeners.entrySet())){
            listeners = new ConcurrentHashMap<>();
        }
        listeners.put(method, requestReceiver);
    }

    /**
     *
     * @param method
     * @param data
     * @return 结果
     * @throws InvalidKeySpecException
     * @throws NoSuchMethodException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    @Override
    public String postRequest(String method, Map<String, String> data) throws InvalidKeySpecException, NoSuchMethodException, NoSuchAlgorithmException, IOException {

        RequestReceiver requestReceiver = listeners.get(method);
        if (Objects.isNull(requestReceiver)) {
            log.info("empty listner");
            throw new BusinessException("500","找不到对应的方法");
        }
        return requestReceiver.handleRequest(data);
    }
}
