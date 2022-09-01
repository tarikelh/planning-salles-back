package fr.dawan.calendarproject.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import fr.dawan.calendarproject.dto.LoginDto;
import fr.dawan.calendarproject.dto.LoginResponseDto;
import fr.dawan.calendarproject.dto.UserDto;
import fr.dawan.calendarproject.interceptors.TokenSaver;
import fr.dawan.calendarproject.services.UserService;
import fr.dawan.calendarproject.tools.HashTools;
import fr.dawan.calendarproject.tools.JwtTokenUtil;

@RestController
public class LoginController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	RestTemplate restTemplate;

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@PostMapping(value = "/authenticate", consumes = "application/json")
	public ResponseEntity<Object> checkLogin(@RequestBody LoginDto loginObj) {

		UserDto uDto = userService.findByEmail(loginObj.getEmail());

		String hashedPwd = null;

		try {
			hashedPwd = HashTools.hashSHA512(loginObj.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (uDto != null && uDto.getPassword().contentEquals(hashedPwd)) {
			Map<String, Object> claims = new HashMap<>();
			claims.put("name", uDto.getFullName());

			String token = jwtTokenUtil.doGenerateToken(claims, loginObj.getEmail());
			TokenSaver.getTokensbyemail().put(loginObj.getEmail(), token);

			return ResponseEntity.ok(new LoginResponseDto(uDto, token));
		} else {
			String message = "Login Error : incorrect username or password entered by " + loginObj.getEmail()
					+ ". Date : " + LocalDateTime.now();
			logger.error(message);
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Erreur : identifiant ou mot de passe incorrects !");
		}
	}
	
	@PostMapping(value = "/test-dg2-login", consumes = "application/json")
	public ResponseEntity<Boolean> testDg2LogIn(@RequestBody LoginDto loginObj){
		URI url = null;
		
		try {
			url = new URI("https://dawan.org/api2/planning/locations");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("x-auth-token", loginObj.getEmail() + ":" + loginObj.getPassword());

		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		ResponseEntity<String> repWs = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

		return ResponseEntity.ok((repWs.getStatusCode() == HttpStatus.OK));
	}
}
