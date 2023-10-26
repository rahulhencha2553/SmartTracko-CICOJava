//package com.cico.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
//import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//	public void configureMessageBroker(MessageBrokerRegistry registry){
//        registry.enableSimpleBroker("/queue");
//        registry.setApplicationDestinationPrefixes("/api");
//    }
//
//    public void registerStompEndpoints(StompEndpointRegistry registry){
//    	registry.addEndpoint("/socket").setAllowedOrigins("http://localhost:4200","http://localhost:4200/","http://cico.dollopinfotech.com/","http://cico.dollopinfotech.com").setHandshakeHandler(new DefaultHandshakeHandler(new TomcatRequestUpgradeStrategy()));
//        registry.addEndpoint("/socket").setAllowedOrigins("http://localhost:4200","http://localhost:4200/","http://cico.dollopinfotech.com/","http://cico.dollopinfotech.com").setHandshakeHandler(new DefaultHandshakeHandler(new TomcatRequestUpgradeStrategy())).withSockJS();
//       
//    }
//
//	
//}
