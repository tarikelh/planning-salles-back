package fr.dawan.calendarproject.interceptors;

import java.util.HashMap;
import java.util.Map;

/**
 * While the server is running the token from an user will be saved in
 * TokenSaver. TokenSaver will save < email, token >
 */
public class TokenSaver {

	private TokenSaver() {
	}

	// email/token
	private static final Map<String, String> tokensByEmail;

	static {
		tokensByEmail = new HashMap<>();
	}

	public static Map<String, String> getTokensbyemail() {
		return tokensByEmail;
	}
}
