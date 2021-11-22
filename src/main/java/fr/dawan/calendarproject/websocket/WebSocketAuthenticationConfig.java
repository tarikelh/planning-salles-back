package fr.dawan.calendarproject.websocket;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import fr.dawan.calendarproject.dto.APIError;
import fr.dawan.calendarproject.exceptions.TokenException;
import fr.dawan.calendarproject.interceptors.TokenSaver;
import fr.dawan.calendarproject.tools.JwtTokenUtil;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketAuthenticationConfig implements WebSocketMessageBrokerConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketAuthenticationConfig.class);
	private APIError error;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	/**
	 *Token verification when client try to connect to the WebSocket
	 *
	 *@param registration customize the configuration for a MessageChannel.
	 *<p>
	 *For more information about ChannelRegistration you can check its documentation.
	 *</p>
	 *@see ChannelRegistration
	 */
	@Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
        	
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> authorization = accessor.getNativeHeader("X-Authorization");
                    logger.debug("X-Authorization: {}", authorization);

                    String accessToken = authorization.get(0).split(" ")[1];
                    
                    if (jwtTokenUtil.isTokenExpired(accessToken)) {
                    	logger.error("Websocket Error : token expired ! ");
                    }
                    	
              
    				String email = jwtTokenUtil.getUsernameFromToken(accessToken);
    				if (!TokenSaver.tokensByEmail.containsKey(email) || !TokenSaver.tokensByEmail.get(email).equals(accessToken)) {
    					logger.error("Websocket Error : token not known ! " + email + ". Date : " + LocalDateTime.now());
    				}
    				
                }
                return message;
            }
        });
    }

}
