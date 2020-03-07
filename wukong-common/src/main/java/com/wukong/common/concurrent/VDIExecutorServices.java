package com.wukong.common.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 任务执行的线程池
 */
@Slf4j
public class VDIExecutorServices {

    private static final VDIExecutorServices instance = new VDIExecutorServices();

    /** 缺省的核心线程数 */
    private static final int THREAD_CORE = 6;

    /** 缺省的最大线程数 */
    private static final int THREAD_MAX = 12;

    /** 缺省的空闲间隔 */
    private static final int THREAD_IDLE = 30;

    /** 缺省的队列大小 */
    private static final int THREAD_CAPACITY = 1024;

    public static VDIExecutorServices get() {
        return instance;
    }

    //用于处理IO阻塞类型的线程池，占用CPU资源较少，但是占用时间长。
    private volatile ExecutorService ioBusyService = null;

    //用于处理CPU繁忙型工作，VDI 公共事件线程池(异步入库、异步方法执行等)
    private volatile ExecutorService cpuBusyService = null;

    // 启监控线程
    private volatile ExecutorService monitorService = null;


    /**
     * 处理VDI中异步操作公共线程池
     */
    public ExecutorService getCpuBusyService() {
        if (null == this.cpuBusyService) {
            synchronized (this) {
                if (null == this.cpuBusyService) {
                    cpuBusyService = new ThreadPoolExecutor(THREAD_CORE,
                            THREAD_MAX, THREAD_IDLE, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(THREAD_CAPACITY),
                            new DaemonThreadFactory("WebCommonOperateService"),
                            new RejectedExecutionHandlerImpl());
                }
            }
        }
        return cpuBusyService;
    }

    public ExecutorService getIoBusyService() {
        if (this.ioBusyService == null) {
            synchronized (this) {
                if (this.ioBusyService == null) {
                    final int CORE = Runtime.getRuntime().availableProcessors();
                    log.info("the core thread num of cluster is {}", CORE);
                    ioBusyService = new ThreadPoolExecutor(CORE,
                            CORE * 2, THREAD_IDLE, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(THREAD_CAPACITY),
                            new DaemonThreadFactory("WebPublicCloudService"),
                            new RejectedExecutionHandlerImpl());
                }
            }
        }
        return ioBusyService;
    }

    /**
     * 处理监控CAS的任务进度的线程池
     *
     * @return 监控CAS的任务进度的线程池
     */
    public ExecutorService getMonitorService() {
        if (null == monitorService) {
            synchronized (this) {
                if (null == monitorService) {
                    monitorService = new ThreadPoolExecutor(THREAD_CORE,
                            THREAD_MAX, THREAD_IDLE, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(THREAD_CAPACITY),
                            new DaemonThreadFactory("WebMonitorService"),
                            new RejectedExecutionHandlerImpl());
                }
            }
        }
        return monitorService;
    }

}
