package controller;

import static integrationTestUtilities.IntegrationTestHelper.DUMMY_NAME;
import static integrationTestUtilities.IntegrationTestHelper.EMPTY_USERNAME_ERROR;
import static integrationTestUtilities.IntegrationTestHelper.ERROR;
import static integrationTestUtilities.IntegrationTestHelper.INVALID_ROLE_ERROR;
import static integrationTestUtilities.IntegrationTestHelper.LIST;
import static integrationTestUtilities.IntegrationTestHelper.MINIMUM_PASSWORD_LENGTH_ERROR;
import static integrationTestUtilities.IntegrationTestHelper.MINIMUM_ROLE_ERROR;
import static integrationTestUtilities.IntegrationTestHelper.PASSWORD;
import static integrationTestUtilities.IntegrationTestHelper.ROLES;
import static integrationTestUtilities.IntegrationTestHelper.SAVE;
import static integrationTestUtilities.IntegrationTestHelper.UNAUTHORIZED_ERROR;
import static integrationTestUtilities.IntegrationTestHelper.UPDATE;
import static integrationTestUtilities.IntegrationTestHelper.USERNAME;
import static integrationTestUtilities.IntegrationTestHelper.USERNAME_VAR;
import static integrationTestUtilities.IntegrationTestHelper.USER_SAME_EMAIL_ERROR;
import static integrationTestUtilities.IntegrationTestHelper.getBasic;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
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
import integrationTestUtilities.IntegrationTestHelper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import testContainerConfiguration.AbstractContainerBaseTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CrudApplication.class)
@ActiveProfiles("integrationTest")
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(ReportPortalExtension.class)
@DirtiesContext
public class SignUpControllerIT extends AbstractContainerBaseTest {

	@LocalServerPort
	private int port;

	private String signUpSchemaFilePath1 = "signUpDummyTestJson1Schema.json";
	private String signUpSchemaFilePath2 = "signUpDummyTestJson2Schema.json";
	private static JSONObject json = IntegrationTestHelper.jsonToMap("signUpDummyTestJson.json");
	private static JSONObject signUpJson1;
	private static JSONObject signUpJson2;

	private static RequestSpecification requestSpecification;

	@BeforeAll
	static void init() {
		RestAssured.filters(new ReportPortalRestAssuredLoggingFilter(42, LogLevel.INFO));
		requestSpecification = RestAssured.given().spec(
				new RequestSpecBuilder().addHeader("Content-Type", "application/json").setAuth(getBasic()).build())
				.log().all().when();
	}

	@BeforeEach
	void setUp() {
		try {
			signUpJson1 = new JSONObject(json.get("signUpJson1").toString());
			signUpJson2 = new JSONObject(json.get("signUpJson2").toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private String getUrl(String action) {
		return "http://localhost:" + port + "/crud-app/signup/" + action;
	}

	@Test
	@Order(1)
	public void saveUserTest() throws JsonParseException, JsonMappingException, IOException {
		requestSpecification.body(signUpJson1.toString()).post(getUrl(SAVE)).then().log().all()
				.statusCode(HttpStatus.OK.value())
				.body((JsonSchemaValidator.matchesJsonSchema(IntegrationTestHelper.readFile(signUpSchemaFilePath1))))
				.body(USERNAME_VAR, equalTo(DUMMY_NAME), ROLES, equalTo("ADMIN"));
	}

	@Test
	@Order(2)
	public void saveUserTestWithThreeRoles()
			throws JsonParseException, JsonMappingException, IOException, JSONException {

		requestSpecification.body(signUpJson2.toString()).post(getUrl(SAVE)).then().log().all()
				.statusCode(HttpStatus.OK.value())
				.body((JsonSchemaValidator.matchesJsonSchema(IntegrationTestHelper.readFile(signUpSchemaFilePath1))))
				.body(USERNAME_VAR, equalTo("ron"), ROLES, equalTo("ADMIN,USER,SERVER_ADMIN"));
		RestAssured.given().auth().basic(USERNAME, PASSWORD).log().all().when().delete(getUrl("ron")).then().log().all()
				.statusCode(200);
	}

	@Test
	@Order(3)
	public void saveUserTestWithRoleOtherThanCSV()
			throws JsonParseException, JsonMappingException, IOException, JSONException {

		signUpJson2.put("roles", "EMPLOYEE");
		requestSpecification.body(signUpJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(400)
				.body(ERROR, equalTo(INVALID_ROLE_ERROR));
	}

	@Test
	@Order(4)
	public void saveUserTestWithWrongCredentials() throws JsonParseException, JsonMappingException, IOException {
		RestAssured.reset();
		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost:" + port + "/crud-app/signup/";
		RestAssured.given().log().all().auth().basic(USERNAME, "admin12X").when().body(signUpJson1.toString())
				.post(SAVE).then().log().all().statusCode(401).body("error", equalTo(UNAUTHORIZED_ERROR));
	}

	@Test
	@Order(5)
	public void saveUserTestWithBlankUsername()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		signUpJson2.put("username", "");
		requestSpecification.body(signUpJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(400)
				.body(ERROR, equalTo(EMPTY_USERNAME_ERROR));
	}

	@Test
	@Order(6)
	public void saveUserTestWithSameUsername()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		signUpJson2.put("username", "Vaibhav");
		requestSpecification.body(signUpJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(400)
				.body(ERROR, equalTo(USER_SAME_EMAIL_ERROR));
	}

	@Test
	@Order(7)
	public void saveUserTestWithPasswordLessThan8Char()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		signUpJson2.put("password", "1234567");
		requestSpecification.body(signUpJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(400)
				.body(ERROR, equalTo(MINIMUM_PASSWORD_LENGTH_ERROR));
	}

	@Test
	@Order(8)
	public void saveUserTestWithPasswordLength7DigitWithSpaceAtEnd()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		signUpJson2.put("password", "1234567 ");
		requestSpecification.body(signUpJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(200)
				.body((JsonSchemaValidator.matchesJsonSchema(IntegrationTestHelper.readFile(signUpSchemaFilePath1))))
				.body(USERNAME_VAR, equalTo("ron"), ROLES, equalTo("ADMIN,USER,SERVER_ADMIN"));
		RestAssured.given().auth().basic(USERNAME, PASSWORD).log().all().when().delete(getUrl("ron")).then().log().all()
				.statusCode(200);
	}

	@Test
	@Order(9)
	public void saveUserTestWithPasswordLength7DigitWithSpaceAtStart()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		signUpJson2.put("password", " 1234567");
		requestSpecification.body(signUpJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(200)
				.body((JsonSchemaValidator.matchesJsonSchema(IntegrationTestHelper.readFile(signUpSchemaFilePath1))))
				.body(USERNAME_VAR, equalTo("ron"), ROLES, equalTo("ADMIN,USER,SERVER_ADMIN"));
		RestAssured.given().auth().basic(USERNAME, PASSWORD).log().all().when().delete(getUrl("ron")).then().log().all()
				.statusCode(200);
	}

	@Test
	@Order(10)
	public void saveUserTestWithBlankRole()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		signUpJson2.put("roles", "");
		requestSpecification.body(signUpJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(400)
				.body(ERROR, equalTo(MINIMUM_ROLE_ERROR));
	}

	@Test
	@Order(11)
	public void updateUserTest() throws JsonParseException, JsonMappingException, IOException, JSONException {
		signUpJson1.put("roles", "USER");
		requestSpecification.body(signUpJson1.toString()).put(getUrl(UPDATE)).then().log().all()
				.statusCode(HttpStatus.OK.value())
				.body((JsonSchemaValidator.matchesJsonSchema(IntegrationTestHelper.readFile(signUpSchemaFilePath1))))
				.body(USERNAME_VAR, equalTo(DUMMY_NAME), ROLES, equalTo("USER"));
	}

	@Test
	@Order(12)
	public void getUserTest() throws IOException {
		requestSpecification.body(signUpJson2.toString()).get(getUrl(LIST)).then().log().all()
				.statusCode(HttpStatus.OK.value())
				.body((JsonSchemaValidator.matchesJsonSchema(IntegrationTestHelper.readFile(signUpSchemaFilePath2))))
				.body(USERNAME_VAR, hasItems(DUMMY_NAME), ROLES, hasItems("USER"));
	}

	@Test
	@Order(13)
	public void getUserWithUsernameTest() throws IOException {
		requestSpecification.body(signUpJson1.toString()).get(getUrl(DUMMY_NAME)).then().log().all()
				.statusCode(HttpStatus.OK.value())
				.body((JsonSchemaValidator.matchesJsonSchema(IntegrationTestHelper.readFile(signUpSchemaFilePath1))))
				.body(USERNAME_VAR, equalTo(DUMMY_NAME), ROLES, equalTo("USER"));
	}

	@Test
	@Order(14)
	public void deleteUserTest() {
		RestAssured.given().auth().basic(USERNAME, PASSWORD).log().all().when().delete(getUrl(DUMMY_NAME)).then().log()
				.all().statusCode(HttpStatus.OK.value());
	}
}
