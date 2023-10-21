package WebSocketForDiscussonForm;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.cico.model.Message;
import com.google.gson.Gson;

public class SocketHandler extends TextWebSocketHandler {
	List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
		System.err.println("111111111111111111111111111111111111111111");
		for (WebSocketSession webSocketSession : sessions) {
			// Sends message to all sessions excepted himself
			// if (!session.equals(webSocketSession)) {
			Message value = new Gson().fromJson(message.getPayload(), Message.class);
			webSocketSession.sendMessage(new TextMessage(new Gson().toJson(value)));
			// }
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		// Will add the session to list -> to broadcast messages
		sessions.add(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		sessions.remove(session);
	}
}