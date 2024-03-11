package crudapplication.crud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import crudapplication.crud.enums.ResponseEnum;
import crudapplication.crud.exception.ApplicationException;
import crudapplication.crud.model.Employee;
import crudapplication.crud.repository.EmployeeRepository;
import lombok.extern.log4j.Log4j2;

/**
 * Employee Service provides an interface
 * to use Employee Entity
 * 
 * @author Rahul
 * @version 1.0
 */

@Service
@Log4j2
public class EmployeeService {

	EmployeeRepository repository;
	
	/**
	 * Instantiates a new employee service.
	 *
	 * @param repository of EmployeeRepository object
	 */
	public EmployeeService(EmployeeRepository repository) {
		this.repository = repository;
	}
	
	/**
	 * This method is used to retrieve
	 * all employees from database
	 * 
	 * @return List of all employees
	 */
	public List<Employee> getAllEmployee() {
		log.info("Getting all employee from the database");
		List<Employee> allEmployees = repository.findAll();
		log.info("Total employees received: {}", allEmployees.size());
		return allEmployees;
	}

	/**
	 * It retrieve employee on the basis of valid email id 
	 * 
	 * @param email - provide a valid email id
	 * @return Optional employee 
	 */	
	public Optional<Employee> getEmployee(String email) {
		log.info("Getting a employee from the database with email id: {}", email);
		return repository.findByEmail(email);
	}

	/**
	 * Persist the employee to database
	 * 
	 * @param employee - unique email id should be passed
	 * @return the persisted employee is returned
	 * @throws ApplicationException in case the given {@literal email} is already present in database.
	 */
	public Employee saveEmployee(Employee employee) {
		employee.setId(0);
		Optional<Employee> employeeExists = repository.findByEmail(employee.getEmail()); 
		if (employeeExists.isPresent()) {
			log.error("Employee email id already exists: {}", employee.getEmail());
			throw new ApplicationException(ResponseEnum.EMPLOYEE_ALREADY_EXISTS);
		}
		log.info("Persiting new employee into database");
		return repository.save(employee);
	}
	
	/**
	 * Modify the existing employee
	 * 
	 * @param employee - should exists in database
	 * @return optional of employee
	 * @throws ApplicationException in case the given {@literal email} is not present in database.
	 */
	public Employee updateEmployee(Employee employee) {
		Optional<Employee> employeeExists = repository.findByEmail(employee.getEmail()); 
		if (employeeExists.isEmpty()) {
			log.error("Employee doesn't exist in the database with email id: {}", employee.getEmail());
			throw new ApplicationException(ResponseEnum.EMPLOYEE_NOT_FOUND);
		}
		log.info("Updating employee with email id: {}", employee.getEmail());
		Employee existingEmployee = employeeExists.get();
		employee.setId(existingEmployee.getId());
		return repository.save(employee);
	}

	/**
	 * Delete the employee on the basis of existing email id
	 * 
	 * @param email id already existing
	 * @throws ApplicationException in case the given {@literal email} is not present in database.
	 */
	public void deleteEmployee(String email) {
		Employee deleteEmployee = repository.findByEmail(email)
											.orElseThrow(() -> new ApplicationException(ResponseEnum.EMPLOYEE_NOT_FOUND));
		log.warn("Deleting employee with email id: {}", email);
		repository.delete(deleteEmployee);
	}
	

}
