package by.itacademy.javaenterprise.knyazev.controllers;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import by.itacademy.javaenterprise.knyazev.dto.LoginUserDTO;
import by.itacademy.javaenterprise.knyazev.dto.TokenDTO;
import by.itacademy.javaenterprise.knyazev.dto.UserDTO;
import by.itacademy.javaenterprise.knyazev.entities.User;
import by.itacademy.javaenterprise.knyazev.exceptions.responses.ErrorMessage;
import by.itacademy.javaenterprise.knyazev.jwt.JwtUtil;
import by.itacademy.javaenterprise.knyazev.mappers.UserMapper;
import by.itacademy.javaenterprise.knyazev.services.UsersService;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;

@RestController
@Validated
public class AuthController {

	@Value("${authorizatin.token.timelife}")
	private String TOKEN_TIME_LIFE;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	UsersService usersService;
	
	@Autowired
	UserMapper userMapperImpl;	

	@PostMapping("/login")
	public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginUserDTO loginUserDTO) {
		String token = null;

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUserDTO.getUsername(), loginUserDTO.getPassword()));
		
		if (authentication.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
			token = jwtUtil.generateJWT(authentication);
		}

		return new ResponseEntity<TokenDTO>(new TokenDTO(token, TOKEN_TIME_LIFE), HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody UserDTO userDTO) {
		User user = userMapperImpl.toUser(userDTO);

		try {
			User savedUser = usersService.saveUser(user);
			String message = "User " + savedUser.getName() + " successfully created! Please login with /login";
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (ServiceException e) {
			return new ResponseEntity<ErrorMessage>(
					new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}

	}
	
}