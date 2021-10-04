package fr.dawan.calendarproject.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${app.google.captcha.secrets.prod}")
	private String _secret;

	@Value("${app.google.captcha.secrets.test}")
	private String _secretTest;

	@Value("${app.google.captcha.secrets.url}")
	private String captchaUrl;

	@PostMapping(value = "/authenticate", consumes = "application/json")
	public ResponseEntity<?> checkLogin(@RequestBody LoginDto loginObj) {

		if (loginObj.getCaptchaToken() != null) {

			String secret = _secret;
			String uri = captchaUrl + "?secret=" + secret + "&response=" + loginObj.getCaptchaToken();
			ResponseEntity<CaptchaResponse> res = null;

			URI url;

			try {
				RestTemplate restTemplate = new RestTemplate();
				url = new URI(uri);
				res = restTemplate.getForEntity(url, CaptchaResponse.class);

				if (res.getStatusCode() == HttpStatus.OK) {
					CaptchaResponse cr = res.getBody();

					if (cr.getSuccess()) {
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
							return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
									.body("Erreur : identifiants ou mot de passe incorrects !");
						}
					} else {
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
								.body("Erreur : Captcha invalide ou expiré !");
					}
				}
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur : captchat absant !");
	}
}
