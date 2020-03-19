package com.wukong.provider.config.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SpringSelfListener implements ApplicationListener<ApplicationEvent> {
    @Override
    public void onApplicationEvent(ApplicationEvent ApplicationEvent) {
        System.out.println("spring监听本身事件========="+ApplicationEvent);
    }
}
