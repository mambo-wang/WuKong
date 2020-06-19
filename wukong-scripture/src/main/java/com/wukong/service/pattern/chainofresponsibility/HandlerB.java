package com.wukong.service.pattern.chainofresponsibility;

public class HandlerB extends Handler {
  @Override
  protected void doHandle() {
    //...
    System.out.println("handler b");
  }
}
