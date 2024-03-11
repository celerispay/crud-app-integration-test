package crudapplication.crud.enums;

import lombok.Getter;

/**
 * ResponseEnum is Constants class for 
 * various error messages and codes.
 */
@Getter
public enum ResponseEnum {
	ROLE_NOT_FOUND(101, "Invalid role"),
	USER_NOT_FOUND(104, "User with this user name Not Found"),
	USER_ALREADY_EXISTS(102, "User with this email id Already Exists"),
	EMPLOYEE_NOT_FOUND(103, "Employee with this email id Not Found"),
	EMPLOYEE_ALREADY_EXISTS(105, "Employee with this email id Already Exists");

	int responseCode;
	String responseMessage;

	ResponseEnum(int code, String msg) {
		responseCode = code;
		responseMessage = msg;
	}

}
