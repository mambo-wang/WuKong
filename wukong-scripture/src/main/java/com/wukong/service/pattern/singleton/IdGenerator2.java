package com.wukong.service.pattern.singleton;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 懒汉
 *
 * 懒汉式的缺点也很明显，我们给 getInstance() 这个方法加了一把大锁（synchronzed），导致这个函数的并发度很低。
 */
public class IdGenerator2 {
  private AtomicLong id = new AtomicLong(0);
  private static IdGenerator2 instance;
  private IdGenerator2() {}
  public static synchronized IdGenerator2 getInstance() {
    if (instance == null) {
      instance = new IdGenerator2();
    }
    return instance;
  }
  public long getId() { 
    return id.incrementAndGet();
  }
}