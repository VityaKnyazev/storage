package by.itacademy.javaenterprise.knyazev.services;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import by.itacademy.javaenterprise.knyazev.dao.UsersDAO;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;

@Service
public class UsersService implements UserDetailsService {
	@Autowired
	private UsersDAO usersDAO;
	private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		by.itacademy.javaenterprise.knyazev.entities.User user = usersDAO.findByName(username);

		if (user == null) {
			logger.error("User with name {} not found!", username);
			throw new UsernameNotFoundException("User with name " + username + " not found!");
		}
		UserDetails userDetails = new User(user.getName(), user.getPassword(),
				Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())));
		return userDetails;
	}

	by.itacademy.javaenterprise.knyazev.entities.User loadUserFromSecurityContext() throws ServiceException {
		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
				
		if (authentification == null || !authentification.isAuthenticated()) {
			logger.error("User is not authenticated!");
			throw new ServiceException("User is not authenticated!");
		}

		by.itacademy.javaenterprise.knyazev.entities.User user = usersDAO.findByName(authentification.getName());
		
		if (user == null) {
			logger.error("Only registered users can see added purchases. User with name {} is not registered!", authentification.getName());
			throw new ServiceException("Only registered users can see added purchases. User with name " + authentification.getName() + " is not registered!");
		}
		return user;
	}

}
