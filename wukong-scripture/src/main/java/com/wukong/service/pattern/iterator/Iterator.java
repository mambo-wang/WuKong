package com.wukong.service.pattern.iterator;

public interface Iterator<E> {
    boolean hasNext();
    void next();
    E currentItem();
}