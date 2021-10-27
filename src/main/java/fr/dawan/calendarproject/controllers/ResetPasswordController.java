package fr.dawan.calendarproject.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.ResetPasswordDto;
import fr.dawan.calendarproject.dto.ResetResponse;
import fr.dawan.calendarproject.dto.TokenDto;
import fr.dawan.calendarproject.dto.UserDto;
import fr.dawan.calendarproject.interceptors.TokenSaver;
import fr.dawan.calendarproject.services.UserService;
import fr.dawan.calendarproject.tools.HashTools;
import fr.dawan.calendarproject.tools.JwtTokenUtil;

@RestController
public class ResetPasswordController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JavaMailSender javaMailSender;

	@PostMapping(value = "/forgot", produces = "application/json")
	public ResponseEntity<?> sendTokenByEmail(@RequestBody ResetPasswordDto resetObj) throws Exception {

		UserDto uDto = userService.findByEmail(resetObj.getEmail());

		if (uDto != null) {
			Map<String, Object> claims = new HashMap<String, Object>();
			claims.put("name", uDto.getFullName());

			// Ajouter les données que l'on souhaite
			String token = jwtTokenUtil.doGenerateToken(claims, resetObj.getEmail());
			TokenSaver.tokensByEmail.put(resetObj.getEmail(), token);

			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(uDto.getEmail());
			msg.setSubject("Réinitialisation du mot de passe DaCalendar");
			msg.setText("Pour réinitialiser votre mot de passe, veuillez entrer ce code : " + token);

			javaMailSender.send(msg);

			return ResponseEntity.status(HttpStatus.OK).build();
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PostMapping(value = "/check-token", consumes = "application/json")
	public ResponseEntity<?> checkToken(@RequestBody TokenDto tokenObj) throws Exception {

		String token = tokenObj.getToken();
		String checktoken = TokenSaver.tokensByEmail.get(tokenObj.getEmail());

		if (token.equals(checktoken))
			return ResponseEntity.status(HttpStatus.OK).build();
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

	}

	@PostMapping(value = "/reset-password", consumes = "application/json")
	public ResponseEntity<?> resetPassword(@RequestBody ResetResponse reset) throws Exception {

		boolean token = TokenSaver.tokensByEmail.containsValue(reset.getToken());
		String hashedPwd = HashTools.hashSHA512(reset.getPassword());

		if (token) {
			String email = jwtTokenUtil.getUsernameFromToken(reset.getToken());

			AdvancedUserDto uDto = userService.findByEmail(email);
			if (uDto != null && !uDto.getPassword().equals(hashedPwd)) {
				// new password
				uDto.setPassword(reset.getPassword());

				// save the new password in DB
				userService.saveOrUpdatePassword(uDto);

				return ResponseEntity.status(HttpStatus.OK).build();
			} else if (uDto.getPassword().equals(hashedPwd))
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

	}
}
