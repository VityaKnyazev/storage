package by.itacademy.javaenterprise.knyazev.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import by.itacademy.javaenterprise.knyazev.jwt.JwtUtil;
import by.itacademy.javaenterprise.knyazev.services.SecurityUsersService;

@Component
public class JsonAuthenticationFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(JsonAuthenticationFilter.class);
	
	private static final String AUTHORIZATION_HEADER = "Bearer ";

	
	@Value("${authentication.url}")
	private String AUTHENTICATION_URL;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private SecurityUsersService securityUsersService;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String jwt = parseAuthorizationHeader(authorizationHeader);

		if (jwt == null) {
			filterChain.doFilter(request, response);
			return;
		}

		String userName = jwtUtil.getUserNameFromJWT(jwt);

		if (userName == null) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			UserDetails userDetails = securityUsersService.loadUserByUsername(userName);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		} catch (UsernameNotFoundException e) {
			logger.error("Authorization error: {}", e.getMessage());
		}
		
		filterChain.doFilter(request, response);
	}

	private final String parseAuthorizationHeader(String authorizationHeader) {
		if (authorizationHeader != null && !authorizationHeader.isEmpty() && !authorizationHeader.isBlank()
				&& authorizationHeader.startsWith(AUTHORIZATION_HEADER)) {
			return authorizationHeader.substring(AUTHORIZATION_HEADER.length() - 1).trim();
		} else {
			return null;
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		if ((request.getMethod().equals(HttpMethod.POST.name())) && (request.getServletPath().equals(AUTHENTICATION_URL))) {
			return true;
		}
		return false;
	}	
	
}