package com.wukong.service.pattern.observer.eventbus;

import com.google.common.eventbus.Subscribe;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomEventListener {

    private List<String> listenedMessageList;

    @Subscribe
    public void onEvent(CustomEvent event) {
        log.info("Guava EventListener listened one message : {}", event.getMessage());
        listenedMessageList.add(event.getMessage());
    }

}
