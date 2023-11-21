//package com.cico.config;
//
//import java.io.IOException;
//import java.net.URI;
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//
//public class SocketHandler extends TextWebSocketHandler {
//
//	public static List<WebSocketSession> qrSessions = new CopyOnWriteArrayList<>();
//	public static List<WebSocketSession> dicussionSessions = new CopyOnWriteArrayList<>();
//	public static List<WebSocketSession> announcementSessions = new CopyOnWriteArrayList<>();
//
//	@Override
//	public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
//		for (WebSocketSession webSocketSession : dicussionSessions) {
//			// Sends message to all sessions excepted himself
//			if (!session.equals(webSocketSession)) {
//				ObjectMapper objectMapper = new ObjectMapper();
//
//				try {
//					JsonNode root = objectMapper.readTree(message.getPayload());
//					if (root.has("type")) {
//
//						String type = root.get("type").asText();
//						System.out.println("Type: " + type);
//
//						if (type.equals("commentResponse")) {
//							CommentResponse value = new Gson().fromJson(message.getPayload(), CommentResponse.class);
//							webSocketSession.sendMessage(new TextMessage(new Gson().toJson(value)));
//						}
//						if (type.equals("likeResponse")) {
//							LikeResponseForm value = new Gson().fromJson(message.getPayload(), LikeResponseForm.class);
//							webSocketSession.sendMessage(new TextMessage(new Gson().toJson(value)));
//						}
//						if (type.equals("createDiscussionForm")) {
//							DiscussionResponseForm value = new Gson().fromJson(message.getPayload(),
//									DiscussionResponseForm.class);
//							webSocketSession.sendMessage(new TextMessage(new Gson().toJson(value)));
//						}
//					} else {
//						System.out.println("No 'type' field found in the JSON.");
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//
//	@Override
//	public void afterConnectionEstablished(WebSocketSession session) {
//
//		URI uri = session.getUri();
//		System.out.println("URI__" + uri);
//		if (uri.getPath().equals("/ws/sessionId")) {
//			qrSessions.add(session);
//			System.out.println("sessoin added in qr sessions");
//		}
//		if (uri.getPath().equals("/ws/discussion")) {
//			dicussionSessions.add(session);
//			System.out.println("sessoin added in Discussion sessions");
//		}
////		if(uri.getPath().equals("/ws/announcement")) {
////			announcementSessions.add(session);
////			System.out.println("sessoin added in Announcement sessions");
////		}
//	}
//
//	@Override
//	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
//
//		URI uri = session.getUri();
//		if (uri.getPath().equals("/ws/sessionId")) {
//			qrSessions.remove(session);
//			System.err.println("disconnect");
//		}
//		if (uri.getPath().equals("/ws/discussion")) {
//			dicussionSessions.remove(session);
//		}
////		if (uri.getPath().equals("/ws/announcement")) {
////			announcementSessions.remove(session);
////		}
//	}
//}
