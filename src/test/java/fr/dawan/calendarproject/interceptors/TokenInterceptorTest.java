package fr.dawan.calendarproject.interceptors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.services.UserService;
import fr.dawan.calendarproject.tools.JwtTokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
class TokenInterceptorTest {
	
	@Autowired
	TokenInterceptor tokenInterceptor;
	
	@MockBean
	private JwtTokenUtil jwtTokenUtil;
	
	@MockBean
	private UserService userService;
	
	MockHttpServletRequest request;
	
	private AdvancedUserDto user;
	
	private static String email = "dbalavoine@dawan.fr";
	private static String emailNonAdmin = "lbalavoine@dawan.fr";
	
	@BeforeEach()
	public void beforeEach() throws Exception {	
		request = new MockHttpServletRequest();
		request.setServerName("www.example.com");
		
		TokenSaver.getTokensbyemail().put(email, "tokenTestTokenInterceptor123");
		TokenSaver.getTokensbyemail().put(emailNonAdmin, "tokenTestTokenInterceptor456");
		
		
		
		user = new AdvancedUserDto(1, 1, 1, "Daniel", "Balavoine", 0,
				"dbalavoine@dawan.fr", "testPassword",
				"ADMINISTRATIF", "DAWAN", "","2022-12-31", 0, null);
	}
	
	@AfterAll()
	public static void afterAll() throws Exception {
		TokenSaver.getTokensbyemail().remove(email);
		TokenSaver.getTokensbyemail().remove(emailNonAdmin);
	}
	
	@Test
	void contextLoads() {
		assertThat(tokenInterceptor).isNotNull();
	}
	
	@Test
	void shouldRequestWithOptionMethodDoesNotPassThroughInterceptor() throws Exception {
		request.setMethod("OPTIONS");
		
		boolean result = tokenInterceptor.preHandle(request, null, null);
		
		assertEquals(true, result);
	}

	@Test
	void shouldRequestDoesNotPassThroughInterceptor() throws Exception {
		request.setMethod("POST");
		request.setRequestURI("/authenticate");
		
		boolean result = tokenInterceptor.preHandle(request, null, null);
		
		assertEquals(true, result);
	}
	
	@Test
	void shouldRequestFromAdminPassThroughInterceptor() throws Exception {
		request.setMethod("POST");
		request.setRequestURI("/api/user");
		request.addHeader("Authorization", "Bearer tokenTestTokenInterceptor123");
		
		when(jwtTokenUtil.isTokenExpired(any(String.class))).thenReturn(false);
		when(jwtTokenUtil.getUsernameFromToken(any(String.class))).thenReturn(email);
		
		when(userService.findByEmail(email)).thenReturn(user);
		
		boolean result = tokenInterceptor.preHandle(request, null, null);
		
		assertEquals(true, result);
	}
	
	@Test
	void shouldThrowExceptionWithNonToken() throws Exception {
		request.setMethod("POST");
		request.setRequestURI("/api/user");
		request.addHeader("Authorization", "Bearer");

		assertThrows(Exception.class, () -> {
			tokenInterceptor.preHandle(request, null, null);
		});
	}
	
	@Test
	void shouldThrowExceptionWithExpiredToken() throws Exception {
		request.setMethod("POST");
		request.setRequestURI("/api/user");
		request.addHeader("Authorization", "Bearer tokenTestTokenInterceptor123");
		
		when(jwtTokenUtil.isTokenExpired(any(String.class))).thenReturn(true);

		assertThrows(Exception.class, () -> {
			tokenInterceptor.preHandle(request, null, null);
		});
	}
	
	@Test
	void shouldThrowExceptionWithNoValidToken() throws Exception {
		request.setMethod("POST");
		request.setRequestURI("/api/user");
		request.addHeader("Authorization", "Bearer wrongTokenTest123");
		
		when(jwtTokenUtil.isTokenExpired(any(String.class))).thenReturn(false);
		when(jwtTokenUtil.getUsernameFromToken(any(String.class))).thenReturn(null);
		
		assertThrows(Exception.class, () -> {
			tokenInterceptor.preHandle(request, null, null);
		});
	}
	
	@Test
	void shouldThrowExceptionWithNoAdminTryingOtherThanGet() throws Exception {
		AdvancedUserDto userNonAdmin = new AdvancedUserDto(2, 2, 2, "Lucie", "Balavoine", 0,
				"lbalavoine@dawan.fr", "testPassword2",
				"FORMATEUR", "DAWAN", "","2022-12-31", 0, null);
		
		request.setMethod("POST");
		request.setRequestURI("/api/user");
		request.addHeader("Authorization", "Bearer tokenTestTokenInterceptor456");
		
		when(jwtTokenUtil.isTokenExpired(any(String.class))).thenReturn(false);
		when(jwtTokenUtil.getUsernameFromToken(any(String.class))).thenReturn(emailNonAdmin);
		
		when(userService.findByEmail(emailNonAdmin)).thenReturn(userNonAdmin);
		
		
		assertThrows(Exception.class, () -> {
			tokenInterceptor.preHandle(request, null, null);
		});
	}

}
