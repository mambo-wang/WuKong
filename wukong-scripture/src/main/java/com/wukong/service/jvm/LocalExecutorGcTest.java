package com.wukong.service.jvm;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * https://www.cnblogs.com/Shock-W/p/9835469.html#commentform
 * java使用局部线程池为什么会造成线程泄露
 * ThreadPoolExecutor  ->   Worker   ->  Thread    由于存在这样的引用关系，并且 Thread 作为 GC Root ，所以无法被回收
 */
public class LocalExecutorGcTest {
    public static void main(String[] args) throws Exception {
        LocalExecutorGcTest t = new LocalExecutorGcTest();
        while (true) {
            Thread.sleep(1000);
            t.test();
        }
    }

    private void test() {
        for (int i = 0; i < 10; i++) {
            Executor mExecutors = Executors.newFixedThreadPool(3);
            for (int j = 0; j < 3; j++) {
                mExecutors.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("execute");
                    }
                });
            }
        }
    }
}