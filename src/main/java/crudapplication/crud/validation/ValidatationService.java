package crudapplication.crud.validation;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import crudapplication.crud.enums.ResponseEnum;
import crudapplication.crud.enums.Roles;
import crudapplication.crud.exception.ApplicationException;
import crudapplication.crud.model.User;
import lombok.extern.log4j.Log4j2;

/**
 * ValidatationService used to validate user roles
 */
@Log4j2
@Service
public class ValidatationService {
	
	/**
	 * Validates user roles.
	 *
	 * @param user entity object
	 * @return string roles in csv format
	 * @throws ApplicationException if valid roles are not passed to the user roles.
	 */
	public String validateUserRoles(User user) {
		return Arrays.stream(user.getRoles().split(","))
					 .map(String::trim)
					 .map(String::toUpperCase)
					 .distinct()
					 .filter(this::containsRole)
					 .collect(Collectors.joining(","));
	}

	private boolean containsRole(String checkRole) {
		boolean matched = Stream.of(Roles.values()).anyMatch(role -> role.name().equals(checkRole));
		if(!matched) {
			log.info("{} is not a valid role", checkRole);
			throw new ApplicationException(ResponseEnum.ROLE_NOT_FOUND);			
		}
		return matched;
	}
}
