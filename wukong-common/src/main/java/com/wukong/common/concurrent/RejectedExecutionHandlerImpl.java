package com.wukong.common.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * WuKong RejectedExecutionHandler自定义实现类。当任务无法执行时，触发此handler。
 */
@Slf4j
public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        log.warn("queue full, {} rejected, {}", String.valueOf(r), String.valueOf(executor));
    }
}

