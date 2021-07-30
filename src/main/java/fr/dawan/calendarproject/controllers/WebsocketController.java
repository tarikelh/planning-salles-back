package fr.dawan.calendarproject.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import fr.dawan.calendarproject.dto.MessageWebsocketDto;

@Controller
public class WebsocketController {

	// Handles messages from /app/chat. (Note the Spring adds the /app prefix for
	// us).
	@MessageMapping("/chat")
	// Sends the return value of this method to /topic/messages
	@SendTo("/topic/messages")
	public MessageWebsocketDto getMessages(MessageWebsocketDto messageWebsocketDto) {
		System.out.println(messageWebsocketDto);
		return messageWebsocketDto;
	}

}
