package by.itacademy.javaenterprise.knyazev.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import by.itacademy.javaenterprise.knyazev.entities.User;

public class SecurityUserFactory extends AbstractUserFactory {

	public SecurityUserFactory(User user) {
		super(user);
	}

	@Override
	public SecurityUser create() {
		Long id = user.getId();
		String name = user.getName();
		String password = user.getPassword();
		String email = user.getEmail();
		List<GrantedAuthority> grantedAuthority = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());
		
		return new SecurityUser(id, name, password, email, grantedAuthority);
	}

}