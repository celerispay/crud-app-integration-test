package crudapplication.crud.exception;


import crudapplication.crud.enums.ResponseEnum;
import lombok.Getter;

/**
 * ApplicationException is used to create custom runtime exception.
 */
@Getter
public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final int statusCode;
	private final String message;
     
	/**
	 * Instantiates a new application exception.
	 *
	 * @param responseEnum to provide exception type and status 
	 */
	public ApplicationException(ResponseEnum responseEnum) {
		this.statusCode = responseEnum.getResponseCode();
		this.message = responseEnum.getResponseMessage();
	}
    
}
