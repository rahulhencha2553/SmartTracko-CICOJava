package com.cico.config;

import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
	//	System.out.println("customhandler request comes");
		// Extract the session ID from the URL
	   String uri = request.getURI().toString();
	//	System.out.println(uri);
		String sessionId = extractSessionIdFromURI(uri);
            //   System.err.println("---------------"+sessionId);
		// Set the session ID as an attribute
		//System.out.println("session id ----"+sessionId);

    	//attributes.put("id", sessionId );
    	attributes.put("sessionId", sessionId );
        System.out.println("111"+attributes.get("sessionId"));
        System.out.println("111"+attributes.get("id"));
	  //   System.out.println("attributes"+attributes);
        // System.out.println(attributes);
		// You can also perform other tasks here before the handshake

		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		// After the handshake (no additional actions needed)
	}

	private String extractSessionIdFromURI(String uri) {
		// Parse the session ID from the URI as needed
		// For example, you can use regular expressions or string manipulation
		// This depends on the format of the URI you are using

		// Here's a simple example assuming the session ID is at the end of the URI:
		// String[] parts = uri.split("/");
		String[] parts = uri.split("\\?=");
		//System.err.println("SessionIdOfQr---------------" + parts);
		//System.err.println(parts[parts.length - 1]);
		//System.err.println(parts[parts.length - 1]);
		return parts[parts.length - 1];

	}
}
