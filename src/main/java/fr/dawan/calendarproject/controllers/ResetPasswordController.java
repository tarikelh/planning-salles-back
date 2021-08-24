package fr.dawan.calendarproject.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.LoginDto;
import fr.dawan.calendarproject.dto.ResetPasswordDto;
import fr.dawan.calendarproject.dto.ResetResponseDto;
import fr.dawan.calendarproject.dto.UserDto;
import fr.dawan.calendarproject.interceptors.TokenSaver;
import fr.dawan.calendarproject.services.UserService;
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
	public void sendTokenByEmail(@RequestBody ResetPasswordDto resetObj) throws Exception {

		UserDto uDto = userService.findByEmail(resetObj.getEmail());

		if (uDto != null) {
			
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("name", uDto.getFirstName()); // change for "getName()"
		// ajouter les données que l'on souhaite
		String token = jwtTokenUtil.doGenerateToken(claims, resetObj.getEmail());
		TokenSaver.tokensByEmail.put(resetObj.getEmail(), token);
		
		//String link = "http://localhost:8080/fr/reset?token=" + token;
		
		SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(uDto.getEmail());
        msg.setFrom("noreply@dawan.fr");
        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email \n Pour réinitialiser votre mot de passe, veuillez entrer ce code : " + token);

        javaMailSender.send(msg);
        
		} else
			throw new Exception("Erreur : L'adresse mail est incorrecte ou n'existe pas.");
	}

	@PostMapping(value = "/api/reset-password", consumes = "application/json")
	public ResponseEntity<?> resetPassword(@RequestBody LoginDto loginObj) throws Exception {

		AdvancedUserDto uDto = userService.findByEmail(loginObj.getEmail());

		// String hashedPwd = HashTools.hashSHA512(loginObj.getPassword());

		if (uDto != null && uDto.getPassword() != loginObj.getPassword()) {
			// new password
			uDto.setPassword(loginObj.getPassword());
			System.out.println("test");
			// save the new password in DB
			userService.saveOrUpdatePassword(uDto);

			return ResponseEntity.ok(new ResetResponseDto("all good"));
		} else
			throw new Exception("Erreur : Mot de passe identique a l'ancien !");

	}
}
