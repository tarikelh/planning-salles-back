package fr.dawan.calendarproject.tools;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class JwtTokenUtilTest {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	private static String email1;
	private static String email2;
	private String token1;
	private String token2;
	
	private static Map<String, Object> claims = new HashMap<String, Object>();
	private static Map<String, String> tokensByEmail = new HashMap<String, String>();
	
	@BeforeEach()
	public void beforeEach() throws Exception {
		email1 = "admin1@admin.fr";
		email2 = "admin2@admin.fr";
		token1 = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbjFAYWRtaW4uZnIiLCJuYW1lIjoiQWRtaW4xIEFkbWluMSIsImV4cCI6MTYzODM2NzUxMiwiaWF0IjoxNjM4MzQ5NTEyfQ.q9gbcpHNWlYotjRNqkRGqhAT1YR_WY1OJjPsZFvL6xVPqU8IQhLGupZT_GFh1tmt2k3GGp1x8z6F0J2JbJ8SXQ";
		token2 = "tokentestadmin2";
		
		claims.put("name", "Admin1 Admin1"); 
		
		tokensByEmail.put(email1, token1);
		tokensByEmail.put(email2, token2);
	}
	
	@AfterAll()
	public static void afterAll() throws Exception {
		claims.remove("name");
		tokensByEmail.remove(email1);
		tokensByEmail.remove(email2);
	}
	
	@Test
	void contextLoads() {
		assertThat(jwtTokenUtil).isNotNull();
	}
	
	@Test
	void shouldDoGenerateToken() {
		String result = jwtTokenUtil.doGenerateToken(claims, email1);
		
		assertThat(result).isNotNull();
		assertTrue(result.length() > 200 ); 
	}

	@Test
	void shouldGetUsernameFromToken() {
		String result = jwtTokenUtil.getUsernameFromToken(token1);
		
		assertThat(result).isNotNull();
		org.junit.Assert.assertEquals("admin1@admin.fr", result);
	}
	
	@Test
	void shouldThrowExceptionWhenIncorrectTokenFormat() {
		assertThrows(Exception.class, () -> {
			jwtTokenUtil.getUsernameFromToken(token2);
		});
	}

	@Test
	void shouldGetIssuedAtDateFromToken() {
		Calendar calendar = Calendar.getInstance();
		LocalDate today = LocalDate.now();  
		
		Date result = jwtTokenUtil.getIssuedAtDateFromToken(token1);
		
		assertThat(result).isNotNull();
		
		calendar.setTime(result);
		org.junit.Assert.assertEquals(today.getYear(), calendar.get(Calendar.YEAR));
		org.junit.Assert.assertEquals(today.getDayOfMonth(), calendar.get(Calendar.DAY_OF_MONTH));
	}

	@Test
	void shouldGetExpirationDateFromToken() {
		Calendar calendar = Calendar.getInstance();
		LocalDate today = LocalDate.now();  
		
		Date result = jwtTokenUtil.getIssuedAtDateFromToken(token1);
		
		assertThat(result).isNotNull();
		
		calendar.setTime(result);
		org.junit.Assert.assertEquals(today.getYear(), calendar.get(Calendar.YEAR));
		org.junit.Assert.assertEquals(today.getDayOfMonth(), calendar.get(Calendar.DAY_OF_MONTH));
	}


	@Test
	void shouldBeFalseIfTokenNotExpired() {
		Boolean result = jwtTokenUtil.isTokenExpired(token1);
		
		org.junit.Assert.assertEquals(false, result);
	}

}
