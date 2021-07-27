package fr.dawan.calendarproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import fr.dawan.calendarproject.dto.MessageWebsocketDto;
import fr.dawan.calendarproject.interceptors.TokenSaver;
import fr.dawan.calendarproject.tools.JwtTokenUtil;

@Controller
public class WebsocketController {
	
    @Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	// Handles messages from /app/chat. (Note the Spring adds the /app prefix for us).
    @MessageMapping("/chat")    
    // Sends the return value of this method to /topic/messages
    @SendTo("/topic/messages")
    public MessageWebsocketDto getMessages(MessageWebsocketDto messageWebsocketDto, @Header(name = "token") String header){
    	
    	String accessToken = header.split(" ")[1];
    	
    	if (jwtTokenUtil.isTokenExpired(accessToken))
        	System.out.println("Erreur : jeton expiré !");
        

		String email = jwtTokenUtil.getUsernameFromToken(accessToken);
		if (!TokenSaver.tokensByEmail.containsKey(email) || !TokenSaver.tokensByEmail.get(email).equals(accessToken))
			System.out.println("Erreur : jeton non reconnu !");
    	
        return messageWebsocketDto;
    }
	
}
