package by.itacademy.javaenterprise.knyazev.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import by.itacademy.javaenterprise.knyazev.dao.UsersDAO;
import by.itacademy.javaenterprise.knyazev.entities.User;
import by.itacademy.javaenterprise.knyazev.security.SecurityUser;
import by.itacademy.javaenterprise.knyazev.security.converters.UsersConverter;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;


@Service
public class SecurityUsersService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(SecurityUsersService.class);
	
	private UsersDAO usersDAO;
	
	@Autowired
	public SecurityUsersService(UsersDAO usersDAO) {
		this.usersDAO = usersDAO;
	}

	@Override
	public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = usersDAO.findByName(username);

		if (user == null) {
			logger.error("User with name {} not found!", username);
			throw new UsernameNotFoundException("User with name " + username + " not found!");
		}
		
		SecurityUser securityUser = new UsersConverter().convertFromOneToTwo(user);
		return securityUser;
	}
	
	SecurityUser getSecurityUserFromSecurityContext() throws ServiceException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			logger.error("Not authenticated!");
			throw new ServiceException("Not authenticated!");
		}
		
		return (SecurityUser) authentication.getPrincipal();
	}

}
