package fr.dawan.calendarproject.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
	}
}
