package com.wukong.consumer.rabbit.object;

import com.wukong.common.model.UserVO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectSender {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void send(UserVO user) {
		System.out.println("Sender object: " + user.toString());
		this.rabbitTemplate.convertAndSend("object", user);
	}

}