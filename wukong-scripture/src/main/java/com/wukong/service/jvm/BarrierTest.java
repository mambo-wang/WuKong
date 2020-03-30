package com.wukong.service.jvm;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BarrierTest {
    // 创建同步屏障对象，并制定需要等待的线程个数 和 打开屏障时需要执行的任务
    static CyclicBarrier barrier = new CyclicBarrier(3,new Runnable(){
        public void run(){
            //当所有线程准备完毕后触发此任务
            System.out.println("准备好了");
        }
    });
    
    public static void main(String[] args) {
        // 启动三条线程
        for( int i=0; i<3; i++ ){
            new Thread( new Runnable(){
                public void run(){
                    // 等待，（每执行一次barrier.await，同步屏障数量-1，直到为0时，打开屏障）
                    try {
                        System.out.println("屏障未打开");
                        barrier.await();
                        System.out.println("屏障打开");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            } ).start();
        }
    }
}
/**
 屏障未打开
 屏障未打开
 屏障未打开
 准备好了
 屏障打开
 屏障打开
 屏障打开
 * */