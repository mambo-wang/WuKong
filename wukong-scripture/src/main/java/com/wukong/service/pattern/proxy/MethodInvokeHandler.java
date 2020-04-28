package com.wukong.service.pattern.proxy;

import com.wukong.common.model.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodInvokeHandler implements InvocationHandler {
    private static final Logger log = LoggerFactory.getLogger(MethodInvokeHandler.class);
    /**
     * 被代理的目标对象
     */
    private Object target;

    public MethodInvokeHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 大哥，我现在准备要调用 method 这个方法了，参数是 args
        before(method, args);
        Object result = null;
        Throwable throwable = null;
        try {
            // 通过反射调用目标方法
            result = method.invoke(target, args);
            // 大哥，method 这个方法，我用参数 args 调用之后正常返回了，结果是 result
            afterReturning(method, args, result);
            return result;
        } catch (Throwable t) {
        	throwable = t;
            // 大哥，method 这个方法，我用参数 args 调用之后抛异常了，异常是 t
            return afterThrowing(method, args, t);
        } finally {
            // 大哥，method 这个方法执行完了，执行状态都在这，您看看还有什么吩咐
            after(method, args, result, throwable);
        }
    }

    public void before(Method method, Object[] args) {
        String methodName = method.getDeclaringClass().getName() + "." + method.getName();
        log.info("方法调用开始，方法: {}, 参数: {}", methodName, Arrays.toString(args));
    }

    public void afterReturning(Method method, Object[] args, Object result) {
        String methodName = method.getDeclaringClass().getName() + "." + method.getName();
        log.debug("方法调用成功, 方法: {}({}), 结果: {}", methodName, Arrays.toString(args), result);
    }

    public Object afterThrowing(Method method, Object[] args, Throwable t) {
        String methodName = method.getDeclaringClass().getName() + "." + method.getName();
        log.error("方法调用发生异常, 方法: {}({})", methodName, Arrays.toString(args), t);
        // 统一异常返回值
        return BaseResult.fail("500", "方法调用发生异常");
    }

    public void after(Method method, Object[] args, Object result, Throwable t) {
        String methodName = method.getDeclaringClass().getName() + "." + method.getName();
        log.info("方法调用结束，方法: {}, 是否成功: {}", methodName, t == null);
    }
}
