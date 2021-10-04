package fr.dawan.calendarproject.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// System.out.println(">>>>>> inside Token Interceptor...");
		// System.out.println("URI =" + request.getRequestURI());
		// System.out.println("Header (authorization) :" +
		// request.getHeader("Authorization"));

		if (!request.getRequestURI().equals("/authenticate") && !request.getRequestURI().equals("/forgot")
				&& !request.getRequestURI().equals("/check-token")
				&& !request.getRequestURI().equals("/reset-password")) {
			String headerAuth = request.getHeader("Authorization");
			if (headerAuth == null || headerAuth.trim().equals("") || headerAuth.length() < 7) {
				throw new Exception("Erreur : jeton absent ou invalide !");
			}

			String token = headerAuth.substring(7);

			// validation le token et extraire les infos
			if (jwtTokenUtil.isTokenExpired(token))
				throw new Exception("Erreur : jeton expiré !");

			String email = jwtTokenUtil.getUsernameFromToken(token);
			if (!TokenSaver.tokensByEmail.containsKey(email) || !TokenSaver.tokensByEmail.get(email).equals(token))
				throw new Exception("Erreur : jeton non reconnu !");

			// Vérification du role d'un utilisateur
			String typeRequest = request.getMethod();
			if (!typeRequest.equals("GET")) {
				String userType = userService.findByEmail(email).getType();
				if (!userType.equals(UserType.ADMINISTRATIF.toString())) {
					throw new Exception("Erreur : Action non autorisée !");
				}
			}
			// TODO autres extractions du jeton ou autres traitements
		}
		return true;
	}

}
