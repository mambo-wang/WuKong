package com.wukong.provider.rabbit.object;

import com.wukong.common.contants.Constant;
import com.wukong.provider.dto.AddScoreDTO;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class AddScoreSender {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void send(AddScoreDTO user) {
		String id = UUID.randomUUID().toString();
		System.out.println("Sender object: " + user.toString());
		this.rabbitTemplate.convertAndSend(Constant.RabbitMQ.queueAddScore, user, message -> {
			message.getMessageProperties().setCorrelationId(id);
			return message;
		});
	}

}