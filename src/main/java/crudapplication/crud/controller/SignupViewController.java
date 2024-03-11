package crudapplication.crud.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import crudapplication.crud.constants.AppConstants;
import crudapplication.crud.model.User;
import crudapplication.crud.service.UserService;

/**
 * The Class SignupViewController used to work with {@link User} entity
 * and provide view pages accordingly.
 */
@Controller
@RequestMapping("/users")
public class SignupViewController {

	private static final String PAGE_ACTION = "action";
	private UserService userService;
	
	/**
	 * Instantiates a new signup view controller using the {@link UserService} object
	 *
	 * @param userService the {@link UserService} 
	 */
	public SignupViewController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Gets the list of all {@link User} view page.
	 *
	 * @param model the interface that defines a holder for model attributes
	 * @return the view page to list all {@link User}
	 */
	@GetMapping("/list")
	public String getUsers(Model model) {
		List<User> users = userService.getAllUser();
		model.addAttribute("users", users);
		return "users/list-users";
	}

	/**
	 * It display a view page to add a new {@link User}.
	 *
	 * @param model the interface that defines a holder for model attributes
	 * @return the view page to add a new {@link User}.
	 */
	@GetMapping("/add-form")
	public String addForm(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute(PAGE_ACTION, "add");
		return AppConstants.LINK_TO_USER_FORM;
	}

	/**
	 * It display a view page to update an existing {@link User}.
	 *
	 * @param username the valid username that already exists in the database.
	 * @param model the interface that defines a holder for model attributes
	 * @return the view page to update an existing user.
	 */
	@GetMapping("/update-form")
	public String updateForm(@RequestParam("username") String username, Model model) {
		User existingUser = userService.getUser(username);
		model.addAttribute("user", existingUser);
		model.addAttribute(PAGE_ACTION, "update");
		return AppConstants.LINK_TO_USER_FORM;
	}

	/**
	 * Save new {@link User} object to the database. 
	 *
	 * @param user the {@link User} object that need to save in database
	 * @param result the result that adds binding-specific analysis and model building. 
	 * @param model the interface that defines a holder for model attributes
	 * @return the list of all {@link User} view page if successful, otherwise
	 * redirected to add user view page if {@link User} is not valid.
	 */
	@PostMapping("/save")
	public String save(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute(PAGE_ACTION, "add");
			return AppConstants.LINK_TO_USER_FORM;
		}
		userService.saveUser(user);
		return AppConstants.REDIRECT_TO_USERS_LIST;
	}

	/**
	 * Update existing {@link User} object to the database.
	 *
	 * @param user the {@link User} object that need to updated in database
	 * @param result the result that adds binding-specific analysis and model building. 
	 * @param model the interface that defines a holder for model attributes
	 * @return the list of all {@link User} view page if successful, otherwise
	 * redirected to update user view page if {@link User} is not valid.
	 */
	@PostMapping("/update")
	public String update(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute(PAGE_ACTION, "update");
			return AppConstants.LINK_TO_USER_FORM;
		}
		userService.updateUser(user);
		return AppConstants.REDIRECT_TO_USERS_LIST;
	}

	/**
	 * Delete existing user from the database.
	 *
	 * @param username the valid username that exists in the database
	 * @return the list of all existing users after deleting the given {@link User} object
	 */
	@GetMapping("delete")
	public String delete(@RequestParam("username") String username) {
		userService.deleteUser(username);
		return AppConstants.REDIRECT_TO_USERS_LIST;
	}
}
