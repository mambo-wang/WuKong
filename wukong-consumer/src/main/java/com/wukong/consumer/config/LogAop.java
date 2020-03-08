package com.wukong.consumer.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wukong.common.dubbo.LogService;
import com.wukong.common.model.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

/**
 * aop采集日志(接口请求参数，接口调用时间)
 *
 * @author wangbao6
 * @date 2019/7/31 17:50
 */
@Aspect
@Component
@Slf4j
public class LogAop {

    @Reference
    private LogService logService;

    /**
     * 拦截所有web端访问的controller方法
     */
    @Pointcut("execution(* com.wukong.consumer.controller.*.*(..)) ")
    public void controllerMethodPointcut() {

    }


    @AfterThrowing(pointcut = "controllerMethodPointcut()", throwing= "error")
    public void controller(JoinPoint point, Throwable error) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String info = String.format("\n =======> request uri: %s", request.getRequestURI());
        log.error("{}, ====> error msg: {}", info, error.getMessage());
        logService.addFailureLog(OperationLog.MODULE_CONSUMER, request.getRequestURI() + " " + point.getSignature().toString(), error.toString(), "system");
    }

    @AfterReturning(pointcut="controllerMethodPointcut()", returning="retVal")
    public void afterReturningAdvice(JoinPoint jp, Object retVal){
        System.out.println("[afterReturningAdvice] Method Signature: "  + jp.getSignature());
        System.out.println("[afterReturningAdvice] Returning: " + retVal.toString() );
        logService.addSuccessLog(OperationLog.MODULE_CONSUMER, jp.getSignature().toString(), retVal.toString(),"admin");
    }


}
