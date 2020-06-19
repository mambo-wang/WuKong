package com.wukong.service.pattern.chainofresponsibility;

public class HandlerA extends Handler {
  @Override
  protected void doHandle() {
    //...
    System.out.println("handler a");
  }
}