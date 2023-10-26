package com.cico.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    	 System.err.println("request comes ");
    	 registry.addHandler(new SocketHandler(), "/ws/sessionId").setAllowedOrigins("*").addInterceptors(new CustomHandshakeInterceptor());
    	 registry.addHandler(new SocketHandler(), "/ws/discussion").setAllowedOrigins("*");//.addInterceptors(new CustomHandshakeInterceptor());
    }  
}