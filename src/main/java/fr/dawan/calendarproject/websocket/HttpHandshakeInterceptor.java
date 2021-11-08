package fr.dawan.calendarproject.websocket;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * <p>
 * For easy deployment, the WebSocket uses HTTP Handshake. 
 * This means that for the first time, the client sends an HTTP request to the server, indicating to the server that it is not an HTTP by asking it to equip itself with the WebSocket. 
 * They ultimately form a connection.
 *</p>
 */
@Component
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(HttpHandshakeInterceptor.class);

	/**
	 *Handles events immediately before the WebSocket connects with HTTP.
	 */
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		logger.info("Call beforeHandshake");

		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpSession session = servletRequest.getServletRequest().getSession();
			attributes.put("sessionId", session.getId());
		}
		return true;
	}

	/**
	 *Handles events immediately after the WebSocket connects with HTTP.
	 */
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		logger.info("Call afterHandshake");
	}

}
