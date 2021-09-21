package fr.dawan.calendarproject.controllers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.dto.CaptchaResponse;
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

	@PostMapping(value = "/authenticate", consumes = "application/json")
	public ResponseEntity<?> checkLogin(@RequestBody LoginDto loginObj) throws Exception {
		
		if(loginObj.getCaptchaToken() == "default") {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erreur : captchat absant ou non valide");
		} else if (loginObj.getCaptchaToken() != ""){
			HttpClient client = HttpClient.newHttpClient();
			
			String secret = "6Leav28cAAAAAGDXtovG7YrZIqgsAdddiZ9Lze4k";
			String uri = "https://www.google.com/recaptcha/api/siteverify?secret=" + secret 
						 + "&response=" + loginObj.getCaptchaToken();
			
			HttpRequest request = HttpRequest.newBuilder()
			          .uri(URI.create(uri))
			          .build();
			
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

			CaptchaResponse res = new CaptchaResponse();
			
			if(response.body() != null) {
				ObjectMapper mapper = new ObjectMapper();
				res = mapper.readValue(response.body(), CaptchaResponse.class);
			}
			
			System.out.println(res);
		}
		
		UserDto uDto = userService.findByEmail(loginObj.getEmail());
		
		String hashedPwd = HashTools.hashSHA512(loginObj.getPassword());

		if (uDto != null && uDto.getPassword().contentEquals(hashedPwd)) {

			Map<String, Object> claims = new HashMap<String, Object>();
			claims.put("name", uDto.getFullName());

			String token = jwtTokenUtil.doGenerateToken(claims, loginObj.getEmail());
			TokenSaver.tokensByEmail.put(loginObj.getEmail(), token);

			return ResponseEntity.ok(new LoginResponseDto(uDto, token));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur : identifiants incorrects !");
		}
	}
}
