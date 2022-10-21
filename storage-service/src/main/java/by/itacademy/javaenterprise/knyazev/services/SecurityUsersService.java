package by.itacademy.javaenterprise.knyazev.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import by.itacademy.javaenterprise.knyazev.dao.UsersDAO;
import by.itacademy.javaenterprise.knyazev.entities.User;
import by.itacademy.javaenterprise.knyazev.security.SecurityUser;
import by.itacademy.javaenterprise.knyazev.security.SecurityUserFactory;


@Service
public class SecurityUsersService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(SecurityUsersService.class);
	
	@Autowired
	private UsersDAO usersDAO;

	@Override
	public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = usersDAO.findByName(username);

		if (user == null) {
			logger.error("User with name {} not found!", username);
			throw new UsernameNotFoundException("User with name " + username + " not found!");
		}
		
		SecurityUser securityUser = new SecurityUserFactory(user).create();
		return securityUser;
	}

}
