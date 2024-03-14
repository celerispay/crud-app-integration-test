package services;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EmployeeHelper {

	public static String wrongSalary = "ABCD";

	public static String requestBodyWithSalaryInUnknownToken = "{\r\n" + "		\"id\": 5,\r\n"
			+ "		\"name\": \"disha\",\r\n" + "		\"email\": \"disha@gmail.com\",\r\n"
			+ "		\"designation\" : \"HR\",\r\n" + "		\"salary\" : ABCD,\r\n" + "		\"gender\" : \"F\",\r\n"
			+ "		\"address\": \"Mathura\"\r\n" + "	}";

	public static Map<Object, Object> jsonToMap(String path)
			throws JsonParseException, JsonMappingException, IOException {
		File jsonFile = new File(
				"C:\\Users\\Admin\\eclipse-workspace\\crud-application\\src\\integrationTest\\resources\\jsonFiles\\"
						+ path);
		ObjectMapper objectMapper = new ObjectMapper();
		Map<Object, Object> jsonMap = objectMapper.readValue(jsonFile, Map.class);
		return jsonMap;
	}

}
