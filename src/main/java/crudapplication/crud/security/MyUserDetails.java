package crudapplication.crud.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import crudapplication.crud.model.User;

/**
 * MyUserDetails provides user information.
 */
public class MyUserDetails implements UserDetails {

	private static final long serialVersionUID = -5256533164560516083L;
	
	private String username;
	private String password;
	private List<GrantedAuthority> authorities;

	public MyUserDetails() {
	}

	/**
	 * Instantiates a new MyUserDetails by mapping User entity to UserDetails
	 *
	 * @param user
	 */
	public MyUserDetails(User user) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.authorities = Arrays.stream(user.getRoles().split(","))
				.map(role -> "ROLE_" + role)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	/**
	 * Gets the list of granted authorities.
	 *
	 * @return the list of granted authorities
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * Checks if account not expired.
	 *
	 * @return true, if account not expired
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * Checks if account not locked.
	 *
	 * @return true, if account not locked.
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * Checks if credentials not expired.
	 *
	 * @return true, if credentials not expired
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * Checks if account is enabled.
	 *
	 * @return true, if account is enabled
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

}
