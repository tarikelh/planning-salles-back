package fr.dawan.calendarproject.tools;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;



@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    
	@Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Set prefix for the endpoint that the client listens for our messages from
        registry.enableSimpleBroker("/topic");
        
        // Set prefix for endpoints the client will send messages to
        registry.setApplicationDestinationPrefixes("/app");
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registers the endpoint where the connection will take place
    	//registry.addEndpoint("/ws");
        registry.addEndpoint("/ws")
            // Allow the origin http://localhost:8080 to send messages to us. (Base URL of the client)
            .setAllowedOrigins("http://localhost:8080")
            // Enable SockJS fallback options
            .withSockJS();
    }
}