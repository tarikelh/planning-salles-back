package fr.dawan.calendarproject.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.ResetPasswordDto;
import fr.dawan.calendarproject.dto.ResetResponse;
import fr.dawan.calendarproject.interceptors.TokenSaver;
import fr.dawan.calendarproject.services.UserService;
import fr.dawan.calendarproject.tools.HashTools;
import fr.dawan.calendarproject.tools.JwtTokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
class ResetPasswordControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private JwtTokenUtil jwtTokenUtil;
	
	@MockBean
	private UserService userService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private JavaMailSender javaMailSender;
	private MimeMessage mimeMessage;
	
	private MockedStatic<HashTools> hashTools;
	
	private ResetResponse resetResponse;
	private String resetResponseJson;
	
	private AdvancedUserDto adUserDto;
	
	private static String email = "dbalavoine@dawan.fr";
	
	@BeforeEach()
	public void beforeEach() throws Exception {	
		TokenSaver.tokensByEmail.put(email, "TokenTestResetPassword");
		
		adUserDto = new AdvancedUserDto(1, "Daniel", "Balavoine", 0,
				"dbalavoine@dawan.fr", "testPassword",
				"ADMINISTRATIF", "DAWAN", "", 0, null);
		
		resetResponse = new ResetResponse("TokenTestResetPassword", "ResetPasswordTest");
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		resetResponseJson = objectMapper.writeValueAsString(resetResponse);
		
		mimeMessage = new MimeMessage((Session)null);
        javaMailSender = mock(JavaMailSender.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
	}
	
	@AfterAll()
	public static void afterAll() throws Exception {
		TokenSaver.tokensByEmail.remove(email);
	}
	
	
	@Test
	void shouldResetPassword() throws Exception {
		
		when(userService.resetPassword(any(ResetResponse.class))).thenReturn(true);
		mockMvc.perform(post("/reset-password").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(resetResponseJson)).andExpect(status().isOk());
		
	}
	
	
	@Test
	void shouldReturnErrorWhenUserNotKnownForResetPassword() throws Exception {
		
		when(jwtTokenUtil.getUsernameFromToken(any(String.class))).thenReturn(email);
		when(userService.findByEmail(email)).thenReturn(null);
		
		mockMvc.perform(post("/reset-password").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(resetResponseJson)).andExpect(status().isNotAcceptable());
	}
	
	@Test
	void shouldReturnErrorWhenUserSaveSamePasswordForResetPassword() throws Exception {
		
		hashTools = Mockito.mockStatic(HashTools.class);
		hashTools.when(() -> HashTools.hashSHA512(any(String.class))).thenReturn("testPassword");
		when(jwtTokenUtil.getUsernameFromToken(any(String.class))).thenReturn(email);
		when(userService.findByEmail(email)).thenReturn(adUserDto);
		
		mockMvc.perform(post("/reset-password").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(resetResponseJson)).andExpect(status().isNotAcceptable());
		
		if(!hashTools.isClosed())
			hashTools.close();
	}
	
	@Test
	void shouldReturnErrorWhenTokenNotKnownForResetPassword() throws Exception {
		ResetResponse resetResponse = new ResetResponse("WrongTokenTestResetPassword", "ResetPasswordTest");
		String resetResponseJson = objectMapper.writeValueAsString(resetResponse);
		
		mockMvc.perform(post("/reset-password").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(resetResponseJson)).andExpect(status().isInternalServerError());
	}
	
	@Test
	void shouldSendTokenByEmail() throws Exception {
		ResetPasswordDto resetPasswordDto = new ResetPasswordDto(email, "ResetPasswordTest");
		String resetPasswordDtoJson = objectMapper.writeValueAsString(resetPasswordDto);
		when(userService.findByEmail(email)).thenReturn(adUserDto);
		when(jwtTokenUtil.doGenerateToken(Mockito.anyMap(), any(String.class))).thenReturn("testToken123");
		doNothing().when(javaMailSender).send(mimeMessage);
		
		mockMvc.perform(post("/forgot").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(resetPasswordDtoJson)).andExpect(status().isOk());
	}
	
	@Test
	void shouldThrowErrorWhenEmailDoesNotExist() throws Exception {
		ResetPasswordDto resetPasswordDto = new ResetPasswordDto("randomemail@test.fr", "ResetPasswordTest");
		String resetPasswordDtoJson = objectMapper.writeValueAsString(resetPasswordDto);	
		when(userService.findByEmail(email)).thenReturn(null);
		
		mockMvc.perform(post("/forgot").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(resetPasswordDtoJson)).andExpect(status().isNotFound());
	}
	
	@Test
	void shouldSendCodeByEmail() throws Exception {
		ResetPasswordDto resetPasswordDto = new ResetPasswordDto(email, "ResetPasswordTest");
		String resetPasswordDtoJson = objectMapper.writeValueAsString(resetPasswordDto);
		when(userService.findByEmail(email)).thenReturn(adUserDto);
		when(jwtTokenUtil.doGenerateToken(Mockito.anyMap(), any(String.class))).thenReturn("testToken123");
		doNothing().when(javaMailSender).send(mimeMessage);
		
		mockMvc.perform(post("/forgot-mobile").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(resetPasswordDtoJson)).andExpect(status().isOk());
	}
	
	@Test
	void shouldThrowErrorWhenEmailDoesNotExistForForgotMobile() throws Exception {
		ResetPasswordDto resetPasswordDto = new ResetPasswordDto("randomemail@test.fr", "ResetPasswordTest");
		String resetPasswordDtoJson = objectMapper.writeValueAsString(resetPasswordDto);	
		when(userService.findByEmail(email)).thenReturn(null);
		
		mockMvc.perform(post("/forgot-mobile").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(resetPasswordDtoJson)).andExpect(status().isNotFound());
	}

}
