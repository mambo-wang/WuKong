package com.wukong.consumer.rabbit.hello;

import com.wukong.consumer.rabbit.DelayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
@Slf4j
public class HelloSender {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void send() {
		String context = "hello " + new Date();
		System.out.println("Sender : " + context);
		this.rabbitTemplate.convertAndSend("hello", context);
	}

	/**
	 * 延迟消息
	 * @param content
	 */
	public void sendDeadLetter(String content, int seconds){
		this.rabbitTemplate.convertAndSend(DelayConfig.EXCHANGE_DELAY, DelayConfig.ROUTINGKEY_DELAY, content, message -> {
			message.getMessageProperties().setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, String.class.getName());
			message.getMessageProperties().setExpiration(seconds * 1000 + "");
			return message;
		});
		log.info("延迟消息发送时间：{}", LocalDateTime.now());
	}

}