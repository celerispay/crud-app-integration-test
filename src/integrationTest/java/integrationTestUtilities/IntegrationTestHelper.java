package integrationTestUtilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.authentication.BasicAuthScheme;

public class IntegrationTestHelper {

	public static final String USERNAME = "Vaibhav";
	public static final String PASSWORD = "admin123";
	public static final String SAVE = "save";
	public static final String LIST = "list";
	public static final String UPDATE = "update";
	public static final String DUMMY_NAME = "tom";
	public static final String ERROR = "errors";
	public static final String ROLES = "roles";
	public static final String USERNAME_VAR = "username";
	public static final String NAME_VAR = "name";
	public static final String EMAIL = "email";
	public static final String DUMMY_EMAIL = "ankit@gmail.com";
	public static final String SALARY_VAR = "salary";
	public static final String WRONG_SALARY = "ABCD";
	public static final String EMPTY_NAME_ERROR = "Please provide a Name";
	public static final String EMPTY_EMAIL_ERROR = "Please provide a unique Email id";
	public static final String SAME_EMAIL_ERROR = "Employee with this email id Already Exists";
	public static final String EMPTY_DESIGNATION_ERROR = "Please provide a Designation";
	public static final String INVALID_SALARY_ERROR = "Salary can't be less than 10000";
	public static final String JSON_UNRECOGNIZED_TOKEN_ERROR = "JSON parse error: Unrecognized token '%s'";
	public static final String JSON_DESERIALIZE_ERROR = "JSON parse error: Cannot deserialize value of type `long` from String \"%s\"";
	public static final String INVALID_GENDER_ERROR = "Please provide a gender M/F";
	public static final String EMPTY_GENDER_ERROR = "gender can't be empty";
	public static final String EMPTY_ADDRESS_ERROR = "Please provide a address";
	public static ObjectMapper objectMapper = new ObjectMapper();
	public static final String INVALID_ROLE_ERROR = "Invalid role";
	public static final String UNAUTHORIZED_ERROR = "Unauthorized";
	public static final String EMPTY_USERNAME_ERROR = "username can't be empty, only unique username";
	public static final String USER_SAME_EMAIL_ERROR = "User with this email id Already Exists";
	public static final String MINIMUM_PASSWORD_LENGTH_ERROR = "Should contain atleast 8 digits";
	public static final String MINIMUM_ROLE_ERROR = "employee should have atleast 1 role";
	public static final String REQUEST_BODY_WITH_SALARY_IN_UNKNOWN_TOKEN = "{\r\n" + "		\"id\": 5,\r\n"
			+ "		\"name\": \"disha\",\r\n" + "		\"email\": \"disha@gmail.com\",\r\n"
			+ "		\"designation\" : \"HR\",\r\n" + "		\"salary\" : ABCD,\r\n" + "		\"gender\" : \"F\",\r\n"
			+ "		\"address\": \"Mathura\"\r\n" + "	}";

	public static JSONObject jsonToMap(String fileName) {
		Path path = Paths.get("src", "integrationTest", "resources", "jsonFiles", fileName);
		JSONObject jsonMap = null;
		try {
			jsonMap = new JSONObject(new String(Files.readAllBytes(path)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonMap;
	}

	public static BasicAuthScheme getBasic() {
		BasicAuthScheme basicAuthScheme = new BasicAuthScheme();
		basicAuthScheme.setUserName(USERNAME);
		basicAuthScheme.setPassword(PASSWORD);
		return basicAuthScheme;
	}

	public static String readFile(String fileName) throws IOException {
		Path path = Paths.get("src", "integrationTest", "resources", "jsonSchemas", fileName);
		byte[] fileData = Files.readAllBytes(path);
		String fileContent = new String(fileData);
		return fileContent;
	}
}
