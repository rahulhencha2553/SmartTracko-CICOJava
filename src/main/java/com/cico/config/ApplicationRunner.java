package com.cico.config;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class ApplicationRunner implements CommandLineRunner {
//	  List<WebSocketSession> sessions;
//
		@Override
		public void run(String... args) throws Exception {
			// Your custom code to be executed when the application starts
			System.out.println("Application started. Executing custom code...");
			// sessions = new CopyOnWriteArrayList<>();
		}

//	public void addSession(WebSocketSession session) {
//		sessions.add(session);
//	}
//
//	public List<WebSocketSession> getSession() {
//		return sessions;
//	}
}