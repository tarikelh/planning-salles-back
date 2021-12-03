package fr.dawan.calendarproject.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.ResetPasswordDto;
import fr.dawan.calendarproject.dto.ResetResponse;
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
	
	@Value("${vue.baseurl}")
	private String vueUrl;

	@PostMapping(value = "/forgot", produces = "application/json")
	public ResponseEntity<?> sendTokenByEmail(@RequestBody ResetPasswordDto resetObj) throws Exception {

		UserDto uDto = userService.findByEmail(resetObj.getEmail());

		if (uDto != null) {
			Map<String, Object> claims = new HashMap<String, Object>();
			claims.put("name", uDto.getFullName());

			// Ajouter les données que l'on souhaite
			String token = jwtTokenUtil.doGenerateToken(claims, resetObj.getEmail());
			TokenSaver.tokensByEmail.put(resetObj.getEmail(), token);
			
			String resetLink = vueUrl + "/#/fr/reset-password?token=" + token;
			String body =
			        "<HTML><body> <a href=\""+ resetLink +"\">Réinitialiser mon mot de passe</a></body></HTML>";

			MimeMessage msg = javaMailSender.createMimeMessage();
			
			msg.addRecipients(Message.RecipientType.TO, uDto.getEmail());
			msg.setSubject("Réinitialisation du mot de passe du Calendrier Dawan");
			msg.setText("Bonjour " + uDto.getLastName() + ". <br /><br />Ce message vous a été envoyé car vous avez oublié votre mot de passe sur l'application"
					+ " Calendrier Dawan. <br />Pour réinitialiser votre mot de passe, veuillez cliquer sur ce lien : " + body, "UTF-8", "html");

			javaMailSender.send(msg);

			return ResponseEntity.status(HttpStatus.OK).build();
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
	@PostMapping(value = "/forgot-mobile", produces = "application/json")
	public ResponseEntity<?> sendCodeByEmail(@RequestBody ResetPasswordDto resetObj) throws Exception {
		
		UserDto uDto = userService.findByEmail(resetObj.getEmail());

		if (uDto != null) {
			Map<String, Object> claims = new HashMap<String, Object>();
			claims.put("name", uDto.getFullName());

			// Ajouter les données que l'on souhaite
			String token = jwtTokenUtil.doGenerateToken(claims, resetObj.getEmail());
			TokenSaver.tokensByEmail.put(resetObj.getEmail(), token);
			
			String resetLink = vueUrl + "/#/fr/reset-password?token=" + token;
			String body =
			        "<HTML><body> <a href=\""+ resetLink +"\">Réinitialiser mon mot de passe</a></body></HTML>";

			MimeMessage msg = javaMailSender.createMimeMessage();
			
			msg.addRecipients(Message.RecipientType.TO, uDto.getEmail());
			msg.setSubject("Réinitialisation du mot de passe du Calendrier Dawan");
			msg.setText("Bonjour " + uDto.getLastName() + ". <br /><br />Ce message vous a été envoyé car vous avez oublié votre mot de passe sur l'application"
					+ " Calendrier Dawan. <br />Pour réinitialiser votre mot de passe, veuillez cliquer sur ce lien : " + body, "UTF-8", "html");

			javaMailSender.send(msg);

			return ResponseEntity.status(HttpStatus.OK).body(new ResetPasswordDto(resetObj.getEmail()));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PostMapping(value = "/reset-password", consumes = "application/json")
	public ResponseEntity<?> resetPassword(@RequestBody ResetResponse reset) throws Exception {

		boolean token = TokenSaver.tokensByEmail.containsValue(reset.getToken());	

		if (token) {
			String hashedPwd = HashTools.hashSHA512(reset.getPassword());
			String email = jwtTokenUtil.getUsernameFromToken(reset.getToken());

			AdvancedUserDto uDto = userService.findByEmail(email);
			if (uDto != null && !uDto.getPassword().equals(hashedPwd)) {
				// new password
				uDto.setPassword(reset.getPassword());

				// save the new password in DB
				userService.saveOrUpdate(uDto);

				return ResponseEntity.status(HttpStatus.OK).build();
			} else if (uDto != null && uDto.getPassword().equals(hashedPwd)) {
				// same password
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
			}
			else {
				// if user == null
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
			}

		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

	}
}
