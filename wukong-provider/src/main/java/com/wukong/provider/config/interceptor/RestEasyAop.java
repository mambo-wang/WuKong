package com.wukong.provider.config.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * aop采集日志(接口请求参数，接口调用时间)
 *
 * @author wangbao6
 * @date 2019/7/31 17:50
 */
@Aspect
@Component
public class RestEasyAop {


    @Resource
    private OperationLogService operationLogService;

    private static String[] types = {"java.lang.Integer", "java.lang.Double",
            "java.lang.Float", "java.lang.Long", "java.lang.Short",
            "java.lang.Byte", "java.lang.Boolean", "java.lang.Char",
            "java.lang.String", "int", "double", "long", "short", "byte",
            "boolean", "char", "float"};

    private static final HikGaLogger log = HikGaLoggerFactory.getLogger(RestEasyAop.class);

    public static ThreadLocal<String> usernameThreadLocal = new ThreadLocal<>();
    public static ThreadLocal<String> imeiThreadLocal = new ThreadLocal<>();
    public static ThreadLocal<String> ipThreadLocal = new ThreadLocal<>();

    /**
     * 拦截所有手机app访问的接口
     */
    @Pointcut("execution(* com.hikvision.pbg.jc.common.modules.resteasy.rs.*.*(..)) ")
    public void restEasyMethodPointcut() {
    }

    /**
     * 拦截所有web端访问的controller方法
     */
    @Pointcut("execution(* com.hikvision.pbg.jc.common.modules.controller.*.*(..)) ")
    public void controllerMethodPointcut() {
    }


    @Pointcut("restEasyMethodPointcut() || controllerMethodPointcut()")
    private void methodPointcut(){}


    /**
     * 拦截所有带RestEasyLog注解的方法，记录成功或失败日志
     */
    @Pointcut("@annotation(com.hikvision.pbg.jc.common.modules.common.interceptor.RestEasyLog)")
    public void logAnnotation(){

    }

    @AfterReturning("logAnnotation()")
    public void doBefore(JoinPoint point) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String username = request.getHeader("Username");
        if(StringUtils.isEmpty(username)){
            username = "system";
        }
        Signature signature = point.getSignature();//此处joinPoint的实现类是MethodInvocationProceedingJoinPoint

        MethodSignature methodSignature = (MethodSignature) signature;//获取参数名
        RestEasyLog restEasyLogAnno = methodSignature.getMethod().getAnnotation(RestEasyLog.class);

        String desc = restEasyLogAnno.description() + getMethodInfo(point);
        Integer category = restEasyLogAnno.category();
        log.info("--------------add success log-----------");
        operationLogService.addSuccessLog(username, category, desc);
    }

    @AfterThrowing(pointcut = "logAnnotation()", throwing= "error")
    public void afterThrowingAdvice(JoinPoint point, Throwable error){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String username = request.getHeader("Username");
        if(StringUtils.isEmpty(username)){
            username = "system";
        }
        Signature signature = point.getSignature();//此处joinPoint的实现类是MethodInvocationProceedingJoinPoint
        MethodSignature methodSignature = (MethodSignature) signature;//获取参数名
        RestEasyLog restEasyLogAnno = methodSignature.getMethod().getAnnotation(RestEasyLog.class);

        String desc = restEasyLogAnno.description() + getMethodInfo(point);
        Integer category = restEasyLogAnno.category();
        log.info("--------------add fail log-----------");
        operationLogService.addFailureLog(username, category, desc, error.getMessage());
    }


    @AfterThrowing(pointcut = "restEasyMethodPointcut()", throwing= "error")
    public void controller(JoinPoint point, Throwable error) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String info = String.format("\n =======> request uri: %s  %s", request.getRequestURI(), getMethodInfo(point));
        log.error("{}, ====> error msg: {}", info, error.getMessage());
    }

    @Before("methodPointcut()")
    public void beforeAppReq(JoinPoint point){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String username = request.getHeader("Username");
        if(StringUtils.isNotEmpty(username)){
            usernameThreadLocal.set(username);
        }
        String imei = request.getHeader("imei");
        if(StringUtils.isNotEmpty(imei)){
            imeiThreadLocal.set(imei);
        }
        String ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip)){
            ipThreadLocal.set(ip);
        }
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
