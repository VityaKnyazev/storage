package by.itacademy.javaenterprise.knyazev.security.converters;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import by.itacademy.javaenterprise.knyazev.entities.Role;
import by.itacademy.javaenterprise.knyazev.entities.User;
import by.itacademy.javaenterprise.knyazev.security.SecurityUser;

public class UsersConverter extends AbstractConverter<User, SecurityUser>{

	public UsersConverter() {
		super(UsersConverter::convertFromUserToSecurityUser, UsersConverter::convertFromUserToSecurityUser);
	}
	
	
	private static final SecurityUser convertFromUserToSecurityUser(User user) {
		Long id = user.getId();
		String name = user.getName();
		String password = user.getPassword();
		String email = user.getEmail();
		Set<Role> roles = user.getRoles();
		Boolean enabled = user.getEnabled();
		List<GrantedAuthority> grantedAuthority = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());
		
		return new SecurityUser(id, name, password, email, roles, enabled, grantedAuthority);
	}
	

	private static final User convertFromUserToSecurityUser(SecurityUser securityUser) {
		Long id = securityUser.getId();
		String name = securityUser.getUsername();
		String password = securityUser.getPassword();
		String email = securityUser.getEmail();
		Boolean enabled = securityUser.isEnabled();
		Set<Role> roles = securityUser.getRoles();
		
		return new User(id, name, password, email, enabled, roles);		
	}

}
