package com.wukong.service.pattern.chainofresponsibility;

/**
 * 在职责链模式中，多个处理器依次处理同一个请求。一个请求先经过 A 处理器处理，
 * 然后再把请求传递给 B 处理器，B 处理器处理完后再传递给 C 处理器，以此类推，形成一个链条。
 * 链条上的每个处理器各自承担各自的处理职责，所以叫作职责链模式。
 *
 * 标准的职责链模式，链上的处理器顺序执行，有一个处理器可以处理，就终止传递执行
 * 变体的职责链模式，链上的处理器会顺序执行，不会终止。
 *
 * 职责链模式的两种实现方式：
 * 1.链表，只记录head和tail，结合模板方法模式，显式调用下一个处理器，具体处理器只要实现自己的处理逻辑即可。
 * 2.数组列表，将处理器放进一个list里，Java的arraylist底层就是一个数组，for循环调用所有的处理器
 */
public class Application {
  public static void main(String[] args) {
    HandlerChain chain = new HandlerChain();
    chain.addHandler(new HandlerA());
    chain.addHandler(new HandlerB());
    chain.handle();
  }
}