package com.wukong.service.jvm;

import lombok.SneakyThrows;

public class DeadLock {

    public static void main(String[] args) {
        DeadLockThread d1 = new DeadLockThread(true);
        DeadLockThread d2 = new DeadLockThread(false);

        Thread t1 = new Thread(d1);
        Thread t2 = new Thread(d2);

        t1.start();
        t2.start();
    }
}

class DeadLockThread implements Runnable{

    boolean flag;
    static Object o1 = new Object();
    static Object o2 = new Object();

    public DeadLockThread(boolean flag){
        this.flag = flag;
    }

    @SneakyThrows
    @Override
    public void run() {
        if(flag){
            synchronized (o1){
                Thread.sleep(1000);
                synchronized (o2){
                    System.out.println("1111111111111111");
                }

            }
        } else {
            synchronized (o2){
                Thread.sleep(1000);


                synchronized (o1){
                    System.out.println("2222222222222222222");
                }
            }

        }
    }
}
