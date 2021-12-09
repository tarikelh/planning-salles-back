package fr.dawan.calendarproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.ResetPasswordDto;
import fr.dawan.calendarproject.dto.ResetResponse;
import fr.dawan.calendarproject.dto.UserDto;
import fr.dawan.calendarproject.interceptors.TokenSaver;
import fr.dawan.calendarproject.services.EmailService;
import fr.dawan.calendarproject.services.UserService;

@RestController
public class ResetPasswordController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	@Value("${vue.baseurl}")
	private String vueUrl;

	@PostMapping(value = "/forgot", produces = "application/json")
	public ResponseEntity<String> sendTokenByEmail(@RequestBody ResetPasswordDto resetObj) throws Exception {
		
		UserDto uDto = userService.findByEmail(resetObj.getEmail());

		if (uDto != null) {
			
			emailService.sendMailForResetPassword(uDto);

			return ResponseEntity.status(HttpStatus.OK).build();
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
	@PostMapping(value = "/forgot-mobile", produces = "application/json")
	public ResponseEntity<Object> sendCodeByEmail(@RequestBody ResetPasswordDto resetObj) throws Exception {
		
		UserDto uDto = userService.findByEmail(resetObj.getEmail());

		if (uDto != null) {
			
			emailService.sendMailForResetPassword(uDto);

			return ResponseEntity.status(HttpStatus.OK).body(new ResetPasswordDto(resetObj.getEmail()));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PostMapping(value = "/reset-password", consumes = "application/json")
	public ResponseEntity<String> resetPassword(@RequestBody ResetResponse reset) throws Exception {

		boolean token = TokenSaver.tokensByEmail.containsValue(reset.getToken());	

		if (token) {
			boolean resetStatus = userService.resetPassword(reset);
			
			if(resetStatus) {
				return ResponseEntity.status(HttpStatus.OK).build();
			} else {
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
			}
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}
}
