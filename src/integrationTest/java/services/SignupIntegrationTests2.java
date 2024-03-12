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
		RequestSpecification rq = new RequestSpecBuilder().setBaseUri("http://localhost:" + port + "/crud-app/signup/")
				.addHeader("Content-Type", "application/json").build();
		RestAssured.requestSpecification = rq;
		RestAssured.authentication = RestAssured.basic(Enums.USERNAME.actualValue, Enums.PASSWORD.actualValue);

	}

	@Test
	@Order(1)
	public void saveUserTest() {
		RestAssured.given().log().all().when().body(SignUpHelper.requestBody).post(Enums.SAVE.actualValue).then().log()
				.all().statusCode(HttpStatus.OK.value()).body(Enums.USERNAME_VAR.actualValue,
						equalTo(Enums.DUMMY_NAME.actualValue), Enums.ROLES.actualValue, equalTo("ADMIN"));
	}

	@Test
	@Order(2)
	public void saveUserTestWithThreeRoles() {
		RestAssured.given().log().all().when().body(SignUpHelper.requestBodyWithThreeRoles).post(Enums.SAVE.actualValue)
				.then().log().all().statusCode(HttpStatus.OK.value()).body(Enums.USERNAME_VAR.actualValue,
						equalTo("ron"), Enums.ROLES.actualValue,
						equalTo("ADMIN,USER,SERVER_ADMIN"));
		RestAssured.given().log().all().when().delete("ron").then().log().all().statusCode(200);
	}

	@Test
	@Order(3)
	public void saveUserTestWithRoleOtherThanCSV() {
		RestAssured.given().log().all().when().body(SignUpHelper.requestBodyWithRoleOtherThanCSV)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("Invalid role"));
	}

	@Test
	@Order(4)
	public void saveUserTestWithWrongCredentials() {
		RestAssured.reset();
		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost:" + port + "/crud-app/signup/";
		String requestBody = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Tom\",\r\n"
				+ "		\"password\": \"12345678\",\r\n" + "		\"roles\" : \"ADMIN\"\r\n" + "}";

		RestAssured.given().log().all().auth().basic(Enums.USERNAME.actualValue, "admin12X").when()
				.body(SignUpHelper.requestBodyWithWrongCredentials).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(401).body("error", equalTo("Unauthorized"));
	}

	@Test
	@Order(5)
	public void saveUserTestWithBlankUsername() {
		RestAssured.given().log().all().when().body(SignUpHelper.requestBodyWithBlankUsername)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("username can't be empty, only unique username"));
	}

	@Test
	@Order(6)
	public void saveUserTestWithSameUsername() {
		RestAssured.given().log().all().when().body(SignUpHelper.requestBodyWithSameUserName)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("User with this email id Already Exists"));
	}

	@Test
	@Order(7)
	public void saveUserTestWithPasswordLessThan8Char() {
		RestAssured.given().log().all().when().body(SignUpHelper.requestBodyWithPasswordLessThan8Char)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("Should contain atleast 8 digits"));
	}

	@Test
	@Order(8)
	public void saveUserTestWithPasswordLength7DigitWithSpaceAtEnd() {
		RestAssured.given().log().all().when().body(SignUpHelper.requestBodyWithPassowrdLength7WithSpaceAtEnd)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(200).body(Enums.USERNAME_VAR.actualValue,
						equalTo("ron"), Enums.ROLES.actualValue, equalTo("ADMIN"));
		RestAssured.given().log().all().when().delete("ron").then().log().all().statusCode(200);
	}

	@Test
	@Order(9)
	public void saveUserTestWithPasswordLength7DigitWithSpaceAtStart() {
		RestAssured.given().log().all().when().body(SignUpHelper.requestBodyWithPasswordLength7DigitWithSpaceAtStart)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(200).body(Enums.USERNAME_VAR.actualValue,
						equalTo("ron"), Enums.ROLES.actualValue, equalTo("ADMIN"));
		RestAssured.given().log().all().when().delete("ron").then().log().all().statusCode(200);
	}

	@Test
	@Order(10)
	public void saveUserTestWithBlankRole() {
		RestAssured.given().log().all().when().body(SignUpHelper.requestBodyWithBlankRole).post(Enums.SAVE.actualValue)
				.then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("employee should have atleast 1 role"));
	}

	@Test
	@Order(11)
	public void updateUserTest() {
		RestAssured.given().log().all().when().body(SignUpHelper.requestBodyWithUpdatedUser)
				.put(Enums.UPDATE.actualValue).then().log().all().statusCode(HttpStatus.OK.value())
				.body(Enums.USERNAME_VAR.actualValue, equalTo(Enums.DUMMY_NAME.actualValue), Enums.ROLES.actualValue,
						equalTo("USER"));
	}

	@Test
	@Order(12)
	public void getUserTest() {
		RestAssured.given().log().all().when().get(Enums.LIST.actualValue).then().log().all()
				.statusCode(HttpStatus.OK.value()).body(Enums.USERNAME_VAR.actualValue,
						hasItems(Enums.DUMMY_NAME.actualValue), Enums.ROLES.actualValue, hasItems("USER"));
	}

	@Test
	@Order(13)
	public void getUserWithUsernameTest() {
		RestAssured.given().log().all().when().get(Enums.DUMMY_NAME.actualValue).then().log().all().statusCode(HttpStatus.OK.value()).body(
				Enums.USERNAME_VAR.actualValue, equalTo(Enums.DUMMY_NAME.actualValue), Enums.ROLES.actualValue,
				equalTo("USER"));
	}

	@Test
	@Order(14)
	public void deleteUserTest() {
		RestAssured.given().log().all().when().delete(Enums.DUMMY_NAME.actualValue).then().log().all().statusCode(HttpStatus.OK.value());
	}
}
