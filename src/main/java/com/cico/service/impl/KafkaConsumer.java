package com.cico.service.impl;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
 //@KafkaListener(topics = "${cico.topic.name}",groupId = "CICO")
	public void readMessage(String message) {
		System.out.println("MESSAGE READ >>>>>>> "+message);
	}
}
