package fr.dawan.calendarproject.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import fr.dawan.calendarproject.dto.MessageWebsocketDto;
import fr.dawan.calendarproject.interceptors.TokenSaver;
import fr.dawan.calendarproject.tools.JwtTokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
class WebsocketControllerTest {
	
	@Autowired
	private WebsocketController websocketController;
	
	@MockBean
	private JwtTokenUtil jwtTokenUtil;
	
	private String header = "Bearer tokenwebsockettest";
	
	private static String email = "test@testEmail.com";
	
	@BeforeEach()
	public void beforeEach() throws Exception {
		TokenSaver.getTokensbyemail().put(email, "tokenwebsockettest");
	}
	
	@AfterAll()
	public static void afterAll() throws Exception {
		TokenSaver.getTokensbyemail().remove(email);
	}
	
	@Test
	void contextLoads() {
		assertThat(websocketController).isNotNull();
	}

	@Test
	void shouldGetMessagesWhenDeleteEvent() throws Exception {
		MessageWebsocketDto messageWebsocketDto = new MessageWebsocketDto("DELETE", "1", null);
		
		when(jwtTokenUtil.isTokenExpired(any(String.class))).thenReturn(false);
		when(jwtTokenUtil.getUsernameFromToken(any(String.class))).thenReturn(email);
		
		MessageWebsocketDto result = websocketController.getMessages(messageWebsocketDto, header);
		
		assertEquals(messageWebsocketDto, result);
	}
	
	@Test
	void shouldGetMessagesWhenAddEvent() throws Exception {
		MessageWebsocketDto messageWebsocketDto = new MessageWebsocketDto("ADD", "2", "event 2 details when add it");
		
		when(jwtTokenUtil.isTokenExpired(any(String.class))).thenReturn(false);
		when(jwtTokenUtil.getUsernameFromToken(any(String.class))).thenReturn(email);
		
		MessageWebsocketDto result = websocketController.getMessages(messageWebsocketDto, header);
		
		assertEquals(messageWebsocketDto, result);
	}
	
	@Test
	void shouldGetMessagesWhenEditEvent() throws Exception {
		MessageWebsocketDto messageWebsocketDto = new MessageWebsocketDto("EDIT", "2", "event 2 details when edit it");
		
		when(jwtTokenUtil.isTokenExpired(any(String.class))).thenReturn(false);
		when(jwtTokenUtil.getUsernameFromToken(any(String.class))).thenReturn(email);
		
		MessageWebsocketDto result = websocketController.getMessages(messageWebsocketDto, header);
		
		assertEquals(messageWebsocketDto, result);
	}
	
	@Test
	void shouldThrowExceptionWhenMessageFormatNotCorrect() throws Exception {
		MessageWebsocketDto messageWebsocketDto = new MessageWebsocketDto("Hacking", "Test", "Websocket");
		
		when(jwtTokenUtil.isTokenExpired(any(String.class))).thenReturn(false);
		when(jwtTokenUtil.getUsernameFromToken(any(String.class))).thenReturn(email);
		
		assertThrows(Exception.class, () -> {
			websocketController.getMessages(messageWebsocketDto, header);
		});
	}
	
	@Test
	void shouldThrowExceptionWhenTokenIsExpired() throws Exception {
		MessageWebsocketDto messageWebsocketDto = new MessageWebsocketDto("DELETE", "1", null);
		
		when(jwtTokenUtil.isTokenExpired(any(String.class))).thenReturn(true);
		
		assertThrows(Exception.class, () -> {
			websocketController.getMessages(messageWebsocketDto, header);
		});
	}
	
	@Test
	void shouldThrowExceptionWhenTokenIsWrong() throws Exception {
		MessageWebsocketDto messageWebsocketDto = new MessageWebsocketDto("DELETE", "1", null);
		
		when(jwtTokenUtil.isTokenExpired(any(String.class))).thenReturn(false);
		when(jwtTokenUtil.getUsernameFromToken(any(String.class))).thenReturn(email);
		
		assertThrows(Exception.class, () -> {
			websocketController.getMessages(messageWebsocketDto, "Bearer tokenhacking");
		});
	}

}
