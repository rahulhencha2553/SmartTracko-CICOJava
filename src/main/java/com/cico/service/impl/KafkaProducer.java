package com.cico.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Value("${cico.topic.name}")
	private String topicName;
	
	public void sendMessage(String message) {
		kafkaTemplate.send(topicName,message);
		System.out.println("MESSAGE_SENT");
	}
}
