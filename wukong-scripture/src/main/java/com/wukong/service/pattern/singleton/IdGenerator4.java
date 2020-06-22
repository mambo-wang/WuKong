package com.wukong.service.pattern.singleton;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 内部类
 *
 * SingletonHolder 是一个静态内部类，当外部类 IdGenerator 被加载的时候，并不会创建 SingletonHolder 实例对象。
 * 只有当调用 getInstance() 方法时，SingletonHolder 才会被加载，这个时候才会创建 instance。
 * instance 的唯一性、创建过程的线程安全性，都由 JVM 来保证。
 * 所以，这种实现方法既保证了线程安全，又能做到延迟加载。
 */
public class IdGenerator4 {
  private AtomicLong id = new AtomicLong(0);
  private IdGenerator4() {}

  private static class SingletonHolder{
    private static final IdGenerator4 instance = new IdGenerator4();
  }
  
  public static IdGenerator4 getInstance() {
    return SingletonHolder.instance;
  }
 
  public long getId() { 
    return id.incrementAndGet();
  }
}