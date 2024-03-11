package crudapplication.crud.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import crudapplication.crud.model.User;
import crudapplication.crud.repository.UserRepository;

/**
 * MyUserDetailsService provides interface which loads user-specific data.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Get <code>UserDetails</code> object from username.
	 *
	 * @param username name of the user
	 * @return the <code>UserDetails</code> object for that username
	 * @throws UsernameNotFoundException in case username does not exist
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);
		return user.map(MyUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("Username Not Found: " + username));
	}
	
}
