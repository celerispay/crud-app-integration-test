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
import crudapplication.crud.enums.ResponseEnum;
import crudapplication.crud.exception.ApplicationException;
import crudapplication.crud.model.Employee;
import crudapplication.crud.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * EmployeeController class provide interface to interact with the {@link Employee} entity.
 */
@RestController
@RequestMapping("/employee")
@EnableDocumentation
@Api(value = "EmployeeRestController", description = "REST APIs related to Employee Entity!!!!")
@GlobalApiReponses
public class EmployeeController {

	private EmployeeService service;
	
	/**
	 * Instantiates a new employee controller using {@link EmployeeService} object.
	 *
	 * @param service the {@link EmployeeService} object
	 */
	public EmployeeController(EmployeeService service) {
		this.service = service;
	}

	/**
	 * Gets all employees from the database.
	 *
	 * @return List of all employees.
	 */
	@GetMapping("/list")
	@ApiOperation(value = "Get list of Employees from the System ", response = Iterable.class)
	List<Employee> getAllEmployee() {
		return service.getAllEmployee();
	}

	/**
	 * Gets employee on the basis of email id provided.
	 *
	 * @param email the existing email id to fetch
	 * @return the employee on the basis of email id provided
	 */
	@GetMapping("/{email}")
	@ApiOperation(value = "Get Employee on the basis of email", response = Employee.class)
	Employee getByEmail(@PathVariable String email) {
		return service.getEmployee(email).orElseThrow(() -> new ApplicationException(ResponseEnum.EMPLOYEE_NOT_FOUND));
	}

	/**
	 * Adds the valid {@link Employee} to the database.
	 *
	 * @param employee the valid {@link Employee} that does not already exist
	 * @return the persisted {@link Employee}
	 */
	@PostMapping("/save")
	@ApiOperation(value = "Add new Employee to the System ", response = Employee.class)
	Employee addEmployee(@Valid @RequestBody Employee employee) {
		return service.saveEmployee(employee);
	}

	/**
	 * Update the valid {@link Employee} in the database.
	 *
	 * @param employee the valid {@link Employee} that already exist
	 * @return the updated employee
	 */
	@PutMapping("/update")
	@ApiOperation(value = "Update existing Employee in the System ", response = Employee.class)
	Employee updateEmployee(@Valid @RequestBody Employee employee) {
		return service.updateEmployee(employee);
	}

	/**
	 * Delete existing {@link Employee} in the database by email.
	 *
	 * @param email the valid email id that exists in the database
	 */
	@DeleteMapping("/{email}")
	@ApiOperation(value = "Delete existing Employee from the System ")
	void deleteByEmail(@PathVariable String email) {
		service.deleteEmployee(email);
	}
}
