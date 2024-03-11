package crudapplication.crud.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * ApiError is a simple DTO to provide an error message information
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ApiError {

	private String timestamp;
	private int status;
	private String errors;
}
