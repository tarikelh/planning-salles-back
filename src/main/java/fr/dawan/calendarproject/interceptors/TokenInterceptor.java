package fr.dawan.calendarproject.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import fr.dawan.calendarproject.TokenSaver;
import fr.dawan.calendarproject.tools.JwtTokenUtil;

@Component
public class TokenInterceptor implements HandlerInterceptor {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	// A commenter si nous avons besoin d'insérer nouveau contact sans token
	/*
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		System.out.println(">>>>>> inside Token Interceptor...");
		System.out.println("URI =" + request.getRequestURI());
		System.out.println("Header (authorization) :" + request.getHeader("Authorization"));

		if (!request.getRequestURI().equals("/authenticate")) {
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

			// TODO autres extractions du jeton ou autres traitements

		}
		return true;
	}
	*/
	
}
