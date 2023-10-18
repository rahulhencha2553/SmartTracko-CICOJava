package com.cico.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class DiscussionFromWebSocket {

	public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/socket1").setAllowedOrigins("http://localhost:4200","http://localhost:4200/","http://cico.dollopinfotech.com/","http://cico.dollopinfotech.com").withSockJS();
    }
}
