package com.wukong.provider.rabbit.object;

import com.wukong.common.contants.Constant;
import com.wukong.provider.dto.AddScoreDTO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddScoreSender {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void send(AddScoreDTO user) {
		System.out.println("Sender object: " + user.toString());
		this.rabbitTemplate.convertAndSend(Constant.RabbitMQ.queueAddScore, user);
	}

}