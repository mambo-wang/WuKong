package com.wukong.service.jvm;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreTest {
    // 创建信号量对象，并给予3个资源
    static Semaphore semaphore = new Semaphore(3);
    private static ExecutorService threadPool = Executors.newCachedThreadPool();
    public static void main(String[] args) {
        // 开启10条线程
        for ( int i=0; i<10; i++ ) {
            final int j=i;
            threadPool.execute(() -> {
                try {
                    // 获取资源，若此时资源被用光，则阻塞，直到有线程归还资源
                    semaphore.acquire();
                    // 任务代码
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println("任务执行完毕"+j+"，等待的线程数"+semaphore.getQueueLength());
                    // 释放资源
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
/**
 任务执行完毕0，等待的线程数7
 任务执行完毕1，等待的线程数7
 任务执行完毕2，等待的线程数7
 任务执行完毕6，等待的线程数4
 任务执行完毕3，等待的线程数4
 任务执行完毕5，等待的线程数4
 任务执行完毕4，等待的线程数1
 任务执行完毕8，等待的线程数1
 任务执行完毕9，等待的线程数1
 任务执行完毕7，等待的线程数0
 */