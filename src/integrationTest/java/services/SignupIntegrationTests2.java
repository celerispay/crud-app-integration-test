package services;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import crudapplication.crud.CrudApplication;
import crudapplication.crud.controller.EmployeeController;
import crudapplication.crud.service.EmployeeService;
import enums.Enums;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import static org.hamcrest.Matchers.*;

import javax.activation.DataSource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = CrudApplication.class)
@ActiveProfiles("integrationTest")
@TestMethodOrder(OrderAnnotation.class)
public class SignupIntegrationTests2 {

	@LocalServerPort
	private int port;
	

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
		RequestSpecification rq = new RequestSpecBuilder().setBaseUri("http://localhost:" + port + "/crud-app/signup/").addHeader("Content-Type", "application/json").build();
		RestAssured.requestSpecification = rq;
		RestAssured.authentication = RestAssured.basic(Enums.USERNAME.actualValue, Enums.PASSWORD.actualValue);
		
		
	}

	@Test
	@Order(13)
	public void saveUserTest() {
		String requestBody = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Tom\",\r\n"
				+ "		\"password\": \"12345678\",\r\n" + "		\"roles\" : \"ADMIN\"\r\n" + "}";

		RestAssured.given().log().all()
				.when().body(requestBody).post(Enums.SAVE.actualValue)
				.then().log().all().statusCode(HttpStatus.OK.value())
				.body(Enums.USERNAME_VAR.actualValue, equalTo(Enums.DUMMY_NAME.actualValue), Enums.ROLES.actualValue, equalTo("ADMIN"));
	}
	
	@Test
	@Order(14)
	public void saveUserTestWithThreeRoles() {
		String requestBody = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Tom\",\r\n"
				+ "		\"password\": \"12345678\",\r\n" + "		\"roles\" : \"ADMIN,USER,SERVER_ADMIN\"\r\n" + "}";

		RestAssured.given().log().all()
				.when().body(requestBody).post(Enums.SAVE.actualValue)
				.then().log().all().statusCode(HttpStatus.OK.value())
				.body(Enums.USERNAME_VAR.actualValue, equalTo(Enums.DUMMY_NAME.actualValue), Enums.ROLES.actualValue, equalTo("ADMIN,USER,SERVER_ADMIN"));
	}

	@Test
	@Order(15)
	public void saveUserTestWithRoleOtherThanCSV() {
		String requestBody = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Tom\",\r\n"
				+ "		\"password\": \"12345678\",\r\n" + "		\"roles\" : \"EMPLOYEE\"\r\n" + "}";

		RestAssured.given().log().all()
				.when().body(requestBody).post(Enums.SAVE.actualValue)
				.then().log().all().statusCode(400).body(Enums.ERROR.actualValue, equalTo("Invalid role"));
	}

	@Test
	@Order(16)
	public void saveUserTestWithWrongCredentials() {
		RestAssured.reset();
		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost:" + port + "/crud-app/signup/";
		String requestBody = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Tom\",\r\n"
				+ "		\"password\": \"12345678\",\r\n" + "		\"roles\" : \"ADMIN\"\r\n" + "}";

		RestAssured.given().log().all().auth().basic(Enums.USERNAME.actualValue, "admin12X")
				.when().body(requestBody).post(Enums.SAVE.actualValue)
				.then().log().all().statusCode(401).body("error", equalTo("Unauthorized"));
	}

	@Test
	@Order(17)
	public void saveUserTestWithBlankUsername() {
		String requestBody = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"\",\r\n"
				+ "		\"password\": \"12345678\",\r\n" + "		\"roles\" : \"ADMIN\"\r\n" + "}";

		RestAssured.given().log().all()
				.when().body(requestBody).post(Enums.SAVE.actualValue)
				.then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("username can't be empty, only unique username"));
	}

	@Test
	@Order(18)
	public void saveUserTestWithSameUsername() {
		String requestBody = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"vaibhav\",\r\n"
				+ "		\"password\": \"12345678\",\r\n" + "		\"roles\" : \"ADMIN\"\r\n" + "}";

		RestAssured.given().log().all()
				.when().body(requestBody).post(Enums.SAVE.actualValue)
				.then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("User with this email id Already Exists"));
	}
	
	@Test
	@Order(19)
	public void saveUserTestWithPasswordLessThan8Char() {
		String requestBody = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Tom\",\r\n"
				+ "		\"password\": \"1234567\",\r\n" + "		\"roles\" : \"ADMIN\"\r\n" + "}";

		RestAssured.given().log().all()
				.when().body(requestBody).post(Enums.SAVE.actualValue)
				.then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("Should contain atleast 8 digits"));
	}
	
	@Test
	@Order(20)
	public void saveUserTestWithPasswordLength7DigitWithSpaceAtEnd() {
		String requestBody = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Tom\",\r\n"
				+ "		\"password\": \"1234567 \",\r\n" + "		\"roles\" : \"ADMIN\"\r\n" + "}";

		RestAssured.given().log().all()
				.when().body(requestBody).post(Enums.SAVE.actualValue)
				.then().log().all().statusCode(200)
				.body(Enums.USERNAME_VAR.actualValue, equalTo(Enums.DUMMY_NAME.actualValue), Enums.ROLES.actualValue, equalTo("ADMIN"));
	}
	
	@Test
	@Order(21)
	public void saveUserTestWithPasswordLength7DigitWithSpaceAtStart() {
		String requestBody = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Tom\",\r\n"
				+ "		\"password\": \" 1234567 \",\r\n" + "		\"roles\" : \"ADMIN\"\r\n" + "}";

		RestAssured.given().log().all()
				.when().body(requestBody).post(Enums.SAVE.actualValue)
				.then().log().all().statusCode(200)
				.body(Enums.USERNAME_VAR.actualValue, equalTo(Enums.DUMMY_NAME.actualValue), Enums.ROLES.actualValue, equalTo("ADMIN"));
	}
	
	@Test
	@Order(22)
	public void saveUserTestWithBlankRole() {
		String requestBody = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Tom\",\r\n"
				+ "		\"password\": \"12345678\",\r\n" + "		\"roles\" : \"\"\r\n" + "}";

		RestAssured.given().log().all()
				.when().body(requestBody).post(Enums.SAVE.actualValue)
				.then().log().all().statusCode(400).body(Enums.ERROR.actualValue, equalTo("employee should have atleast 1 role"));
	}

	@Test
	@Order(23)
	public void updateUserTest() {
		String requestBody = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Tom\",\r\n"
				+ "		\"password\": \"12345678\",\r\n" + "		\"roles\" : \"USER\"\r\n" + "}";

		RestAssured.given().log().all()
				.when().body(requestBody).put(Enums.UPDATE.actualValue)
				.then().log().all().statusCode(HttpStatus.OK.value())
				.body(Enums.USERNAME_VAR.actualValue, equalTo(Enums.DUMMY_NAME.actualValue), Enums.ROLES.actualValue, equalTo("USER"));
	}

	@Test
	@Order(24)
	public void getUserTest() {
		RestAssured.given().log().all()
				.when().get(Enums.LIST.actualValue).then().log().all()
				.statusCode(HttpStatus.OK.value()).body(Enums.USERNAME_VAR.actualValue, hasItems(Enums.DUMMY_NAME.actualValue), Enums.ROLES.actualValue, hasItems("USER"));
	}

	@Test
	@Order(25)
	public void getUserWithUsernameTest() {
		String username = Enums.DUMMY_NAME.actualValue;
		RestAssured.given().log().all()
				.when().get(username).then().log()
				.all().statusCode(HttpStatus.OK.value()).body(Enums.USERNAME_VAR.actualValue, equalTo(Enums.DUMMY_NAME.actualValue), Enums.ROLES.actualValue, equalTo("USER"));
	}

	@Test
	@Order(26)
	public void deleteUserTest() {
		String username = Enums.DUMMY_NAME.actualValue;
		RestAssured.given().log().all()
				.when().delete(username).then().log()
				.all().statusCode(HttpStatus.OK.value());
	}
}
