package by.itacademy.javaenterprise.knyazev.filters;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import by.itacademy.javaenterprise.knyazev.jwt.JwtUtil;

public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	private JwtUtil jwtUtil;
	
	private static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";

	private static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
	
	private static final String AUTHENTICATION_HEADER = "Authorization";
	
	private static final String TOKEN_PREFIX = "Bearer ";

	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login",
			"POST");

	protected JsonUsernamePasswordAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher, AuthenticationManager authenticationManager) {
		super(requiresAuthenticationRequestMatcher, authenticationManager);
		this.jwtUtil = new JwtUtil();
	}
	
	public JsonUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
		this(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		String username = null, password = null;
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);

			username = requestMap.get(SPRING_SECURITY_FORM_USERNAME_KEY);
			username = (username != null) ? username : "";
			username = username.trim();
			password = requestMap.get(SPRING_SECURITY_FORM_PASSWORD_KEY);
			password = (password != null) ? password : "";
			
		} catch (IllegalStateException | IOException | ClassCastException e) {
			throw new AuthenticationServiceException(e.getMessage(), e);
		}

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		String token = jwtUtil.generateJWT(authResult);
		response.addHeader(AUTHENTICATION_HEADER, TOKEN_PREFIX + token);
	
		super.successfulAuthentication(request, response, chain, authResult);
	}
	
	

}
