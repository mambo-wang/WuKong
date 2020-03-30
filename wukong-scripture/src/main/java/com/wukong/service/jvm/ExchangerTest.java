package com.wukong.service.jvm;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangerTest {
    private static final Exchanger<String> exgr = new Exchanger<>();
    private static ExecutorService threadPool = Executors.newFixedThreadPool(2);
 
    public static void main(String[] args) {
        threadPool.execute(() -> {
            try {
                String A = "银行流水A";
                exgr.exchange(A);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
 
        threadPool.execute(() -> {
            try {
                String B = "银行流水B";
                String A = exgr.exchange(B);
                System.out.println("A和B数据是否一致：" + A.equals(B) + ",A的录入是"+ A + ",B录入的是" + B);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threadPool.shutdown();
    }
}
/**
 A和B数据是否一致：false,A的录入是银行流水A,B录入的是银行流水B
 * */