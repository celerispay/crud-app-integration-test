package crudapplication.crud.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * UtilityService provides utility method 
 * used at various points
 * 
 * @author Rahul
 * @version 1.0
 */
public class UtilityService {
	
	/**
	 * Can't instantiates a utility service.
	 * 
	 * @throws IllegalStateException because class can't be Instantiated!
	 */
	private UtilityService() {
	    throw new IllegalStateException("Utility class can't be Instantiated!");
	}

	/**
	 * Static method to gets the current date in a specific format.
	 *
	 * @return the current date in "dd/MM/yyyy HH:mm:ss" format.
	 */
	public static String getCurrentDateFormat() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(LocalDateTime.now());
	}

}
