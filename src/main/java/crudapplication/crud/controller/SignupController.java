package crudapplication.crud.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import crudapplication.crud.configuration.EnableDocumentation;
import crudapplication.crud.configuration.GlobalApiReponses;
import crudapplication.crud.model.User;
import crudapplication.crud.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * SignupController class provide interface to interact with the {@link User} entity.
 */
@RestController
@RequestMapping("/signup")
@EnableDocumentation
@Api(value = "SignupRestController", description = "REST APIs related to User Entity!!!!")
@GlobalApiReponses
public class SignupController {

	UserService service;

	/**
	 * Instantiates a new {@link SignupController} using {@link UserService} object.
	 *
	 * @param service the {@link UserService} object
	 */
	public SignupController(UserService service) {
		this.service = service;
	}
	
	/**
	 * Gets all users from the database.
	 *
	 * @return List of all users.
	 */
	@GetMapping("/list")
	@ApiOperation(value = "Get list of Users from the System ", response = Iterable.class)
	List<User> getAllUser() {
		return service.getAllUser();
	}

	/**
	 * Gets user on the basis of username provided.
	 *
	 * @param username the existing username to fetch
	 * @return the user on the basis of username provided
	 */
	@GetMapping("/{username}")
	@ApiOperation(value = "Get User on the basis of username", response = User.class)
	User getByUsername(@PathVariable String username) {
		return service.getUser(username);
	}

	/**
	 * Adds the valid {@link User} to the database.
	 *
	 * @param user the valid {@link User} that does not already exist
	 * @return the persisted {@link User}
	 */
	@PostMapping("/save")
	@ApiOperation(value = "Add new User to the System ", response = User.class)
	User addUser(@Valid @RequestBody User user) {
		return service.saveUser(user);
	}

	/**
	 * Update the valid {@link User} in the database.
	 *
	 * @param user the valid {@link User} that already exist
	 * @return the updated user
	 */
	@PutMapping("/update")
	@ApiOperation(value = "Update existing User in the System ", response = User.class)
	User updateUser(@Valid @RequestBody User user) {
		return service.updateUser(user);
	}

	/**
	 * Delete existing {@link User} in the database by username.
	 *
	 * @param username the valid username that exists in the database
	 */
	@DeleteMapping("/{username}")
	@ApiOperation(value = "Delete existing User from the System ")
	void deleteByUsername(@PathVariable String username) {
		service.deleteUser(username);
	}
}
