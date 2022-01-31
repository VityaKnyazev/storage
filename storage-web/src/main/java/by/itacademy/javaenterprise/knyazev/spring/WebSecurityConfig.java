package by.itacademy.javaenterprise.knyazev.spring;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import by.itacademy.javaenterprise.knyazev.services.UsersService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UsersService usersService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().anonymous().disable()
		.authorizeRequests()
				.regexMatchers(HttpMethod.POST, "/(goods|categories|storehouses)").hasRole("ADMIN")
				.regexMatchers(HttpMethod.PUT, "/(goods|categories|storehouses)").hasRole("ADMIN")
				.regexMatchers(HttpMethod.DELETE, "/(goods/.{1,}|categories/.{1,}|storehouses/.{1,})").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET, "/reports").hasRole("ADMIN")
				
				.antMatchers(HttpMethod.GET, "/purchases").hasRole("USER")
				.antMatchers(HttpMethod.POST, "/purchases").hasRole("USER")
				.antMatchers(HttpMethod.PUT, "/purchases").hasRole("USER")
				.regexMatchers(HttpMethod.DELETE, "/purchases/.{1,}").hasRole("USER")
				
				.antMatchers("/**").permitAll().and()
				.formLogin();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(usersService);
		return daoAuthenticationProvider;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET, POST, PUT, DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}