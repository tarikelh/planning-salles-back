package fr.dawan.calendarproject.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
	public ResponseEntity<?> checkLogin(@RequestBody LoginDto loginObj) {
		
		if(loginObj.getCaptchaToken() == "default") {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erreur : captchat absant ou non valide");
		} else if (loginObj.getCaptchaToken() != ""){
			
			String secret = "6Leav28cAAAAAGDXtovG7YrZIqgsAdddiZ9Lze4k";
			String uri = "https://www.google.com/recaptcha/api/siteverify?secret=" + secret 
						 + "&response=" + loginObj.getCaptchaToken();
			ResponseEntity<CaptchaResponse> res = null;
			
			URI url;
			
			try {
				RestTemplate restTemplate = new RestTemplate();
				url = new URI(uri);
				res = restTemplate.getForEntity(url, CaptchaResponse.class);

				if(res.getStatusCode()==HttpStatus.OK) {
					CaptchaResponse nbStr = res.getBody();
				}
				if(res != null) {
					System.out.println(res);
				}
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		
		UserDto uDto = userService.findByEmail(loginObj.getEmail());
		
		String hashedPwd = null;
		
		try {
			hashedPwd = HashTools.hashSHA512(loginObj.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}

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
