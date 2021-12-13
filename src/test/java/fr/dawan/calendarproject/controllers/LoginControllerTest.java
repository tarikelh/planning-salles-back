package fr.dawan.calendarproject.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.LoginDto;
import fr.dawan.calendarproject.services.UserService;
import fr.dawan.calendarproject.tools.HashTools;
import fr.dawan.calendarproject.tools.JwtTokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private LoginController loginController;

	@MockBean
	private UserService userService;

	@MockBean
	private JwtTokenUtil jwtTokenUtil;

	private MockedStatic<HashTools> hashTools;

	private AdvancedUserDto user;
	private LoginDto login;

	@BeforeEach
	void beforeEach() throws Exception {
		user = new AdvancedUserDto(1, 1, "Daniel", "Balavoine", 1, "dbalavoine@dawan.fr", "testPassword", "ADMINISTRATIF",
				"DAWAN", "", 0, null);

		login = new LoginDto("dbalavoine@dawan.fr", "testPassword");
	}

	@Test
	void contextLoads() {
		assertThat(loginController).isNotNull();
	}

	@Test
	void shouldAuthenticateWhenGoodLogin() throws Exception {

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String loginJson = objectMapper.writeValueAsString(login);

		hashTools = Mockito.mockStatic(HashTools.class);
		hashTools.when(() -> HashTools.hashSHA512(any(String.class))).thenReturn("testPassword");
		when(userService.findByEmail(any(String.class))).thenReturn(user);
		when(jwtTokenUtil.doGenerateToken(Mockito.anyMap(), any(String.class))).thenReturn("testToken123");

		mockMvc.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(loginJson)).andExpect(status().isOk())
				.andExpect(jsonPath("$.user.fullName", is("Daniel Balavoine")))
				.andExpect(jsonPath("$.token", is("testToken123")));

		if (!hashTools.isClosed())
			hashTools.close();
	}

	@Test
	void shouldDenyAccessWhenGivingWrongPassword() throws Exception {
		login.setPassword("wrongPassword");

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String loginJson = objectMapper.writeValueAsString(login);

		when(userService.findByEmail(login.getEmail())).thenReturn(user);

		mockMvc.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(loginJson)).andExpect(status().is(422));
	}

	@Test
	void shouldDenyAccessWhenGivingWrongLogin() throws Exception {
		login.setEmail("wrong@email.fr");

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String loginJson = objectMapper.writeValueAsString(login);

		hashTools = Mockito.mockStatic(HashTools.class);
		hashTools.when(() -> HashTools.hashSHA512(any(String.class))).thenReturn("testPassword");
		when(userService.findByEmail(login.getEmail())).thenReturn(null);
		when(jwtTokenUtil.doGenerateToken(Mockito.anyMap(), any(String.class))).thenReturn("testToken123");

		mockMvc.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(loginJson)).andExpect(status().is(422));

		if (!hashTools.isClosed())
			hashTools.close();
	}
}
