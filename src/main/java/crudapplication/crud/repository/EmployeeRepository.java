package crudapplication.crud.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import crudapplication.crud.model.Employee;

/**
 * EmployeeRepository interface to utilize the jpaRepository interface methods.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	
	/**
	 * Find Employee by email id.
	 *
	 * @param email to retrieve Employee from the database
	 * @return the optional of Employee
	 */
	Optional<Employee> findByEmail(String email);
}
