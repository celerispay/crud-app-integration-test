package services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.epam.reportportal.junit5.ReportPortalExtension;
import com.epam.reportportal.listeners.LogLevel;
import com.epam.reportportal.restassured.ReportPortalRestAssuredLoggingFilter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import crudapplication.crud.CrudApplication;
import enums.Enums;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = CrudApplication.class)
@ActiveProfiles("integrationTest")
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(ReportPortalExtension.class)
@DirtiesContext
public class SignupIntegrationTests extends AbstractContainerBaseTest{
	
	
	static {
		RestAssured.filters(new ReportPortalRestAssuredLoggingFilter(42, LogLevel.INFO));
	}
	private String signUpSchemaFilePath1 = "C:\\Users\\Admin\\eclipse-workspace\\crud-application\\src\\integrationTest\\resources\\jsonSchemas\\signUpDummyTestJson1Schema.json";
	private String signUpSchemaFilePath2 = "C:\\Users\\Admin\\eclipse-workspace\\crud-application\\src\\integrationTest\\resources\\jsonSchemas\\signUpDummyTestJson2Schema.json";

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
	public void saveUserTest() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = SignUpHelper.jsonToMap("signUpDummyTestJson1.json");
		Path path = SignUpHelper.createPath(signUpSchemaFilePath1);
		String jsonSchema = Files.readString(path);
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(HttpStatus.OK.value()).body((JsonSchemaValidator.matchesJsonSchema(jsonSchema)))
				.body(Enums.USERNAME_VAR.actualValue, equalTo(Enums.DUMMY_NAME.actualValue), Enums.ROLES.actualValue,
						equalTo("ADMIN"));
	}

	@Test
	@Order(2)
	public void saveUserTestWithThreeRoles() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = SignUpHelper.jsonToMap("signUpDummyTestJson2.json");
		Path path = SignUpHelper.createPath(signUpSchemaFilePath1);
		String jsonSchema = Files.readString(path);
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(HttpStatus.OK.value()).body((JsonSchemaValidator.matchesJsonSchema(jsonSchema)))
				.body(Enums.USERNAME_VAR.actualValue, equalTo("ron"), Enums.ROLES.actualValue,
						equalTo("ADMIN,USER,SERVER_ADMIN"));
		RestAssured.given().log().all().when().delete("ron").then().log().all().statusCode(200);
	}

	@Test
	@Order(3)
	public void saveUserTestWithRoleOtherThanCSV() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = SignUpHelper.jsonToMap("signUpDummyTestJson2.json");
		jsonMap.put("roles", "EMPLOYEE");
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(400).body(Enums.ERROR.actualValue, equalTo("Invalid role"));
	}

	@Test
	@Order(4)
	public void saveUserTestWithWrongCredentials() throws JsonParseException, JsonMappingException, IOException {
		RestAssured.reset();
		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost:" + port + "/crud-app/signup/";

		Map<Object, Object> jsonMap = SignUpHelper.jsonToMap("signUpDummyTestJson1.json");
		RestAssured.given().log().all().auth().basic(Enums.USERNAME.actualValue, "admin12X").when().body(jsonMap)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(401).body("error", equalTo("Unauthorized"));
	}

	@Test
	@Order(5)
	public void saveUserTestWithBlankUsername() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = SignUpHelper.jsonToMap("signUpDummyTestJson2.json");
		jsonMap.put("username", "");
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("username can't be empty, only unique username"));
	}

	@Test
	@Order(6)
	public void saveUserTestWithSameUsername() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = SignUpHelper.jsonToMap("signUpDummyTestJson2.json");
		jsonMap.put("username", "Vaibhav");
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(400).body(Enums.ERROR.actualValue, equalTo("User with this email id Already Exists"));
	}

	@Test
	@Order(7)
	public void saveUserTestWithPasswordLessThan8Char() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = SignUpHelper.jsonToMap("signUpDummyTestJson2.json");
		jsonMap.put("password", "1234567");
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(400).body(Enums.ERROR.actualValue, equalTo("Should contain atleast 8 digits"));
	}

	@Test
	@Order(8)
	public void saveUserTestWithPasswordLength7DigitWithSpaceAtEnd()
			throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = SignUpHelper.jsonToMap("signUpDummyTestJson2.json");
		jsonMap.put("password", "1234567 ");
		Path path = SignUpHelper.createPath(signUpSchemaFilePath1);
		String jsonSchema = Files.readString(path);
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(200).body((JsonSchemaValidator.matchesJsonSchema(jsonSchema)))
				.body(Enums.USERNAME_VAR.actualValue, equalTo("ron"), Enums.ROLES.actualValue,
						equalTo("ADMIN,USER,SERVER_ADMIN"));
		RestAssured.given().log().all().when().delete("ron").then().log().all().statusCode(200);
	}

	@Test
	@Order(9)
	public void saveUserTestWithPasswordLength7DigitWithSpaceAtStart()
			throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = SignUpHelper.jsonToMap("signUpDummyTestJson2.json");
		jsonMap.put("password", " 1234567");
		Path path = SignUpHelper.createPath(signUpSchemaFilePath1);
		String jsonSchema = Files.readString(path);
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(200).body((JsonSchemaValidator.matchesJsonSchema(jsonSchema)))
				.body(Enums.USERNAME_VAR.actualValue, equalTo("ron"), Enums.ROLES.actualValue,
						equalTo("ADMIN,USER,SERVER_ADMIN"));
		RestAssured.given().log().all().when().delete("ron").then().log().all().statusCode(200);
	}

	@Test
	@Order(10)
	public void saveUserTestWithBlankRole() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = SignUpHelper.jsonToMap("signUpDummyTestJson2.json");
		jsonMap.put("roles", "");
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(400).body(Enums.ERROR.actualValue, equalTo("employee should have atleast 1 role"));
	}

	@Test
	@Order(11)
	public void updateUserTest() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = SignUpHelper.jsonToMap("signUpDummyTestJson1.json");
		jsonMap.put("roles", "USER");
		Path path = SignUpHelper.createPath(signUpSchemaFilePath1);
		String jsonSchema = Files.readString(path);
		RestAssured.given().log().all().when().body(jsonMap).put(Enums.UPDATE.actualValue).then().log().all()
				.statusCode(HttpStatus.OK.value()).body((JsonSchemaValidator.matchesJsonSchema(jsonSchema)))
				.body(Enums.USERNAME_VAR.actualValue, equalTo(Enums.DUMMY_NAME.actualValue), Enums.ROLES.actualValue,
						equalTo("USER"));
	}

	@Test
	@Order(12)
	public void getUserTest() throws IOException {
		Path path = SignUpHelper.createPath(signUpSchemaFilePath2);
		String jsonSchema = Files.readString(path);
		RestAssured.given().log().all().when().get(Enums.LIST.actualValue).then().log().all()
				.statusCode(HttpStatus.OK.value()).body((JsonSchemaValidator.matchesJsonSchema(jsonSchema)))
				.body(Enums.USERNAME_VAR.actualValue, hasItems(Enums.DUMMY_NAME.actualValue), Enums.ROLES.actualValue,
						hasItems("USER"));
	}

	@Test
	@Order(13)
	public void getUserWithUsernameTest() throws IOException {
		Path path = SignUpHelper.createPath(signUpSchemaFilePath1);
		String jsonSchema = Files.readString(path);
		RestAssured.given().log().all().when().get(Enums.DUMMY_NAME.actualValue).then().log().all()
				.statusCode(HttpStatus.OK.value()).body((JsonSchemaValidator.matchesJsonSchema(jsonSchema)))
				.body(Enums.USERNAME_VAR.actualValue, equalTo(Enums.DUMMY_NAME.actualValue), Enums.ROLES.actualValue,
						equalTo("USER"));
	}

	@Test
	@Order(14)
	public void deleteUserTest() {
		RestAssured.given().log().all().when().delete(Enums.DUMMY_NAME.actualValue).then().log().all()
				.statusCode(HttpStatus.OK.value());
	}
}
