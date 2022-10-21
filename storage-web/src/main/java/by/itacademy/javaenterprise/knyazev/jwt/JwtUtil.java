package by.itacademy.javaenterprise.knyazev.jwt;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtil {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	@Value("${authorization.token.secret}")
	private String TOKEN_SECRET_KEY;
	
	@Value("#{T (java.lang.Long).valueOf('${authorizatin.token.timelife}')}")
	private Long TOKEN_TIME_LIFE;

	public String generateJWT(Authentication authentication) {
		UserDetails user = (UserDetails) authentication.getPrincipal();

		Map<String, Object> header = new HashMap<>();
		header.put("alg", "HMAC-SHA512");
		header.put("typ", "JWT");

		Map<String, String> payload = new HashMap<>();
		payload.put("name", user.getUsername());
		
		return JWT.create().withHeader(header).withPayload(payload)
				.withExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusSeconds(TOKEN_TIME_LIFE)))
				.sign(Algorithm.HMAC512(TOKEN_SECRET_KEY));
	}

	private DecodedJWT verifyJWT(String jwt) {
		try {
			return JWT.require(Algorithm.HMAC512(TOKEN_SECRET_KEY)).build().verify(jwt);
		} catch (RuntimeException e) {
			logger.error("Unable to verify java web token or algorithm failed: {}", e.getMessage());
			return null;
		}
	}

	public String getUserNameFromJWT(String jwt) {
		DecodedJWT decodedJWT = verifyJWT(jwt);

		if (decodedJWT != null) {
			return decodedJWT.getClaim("name").asString();
		}

		return null;
	}
}
