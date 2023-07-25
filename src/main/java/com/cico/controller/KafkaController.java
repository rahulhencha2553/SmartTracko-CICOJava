package com.cico.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.service.impl.KafkaProducer;

@RestController
@RequestMapping("/kafka")
@CrossOrigin("*")
public class KafkaController {

	@Autowired
	private KafkaProducer producer;
	
	@PostMapping("/send")
	public String sendMessage(@RequestParam("msg") String msg) {
		producer.sendMessage(msg);
		return "SENT";
	}

}
