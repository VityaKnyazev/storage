package by.itacademy.javaenterprise.knyazev.spring;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import by.itacademy.javaenterprise.knyazev.filters.JsonAuthenticationFilter;
import by.itacademy.javaenterprise.knyazev.services.SecurityUsersService;

@Configuration
@ComponentScan(basePackages = { "by.itacademy.javaenterprise.knyazev.filters",
		"by.itacademy.javaenterprise.knyazev.jwt" })
@PropertySource(value = "classpath:secure.properties")
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private SecurityUsersService securityUsersService;

	@Autowired
	private JsonAuthenticationFilter jsonAuthenticationFilter;
	
	@Value("${authentication.url}")
	private String AUTHENTICATION_URL;
	
	@Value("${registration.url}")
	private String REGISTRATION_URL;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.cors().and().csrf().disable()
		
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

				.exceptionHandling().authenticationEntryPoint((request, response, ex) -> {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
				}).and()

				.authorizeRequests().regexMatchers(HttpMethod.POST, "/(goods|categories|storehouses)").hasRole("ADMIN")
				.regexMatchers(HttpMethod.PUT, "/(goods|categories|storehouses)").hasRole("ADMIN")
				.regexMatchers(HttpMethod.DELETE, "/(goods/.{1,}|categories/.{1,}|storehouses/.{1,})").hasRole("ADMIN")
				
				.antMatchers(HttpMethod.GET, "/reports").hasRole("ADMIN")

				.antMatchers(HttpMethod.GET, "/purchases").hasRole("USER")
				.antMatchers(HttpMethod.POST, "/purchases").hasRole("USER")
				.antMatchers(HttpMethod.PUT, "/purchases").hasRole("USER")
				.regexMatchers(HttpMethod.DELETE, "/purchases/.{1,}").hasRole("USER")
				
				.regexMatchers(HttpMethod.GET, "/(users|users/.{1,})").hasRole("ADMIN")
				.regexMatchers(HttpMethod.DELETE, "/users/.{1,}").hasRole("ADMIN")
				
				.antMatchers(HttpMethod.POST, AUTHENTICATION_URL).permitAll()
				
				.antMatchers(HttpMethod.POST, REGISTRATION_URL).permitAll()
				
				.anyRequest().authenticated()
				
				.and().addFilterAt(jsonAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(securityUsersService);
		return daoAuthenticationProvider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return new CorsFilter(source);
	}
}