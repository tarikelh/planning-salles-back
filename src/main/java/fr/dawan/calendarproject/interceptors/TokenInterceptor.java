 package fr.dawan.calendarproject.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.services.UserService;
import fr.dawan.calendarproject.tools.JwtTokenUtil;

@Component
public class TokenInterceptor implements HandlerInterceptor {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// System.out.println(">>>>>> inside Token Interceptor...");
		// System.out.println("URI =" + request.getRequestURI());
		// System.out.println("Header (authorization) :" +
		// request.getHeader("Authorization"));
		
		String uri = request.getRequestURI();
		logger.info(uri);
		
		if(!request.getMethod().equalsIgnoreCase("OPTIONS")){
			if (!request.getRequestURI().equals("/authenticate")
					&& !request.getRequestURI().equals("/forgot")
					&& !request.getRequestURI().equals("/forgot-mobile")
					&& !request.getRequestURI().equals("/check-token")
					&& !request.getRequestURI().equals("/reset-password")
					&& !request.getRequestURI().contains("/ws")) {
				String headerAuth = request.getHeader("Authorization");
				if (headerAuth == null || headerAuth.trim().equals("") || headerAuth.length() < 7) {
					throw new Exception("Error : missing or invalid token !");
				}
	
				String token = headerAuth.substring(7);
	
				// validate token and get info
				if (jwtTokenUtil.isTokenExpired(token))
					throw new Exception("Error : token expired");
	
				String email = jwtTokenUtil.getUsernameFromToken(token);
				if (!TokenSaver.tokensByEmail.containsKey(email) || !TokenSaver.tokensByEmail.get(email).equals(token))
					throw new Exception("Error : token not known !");
	
				// verification of the user role
				String typeRequest = request.getMethod();
				if (!typeRequest.equals("GET")) {
					String userType = userService.findByEmail(email).getType();
					if (!userType.equals(UserType.ADMINISTRATIF.toString())) {
						throw new Exception("Error : Unauthorized action !");
					}
				}
			}
		}
		
		return true;
	}

}
