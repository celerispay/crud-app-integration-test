package crudapplication.crud.exception;

import java.util.stream.Collectors;

import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import crudapplication.crud.util.UtilityService;
import lombok.extern.log4j.Log4j2;

/**
 * CustomGlobalExceptionHandler provide centralized exception handling across all
 * {@code @RequestMapping} methods through {@code @ExceptionHandler} methods.
 */
@ControllerAdvice
@Log4j2
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Handles the response for MethodArgumentNotValidException.
	 * @param ex the exception
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
									 HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiError body = getErrorBody(ex, status);
		// special case: providing modified errors
		body.setErrors(ex.getBindingResult().getFieldErrors().stream()
							 .map(DefaultMessageSourceResolvable::getDefaultMessage)
						   	 .collect(Collectors.joining(", ")));
		return new ResponseEntity<>(body, headers, status);
	}

	/**
	 * Handles the response for HttpRequestMethodNotSupportedException.
	 * @param ex the exception
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
									 HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiError body = getErrorBody(ex, status);
		return new ResponseEntity<>(body, headers, status);
	}

	/**
	 * Handles the response for TypeMismatchException.
	 * @param ex the exception
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
									 HttpStatus status, WebRequest request) {
		ApiError body = getErrorBody(ex, status);
		return new ResponseEntity<>(body, headers, status);
	}

	/**
	 * Handles the response for HttpMessageNotReadableException.
	 * @param ex the exception
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
									 HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiError body = getErrorBody(ex, status);
		return new ResponseEntity<>(body, headers, status);
	}
	
	/**
	 * Handles the response for all Exception.
	 * @param ex the exception
	 * @return a {@code ResponseEntity} instance
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllException(Exception ex) {
		ApiError body = getErrorBody(ex, HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles the response for InternalServerError.
	 * @param ex the exception
	 * @return a {@code ResponseEntity} instance
	 */
	@ExceptionHandler(InternalServerError.class)
	public ResponseEntity<Object> handleAllInternalException(InternalServerError ex) {
		ApiError body = getErrorBody(ex, HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles the response for custom Application Exception.
	 * @param ex the exception
	 * @return a {@code ResponseEntity} instance
	 */
	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<Object> handleCustomException(ApplicationException ex) {
		ApiError body = getErrorBody(ex, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}
	
	
	/**
	 * It creates error response
	 * @param ex Exception
	 * @param status HttpStatus
	 * @return {@link ApiError}
	 */
	private ApiError getErrorBody(Exception ex, HttpStatus status) {
		ApiError apiError = new ApiError(UtilityService.getCurrentDateFormat(), status.value(), ex.getMessage());
		log.error("Exception: " + ex.getMessage());
		return apiError;
	}
	
}