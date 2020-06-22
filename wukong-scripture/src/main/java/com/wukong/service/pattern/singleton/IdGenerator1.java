package com.wukong.service.pattern.singleton;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 饿汉
 *
 * 饿汉式的实现方式比较简单。在类加载的时候，instance 静态实例就已经创建并初始化好了，所以，instance 实例的创建过程是线程安全的。
 * 不过，这样的实现方式不支持延迟加载（在真正用到 IdGenerator 的时候，再创建实例），从名字中我们也可以看出这一点。
 */
public class IdGenerator1 {
  private AtomicLong id = new AtomicLong(0);
  private static final IdGenerator1 instance = new IdGenerator1();
  private IdGenerator1() {}
  public static IdGenerator1 getInstance() {
    return instance;
  }
  public long getId() { 
    return id.incrementAndGet();
  }
}