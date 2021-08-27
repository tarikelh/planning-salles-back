package fr.dawan.calendarproject.controllers;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.bridge.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.LoginDto;
import fr.dawan.calendarproject.dto.ResetResponse;
import fr.dawan.calendarproject.dto.TokenDto;
import fr.dawan.calendarproject.TokenSaver;
import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.LoginResponseDto;
import fr.dawan.calendarproject.dto.ResetPasswordDto;
import fr.dawan.calendarproject.dto.UserDto;
import fr.dawan.calendarproject.services.UserService;
import fr.dawan.calendarproject.tools.HashTools;
import fr.dawan.calendarproject.tools.JwtTokenUtil;

@RestController
public class ResetPasswordController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	String email = "";

	@Autowired
	private JavaMailSender javaMailSender;

	@PostMapping(value = "/forgot", produces = "application/json")
	public ResponseEntity<?> sendTokenByEmail(@RequestBody ResetPasswordDto resetObj) throws Exception {

		UserDto uDto = userService.findByEmail(resetObj.getEmail());

		if (uDto != null) {
			Map<String, Object> claims = new HashMap<String, Object>();
			claims.put("name", uDto.getFirstName()); // change for "getName()"
			// ajouter les données que l'on souhaite
			String token = jwtTokenUtil.doGenerateToken(claims, resetObj.getEmail());
			TokenSaver.tokensByEmail.put(resetObj.getEmail(), token);

			// String link = "http://localhost:8080/fr/reset?token=" + token;

			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(uDto.getEmail());
			//Voir avec Mohammed pour une adresse mail générique 
			//msg.setFrom("noreply@dawan.fr");
			msg.setSubject("Réinitialisation du mot de passe DaCalendar");
			msg.setText("Pour réinitialiser votre mot de passe, veuillez entrer ce code : " + token);

			javaMailSender.send(msg);

			return ResponseEntity.status(HttpStatus.OK).body("Mail envoyé !");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Erreur : L'adresse mail est incorrecte ou n'existe pas.");
		}
	}

	@PostMapping(value = "/api/check-token", consumes = "application/json")
	public ResponseEntity<?> checkToken(@RequestBody TokenDto tokenObj) throws Exception {
		
			String token = tokenObj.getToken();
			String checktoken = TokenSaver.tokensByEmail.get(tokenObj.getEmail());
			
			if(token.equals(checktoken))
				return ResponseEntity.status(HttpStatus.OK).body("Code valide");
			else
				throw new Exception("Erreur : Code incorrect ou expiré !");
		
	}

	@PostMapping(value = "/api/reset-password", consumes = "application/json")
	public ResponseEntity<?> resetPassword(@RequestBody ResetResponse reset) throws Exception {

		boolean token = TokenSaver.tokensByEmail.containsValue(reset.getToken());
		String hashedPwd = HashTools.hashSHA512(reset.getPassword());

		// uDto != null && uDto.getPassword() != loginObj.getPassword()

		if (token) {
			TokenSaver.tokensByEmail.forEach((k, v) -> {
				if (v.equals(reset.getToken())) {
					email = k;
					AdvancedUserDto uDto = userService.findByEmail(email);
					if (uDto != null && !uDto.getPassword().equals(hashedPwd)) {
						// new password
						uDto.setPassword(reset.getPassword());
						// save the new password in DB
						userService.saveOrUpdatePassword(uDto);
					} else
						throw new AbortException("Mot de passe identique à l'ancien !");
				}

			});

			return ResponseEntity.status(HttpStatus.OK).body("Mot de passe modifié");
		} else
			throw new Exception("Erreur : Code incorrect !");

	}
}
