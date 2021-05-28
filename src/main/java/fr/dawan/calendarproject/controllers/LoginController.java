package fr.dawan.calendarproject.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.TokenSaver;
import fr.dawan.calendarproject.dto.LoginDto;
import fr.dawan.calendarproject.dto.LoginResponseDto;
import fr.dawan.calendarproject.dto.UserDto;
import fr.dawan.calendarproject.services.UserService;
import fr.dawan.calendarproject.tools.HashTools;
import fr.dawan.calendarproject.tools.JwtTokenUtil;


@RestController
public class LoginController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	
	@PostMapping(value="/authenticate", consumes = "application/json")
	public ResponseEntity<?> checkLogin(@RequestBody LoginDto loginObj) throws Exception {
		UserDto cDto = userService.findByEmail(loginObj.getEmail());
		//String hashedPwd = HashTools.hashSHA512(loginObj.getPassword());
		String pwd = loginObj.getPassword();
		if(cDto !=null && cDto.getPassword().contentEquals(pwd)) {
			
			//Fabrication du token en utilisant jjwt (librairie incluse dans le pom)
			Map<String, Object> claims = new HashMap<String, Object>();
			claims.put("name", cDto.getFirstName()); //change for "getName()"
			//ajouter les données que l'on souhaite
			String token = jwtTokenUtil.doGenerateToken(claims, loginObj.getEmail());
			TokenSaver.tokensByEmail.put(loginObj.getEmail(), token);
			
			return ResponseEntity.ok(new LoginResponseDto(cDto.getId(), cDto.getFullName(), token));
		}else
			throw new Exception("Erreur : identifiants incorrects !");
	}

}
