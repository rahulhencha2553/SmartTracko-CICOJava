package com.cico.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin("*")
public class WebSocketController {

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public String greeting(String message) {
		return "Hello, " + message + "!";
	}
}
