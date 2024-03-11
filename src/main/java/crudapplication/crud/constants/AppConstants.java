package crudapplication.crud.constants;

/**
 * AppConstants class for url of pages.
 */
public class AppConstants {
	
	private AppConstants() {
	    throw new IllegalStateException("Constants class can't be Instantiated!");
	}
	
	public static final String REDIRECT_TO_EMPLOYEES_LIST = "redirect:/employees/list";
	public static final String REDIRECT_TO_USERS_LIST = "redirect:/users/list";
	
	public static final String LINK_TO_EMPLOYEE_FORM = "employees/employee-form";
	public static final String LINK_TO_USER_FORM = "users/user-form";
}
