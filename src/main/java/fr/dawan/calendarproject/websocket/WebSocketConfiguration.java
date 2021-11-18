package fr.dawan.calendarproject.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import fr.dawan.calendarproject.dto.APIError;
import fr.dawan.calendarproject.tools.JwtTokenUtil;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
	
	@Autowired
	private HttpHandshakeInterceptor handshakeInterceptor;

	@Value("${vue.baseurl}")
	private String vueUrl;

	
	/**
	 *Set prefix for endpoints to listen and send messages for the WebSocket
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// Set prefix for the endpoint that the client listens for our messages from
		registry.enableSimpleBroker("/topic");

		// Set prefix for endpoints the client will send messages to
		registry.setApplicationDestinationPrefixes("/app");
	}

	/**
	 *Registers the endpoint where the connection will take place for the WebSocket
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// registry.addEndpoint("/ws");
		registry.addEndpoint("/ws")
				// Allow the origin http://localhost:8080 to send messages to us. (Base URL of
				// the client)
				.setAllowedOrigins(vueUrl)
				// Enable SockJS fallback options
				.withSockJS()
				.setInterceptors(handshakeInterceptor);
	}

}