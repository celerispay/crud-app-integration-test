package crudapplication.crud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import crudapplication.crud.enums.ResponseEnum;
import crudapplication.crud.exception.ApplicationException;
import crudapplication.crud.model.User;
import crudapplication.crud.repository.UserRepository;
import crudapplication.crud.validation.ValidatationService;
import lombok.extern.log4j.Log4j2;

/**
 * User Service provides an interface
 * to use User Entity.
 *
 * @author Rahul
 * @version 1.0
 */
@Service
@Log4j2
public class UserService {

	private final UserRepository repository;
	
	private final ValidatationService validatationService;

	/**
	 * Instantiates a new user service.
	 *
	 * @param required a UserRepository object
	 * @param required a ValidatationService object
	 */
	@Autowired
	public UserService(UserRepository repository, ValidatationService validatationService) {
		this.repository = repository;
		this.validatationService = validatationService;
	}
	
	/**
	 * Gets all user from database.
	 *
	 * @return list of users
	 */
	public List<User> getAllUser() {
		log.info("Getting all users from the database");
		List<User> allUsers = repository.findAll();
		log.info("Total users received: {}", allUsers.size());
		return allUsers;
	}
	
	
	/**
	 * Gets user from existing username
	 *
	 * @param username that exists in database 
	 * @return the user from database
	 */
	public User getUser(String username) {
		log.info("Getting a user from the database with user name: {}", username);
		return repository.findByUsername(username)
						 .orElseThrow(() -> new ApplicationException(ResponseEnum.USER_NOT_FOUND));
	}

	/**
	 * Save user to database.
	 *
	 * @param user with unique username
	 * @return the prersised user 
	 * @throws ApplicationException in case the given {@literal username} is already present in database.
	 */
	public User saveUser(User user) {
		user.setUsername(user.getUsername().toLowerCase());
		if (repository.findByUsername(user.getUsername()).isPresent()) {
			log.error("user already exists in the database with username: {}", user.getUsername());
			throw new ApplicationException(ResponseEnum.USER_ALREADY_EXISTS);
		}
		log.info("Validating user roles");
		user.setRoles(validatationService.validateUserRoles(user));
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		log.info("Persiting new user to database");
		return repository.save(user);
	}

	/**
	 * Update user in database.
	 *
	 * @param user that need to be updated
	 * @return the updated user
	 * @throws ApplicationException in case the given {@literal username} is not present in database.
	 */
	public User updateUser(User user) {
		Optional<User> userExists = repository.findByUsername(user.getUsername()); 
		user.setUsername(user.getUsername().toLowerCase());

		log.info("checking if username:{} exist", user.getUsername());
		if (userExists.isEmpty()) {
			log.error("user doesn't exist in the database with username: {}", user.getUsername());
			throw new ApplicationException(ResponseEnum.USER_NOT_FOUND);
		}
		User existingUser = userExists.get();

		log.info("Validating user roles");
		user.setRoles(validatationService.validateUserRoles(user));
		// set id to existing employee so that .save() doesn't create new user
		user.setId(existingUser.getId());
		if (!existingUser.getPassword().equals(user.getPassword()))
			user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		log.info("Updating user with user name: {}", user.getUsername());
		return repository.save(user);

	}

	/**
	 * Delete user in the database.
	 *
	 * @param username of existing user
	 * @throws ApplicationException in case the given {@literal username} is not present in database.
	 */
	public void deleteUser(String username) {
		User deleteUser = repository.findByUsername(username)
									.orElseThrow(() -> new ApplicationException(ResponseEnum.USER_NOT_FOUND));
		log.warn("Deleting user with user name : {}", (username));
		repository.delete(deleteUser);
	}

}
