package by.itacademy.javaenterprise.knyazev.security;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import by.itacademy.javaenterprise.knyazev.entities.Role;

public class SecurityUser implements UserDetails {

	private static final long serialVersionUID = -3323043387582495788L;

	private long id;
	private String name;
	private String password;
	private String email;
	private Set<Role> roles;
	private Boolean enabled;
	private Collection<? extends GrantedAuthority> grantedAuthorities;

	public SecurityUser(long id, String name, String password, String email, Set<Role> roles, Boolean enabled, Collection<? extends GrantedAuthority> grantedAuthorities) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.email = email;
		this.roles = roles;
		this.enabled = enabled;
		this.grantedAuthorities = grantedAuthorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String getUsername() {
		return name;
	}

	public String getEmail() {
		return email;
	}
	
	public Set<Role> getRoles() {
		return roles;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
