package com.wukong.service.pattern.observer.eventbus;

import com.google.common.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class Test {


    public static void main(String[] args) {

        EventBus eventBus = new EventBus();

        List<String> listenedMessageList = new ArrayList<>();
        CustomEventListener customEventListener = new CustomEventListener(listenedMessageList);

        eventBus.register(customEventListener);

        eventBus.post(new CustomEvent("post a custom event ---- 1"));

        eventBus.unregister(customEventListener);

        eventBus.post(new CustomEvent("post a custom event ---- 2"));
    }

}
