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
import crudapplication.crud.enums.ResponseEnum;
import crudapplication.crud.exception.ApplicationException;
import crudapplication.crud.model.Employee;
import crudapplication.crud.service.EmployeeService;

/**
 * The Class EmployeeViewController used to work with {@link Employee} entity
 * and provide view pages accordingly.
 */
@Controller()
@RequestMapping("/employees")
public class EmployeeViewController {

	private static final String PAGE_ACTION = "action";
	private EmployeeService employeeService;
	
	/**
	 * Instantiates a new employee view controller using the {@link EmployeeService} object
	 *
	 * @param employeeService the {@link EmployeeService} 
	 */
	public EmployeeViewController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	/**
	 * Gets the list of all {@link Employee} view page.
	 *
	 * @param model the interface that defines a holder for model attributes
	 * @return the view page to list all {@link Employee}
	 */
	@GetMapping("/list")
	public String getEmployees(Model model) {
		List<Employee> employees = employeeService.getAllEmployee();
		model.addAttribute("employees", employees);
		return "employees/list-employees";
	}

	/**
	 * It display a view page to add a new {@link Employee}.
	 *
	 * @param model the interface that defines a holder for model attributes
	 * @return the view page to add a new {@link Employee}.
	 */
	@GetMapping("/add-form")
	public String addForm(Model model) {
		Employee theEmployee = new Employee();
		model.addAttribute("employee", theEmployee);
		model.addAttribute(PAGE_ACTION, "add");
		return AppConstants.LINK_TO_EMPLOYEE_FORM;
	}

	/**
	 * It display a view page to update an existing {@link Employee}.
	 *
	 * @param email the valid email id that already exists in the database.
	 * @param model the interface that defines a holder for model attributes
	 * @return the view page to update an existing employee.
	 */
	@GetMapping("/update-form")
	public String updateForm(@RequestParam("email") String email, Model model) {
		Employee theEmployee = employeeService.getEmployee(email)
											  .orElseThrow(() -> new ApplicationException(ResponseEnum.EMPLOYEE_NOT_FOUND));
		model.addAttribute("employee", theEmployee);
		model.addAttribute(PAGE_ACTION, "update");
		return AppConstants.LINK_TO_EMPLOYEE_FORM;
	}

	/**
	 * Save new {@link Employee} object to the database. 
	 *
	 * @param employee the {@link Employee} object that need to save in database
	 * @param result the result that adds binding-specific analysis and model building. 
	 * @param model the interface that defines a holder for model attributes
	 * @return the list of all {@link Employee} view page if successful, otherwise
	 * redirected to add employee view page if {@link Employee} is not valid.
	 */
	@PostMapping("/save")
	public String saveEmployee(@ModelAttribute("employee") @Valid Employee employee, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute(PAGE_ACTION, "add");
			return AppConstants.LINK_TO_EMPLOYEE_FORM;
		}
		employeeService.saveEmployee(employee);
		return AppConstants.REDIRECT_TO_EMPLOYEES_LIST;
	}

	/**
	 * Update existing {@link Employee} object to the database. 
	 *
	 * @param employee the {@link Employee} object that need to updated in database
	 * @param result the result that adds binding-specific analysis and model building. 
	 * @param model the interface that defines a holder for model attributes
	 * @return the list of all {@link Employee} view page if successful, otherwise
	 * redirected to update employee view page if {@link Employee} is not valid.
	 */
	@PostMapping("/update")
	public String updateEmployee(@ModelAttribute("employee") @Valid Employee employee, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute(PAGE_ACTION, "update");
			return AppConstants.LINK_TO_EMPLOYEE_FORM;
		}
		employeeService.updateEmployee(employee);
		return AppConstants.REDIRECT_TO_EMPLOYEES_LIST;
	}

	/**
	 * Delete existing employee from the database.
	 *
	 * @param email the valid email id that exists in the database
	 * @return the list of all existing employees after deleting the given {@link Employee} object
	 */
	@GetMapping("/delete")
	public String delete(@RequestParam("email") String email) {
		employeeService.deleteEmployee(email);
		return AppConstants.REDIRECT_TO_EMPLOYEES_LIST;
	}

}
