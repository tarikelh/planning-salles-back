package fr.dawan.calendarproject.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import fr.dawan.calendarproject.tools.JwtTokenUtil;

@RestController
public class LoginController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private HttpServletRequest request;

	@Value("${app.ldap.url}")
	private String ldapUrl;

	@Value("${app.ldap.protocol}")
	private String ldapProtocol;

	@Value("${app.ldap.technical.dn}")
	private String ldapTechnicalAccDN;

	@Value("${app.ldap.technical.pwd}")
	private String ldapTechnicalAccPwd;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getLdapUrl() {
		return ldapUrl;
	}

	public void setLdapUrl(String ldapUrl) {
		this.ldapUrl = ldapUrl;
	}

	public String getLdapProtocol() {
		return ldapProtocol;
	}

	public void setLdapProtocol(String ldapProtocol) {
		this.ldapProtocol = ldapProtocol;
	}

	public String getLdapTechnicalAccDN() {
		return ldapTechnicalAccDN;
	}

	public void setLdapTechnicalAccDN(String ldapTechnicalAccDN) {
		this.ldapTechnicalAccDN = ldapTechnicalAccDN;
	}

	public String getLdapTechnicalAccPwd() {
		return ldapTechnicalAccPwd;
	}

	public void setLdapTechnicalAccPwd(String ldapTechnicalAccPwd) {
		this.ldapTechnicalAccPwd = ldapTechnicalAccPwd;
	}

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@PostMapping(value = "/authenticate", consumes = "application/json")
	public ResponseEntity<Object> checkLogin(@RequestBody LoginDto loginDto) throws Exception {
		ResponseEntity<Object> responseEntity = null;
		
		// email domains allowed
		String listDomains = "@jehann.fr;@dawan.fr"; // TODO externalize domains in application.properties
		String[] domains = listDomains.split(";");
		
		// check if email domain of loginDto is allowed
		boolean auth = false;
		for (int i = 0; i < domains.length; i++) {
			if (auth == false) {
				auth = loginDto.getEmail().contains(domains[i]);
			}
		}

		// if email domain is allowed
		if (auth) {
			// login via LDAP
			try {
				Hashtable<String, String> environment = new Hashtable<>();
				environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
				environment.put(Context.PROVIDER_URL, ldapUrl);
				environment.put(Context.SECURITY_PROTOCOL, ldapProtocol);
				environment.put(Context.SECURITY_AUTHENTICATION, "simple");

				String organizationUnit = "Dawan";
				String uid = loginDto.getEmail().substring(0, loginDto.getEmail().indexOf('@'));
				
				// les utilisateurs jehann se loggent avec un compte : xxxx-jehann
				if (loginDto.getEmail().contains("@jehann.fr")) {
					uid = uid + "-jehann";
					organizationUnit = "jehann";
				}
				
				environment.put(Context.SECURITY_PRINCIPAL,
						"uid=" + uid + ",ou=" + organizationUnit + ",ou=Utilisateurs,dc=dawan,dc=fr");
				environment.put(Context.SECURITY_CREDENTIALS, loginDto.getPassword());

				DirContext ctx = new InitialDirContext(environment);

				logger.info("Login of user " + loginDto.getEmail() + " from : " + request.getRemoteAddr());
				ctx.close();

				// check if user exist in application Db
				UserDto userInDb = userService.findByEmail(loginDto.getEmail());
				
				if (userInDb != null) { // user found in DB
					responseEntity = ResponseEntity.ok(createTokenFromUser(userInDb));
				} else { // user authenticated via LDAP but no present in DB
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body("Erreur : Utilisateur inconnue ! (demander une synchronisation de la base)");
				}
				
			} catch (Exception ex) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body("Erreur : identifiant ou mot de passe incorrect !");
			}
		}
//		else { // login via Db
//			UserDto userInDb = userService.findByEmail(loginDto.getEmail());
//			if (userInDb != null && userInDb.getPassword().equals(HashTools.hashSHA512(loginDto.getPassword()))) {
//				return createTokenFromUser(userInDb);
//			} else {
//				logger.info("Login failed of" + loginDto.getEmail() + " from " + request.getRemoteAddr());
//				throw new Exception("Error : invalid credentials !");
//			}
//		}
		return responseEntity;
	}

	private LoginResponseDto createTokenFromUser(UserDto userDto) throws Exception {
		LoginResponseDto result = new LoginResponseDto();

		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("user_id", userDto.getId());
		claims.put("user_fullName", userDto.getFullName());
		claims.put("user_role", userDto.getType().toString());

		String token = jwtTokenUtil.doGenerateToken(claims, userDto.getEmail());
		TokenSaver.getTokensbyemail().put(userDto.getEmail(), token);

		result.setToken(token);
		result.setUser(userDto);

		logger.info("Login of user " + userDto.getId() + " from : " + request.getRemoteAddr());

		return result;
	}

//	@PostMapping(value = "/authenticate", consumes = "application/json")
//	public ResponseEntity<Object> checkLogin(@RequestBody LoginDto loginObj) {
//
//		UserDto uDto = userService.findByEmail(loginObj.getEmail());
//
//		String hashedPwd = null;
//
//		try {
//			hashedPwd = HashTools.hashSHA512(loginObj.getPassword());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		if (uDto != null && uDto.getPassword().contentEquals(hashedPwd)) {
//			Map<String, Object> claims = new HashMap<>();
//			claims.put("name", uDto.getFullName());
//
//			String token = jwtTokenUtil.doGenerateToken(claims, loginObj.getEmail());
//			TokenSaver.getTokensbyemail().put(loginObj.getEmail(), token);
//
//			return ResponseEntity.ok(new LoginResponseDto(uDto, token));
//		} else {
//			String message = "Login Error : incorrect username or password entered by " + loginObj.getEmail()
//					+ ". Date : " + LocalDateTime.now();
//			logger.error(message);
//			return ResponseEntity.status(HttpStatus.NOT_FOUND)
//					.body("Erreur : identifiant ou mot de passe incorrects !");
//		}
//	}

//	@PostMapping(value = "/test-dg2-login", consumes = "application/json")
//	public ResponseEntity<Boolean> testDg2LogIn(@RequestBody LoginDto loginObj) {
//		URI url = null;
//
//		try {
//			url = new URI("https://dawan.org/api2/planning/locations");
//		} catch (URISyntaxException e) {
//			e.printStackTrace();
//		}
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("x-auth-token", loginObj.getEmail() + ":" + loginObj.getPassword());
//
//		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
//
//		ResponseEntity<String> repWs = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
//
//		return ResponseEntity.ok((repWs.getStatusCode() == HttpStatus.OK));
//	}
}
