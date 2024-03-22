package controller;

import static integrationTestUtilities.IntegrationTestHelper.DUMMY_EMAIL;
import static integrationTestUtilities.IntegrationTestHelper.EMAIL;
import static integrationTestUtilities.IntegrationTestHelper.ERROR;
import static integrationTestUtilities.IntegrationTestHelper.LIST;
import static integrationTestUtilities.IntegrationTestHelper.NAME_VAR;
import static integrationTestUtilities.IntegrationTestHelper.REQUEST_BODY_WITH_SALARY_IN_UNKNOWN_TOKEN;
import static integrationTestUtilities.IntegrationTestHelper.SALARY_VAR;
import static integrationTestUtilities.IntegrationTestHelper.SAVE;
import static integrationTestUtilities.IntegrationTestHelper.UPDATE;
import static integrationTestUtilities.IntegrationTestHelper.WRONG_SALARY;
import static integrationTestUtilities.IntegrationTestHelper.getBasic;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
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
public class EmployeeControllerIT extends AbstractContainerBaseTest {

	@LocalServerPort
	private int port;

	private static String employeeSchemaFilePath1 = "employeeDummyTestJson1Schema.json";
	private static String employeeSchemaFilePath2 = "employeeDummyTestJson2Schema.json";
	private static JSONObject json = IntegrationTestHelper.jsonToMap("employeeDummyTestJson.json");
	private static JSONObject empJson1;
	private static JSONObject empJson2;
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
			empJson1 = new JSONObject(json.get("empJson1").toString());
			empJson2 = new JSONObject(json.get("empJson2").toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private String getUrl(String action) {
		return "http://localhost:" + port + "/crud-app/employee/" + action;
	}

	@Test
	@Order(1)
	public void saveEmployeeTest() throws JsonParseException, JsonMappingException, IOException {
		requestSpecification.body(empJson1.toString()).post(getUrl(SAVE)).then().log().all()
				.statusCode(HttpStatus.OK.value())
				.body((JsonSchemaValidator.matchesJsonSchema(IntegrationTestHelper.readFile(employeeSchemaFilePath1))))
				.body(NAME_VAR, equalTo("Ankit"), EMAIL, equalTo(DUMMY_EMAIL));
	}

	@Test
	@Order(2)
	public void saveEmployeeTestWithBlankName()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		empJson2.put("name", "");
		requestSpecification.body(empJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(400).body(ERROR,
				equalTo("Please provide a Name"));
	}

	@Test
	@Order(3)
	public void saveEmployeeTestWithBlankEmail()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		empJson2.put("email", "");
		requestSpecification.body(empJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(400).body(ERROR,
				equalTo("Please provide a unique Email id"));
	}

	@Test
	@Order(4)
	public void saveEmployeeTestWithSameEmail()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		empJson2.put("email", "ankit@gmail.com");
		requestSpecification.body(empJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(400).body(ERROR,
				equalTo("Employee with this email id Already Exists"));
	}

	@Test
	@Order(5)
	public void saveEmployeeTestWithBlankDesignation()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		empJson2.put("designation", "");
		requestSpecification.body(empJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(400).body(ERROR,
				equalTo("Please provide a Designation"));
	}

	@Test
	@Order(6)
	public void saveEmployeeTestWithSalaryLessThan10000()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		empJson2.put("salary", "8000");
		requestSpecification.body(empJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(400).body(ERROR,
				equalTo("Salary can't be less than 10000"));
	}

	@Test
	@Order(7)
	public void saveEmployeeTestWithSalaryInUnknownToken() {
		requestSpecification.body(REQUEST_BODY_WITH_SALARY_IN_UNKNOWN_TOKEN).post(getUrl(SAVE)).then().log().all()
				.statusCode(400)
				.body(ERROR, containsString(String.format("JSON parse error: Unrecognized token '%s'", WRONG_SALARY)));
	}

	@Test
	@Order(8)
	public void saveEmployeeTestWithSalaryInChar()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		String value = "ABCD";
		empJson2.put("salary", value);
		requestSpecification.body(empJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(400)
				.body(ERROR,
						containsString(String.format(
								"JSON parse error: Cannot deserialize value of type `long` from String \"%s\"",
								WRONG_SALARY)));
	}

	@Test
	@Order(9)
	public void saveEmployeeTestWithGenderOtherThanMAndF()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		empJson2.put("gender", "FF");
		requestSpecification.body(empJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(400).body(ERROR,
				equalTo("Please provide a gender M/F"));
	}

	@Test
	@Order(10)
	public void saveEmployeeTestWithBlankGender()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		empJson2.put("gender", "");
		requestSpecification.body(empJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(400).body(ERROR,
				equalTo("gender can't be empty"));
	}

	@Test
	@Order(11)
	public void saveEmployeeTestWithBlankAddress()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		empJson2.put("address", "");
		requestSpecification.body(empJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(400).body(ERROR,
				allOf(containsString("Please provide a address"), containsString("Enter a vaild length Address")));
	}

	@Test
	@Order(12)
	public void saveEmployeeTestWithLessThan5CharAddress()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		empJson2.put("address", "ABC");
		requestSpecification.body(empJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(400).body(ERROR,
				equalTo("Enter a vaild length Address"));
	}

	@Test
	@Order(13)
	public void saveEmployeeTestWithMoreThan100CharAddress()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		empJson2.put("address",
				"6kMh7vixQk-y5?85/,)_bc8&*#hP6VnWEG(_@?tj:q(Fi{BG&ZE6mR;60A@{F*uLZUH!7=5YLZFRdTvuD$Fi(%9T1wF#SRxb#ark.");
		requestSpecification.body(empJson2.toString()).post(getUrl(SAVE)).then().log().all().statusCode(400).body(ERROR,
				equalTo("Enter a vaild length Address"));
	}

	@Test
	@Order(14)
	public void updateEmployeeTest() throws JsonParseException, JsonMappingException, IOException, JSONException {
		empJson1.put("salary", "21000");
		requestSpecification.body(empJson1.toString()).put(getUrl(UPDATE)).then().log().all()
				.statusCode(HttpStatus.OK.value())
				.body((JsonSchemaValidator.matchesJsonSchema(IntegrationTestHelper.readFile(employeeSchemaFilePath1))))
				.body(NAME_VAR, equalTo("Ankit"), SALARY_VAR, equalTo(21000));
	}

	@Test
	@Order(15)
	public void getEmployeeTest() throws IOException {
		requestSpecification.get(getUrl(LIST)).then().log().all().statusCode(HttpStatus.OK.value())
				.body((JsonSchemaValidator.matchesJsonSchema(IntegrationTestHelper.readFile(employeeSchemaFilePath2))))
				.body(NAME_VAR, hasItems("Ankit"), EMAIL, hasItems(DUMMY_EMAIL));
	}

	@Test
	@Order(16)
	public void getEmployeeWithEmailTest() throws IOException {
		requestSpecification.get(getUrl(DUMMY_EMAIL)).then().log().all().statusCode(HttpStatus.OK.value())
				.body((JsonSchemaValidator.matchesJsonSchema(IntegrationTestHelper.readFile(employeeSchemaFilePath1))))
				.body(NAME_VAR, equalTo("Ankit"), EMAIL, equalTo(DUMMY_EMAIL));
	}

	@Test
	@Order(17)
	public void deleteEmployeeTest() {
		requestSpecification.delete(getUrl(DUMMY_EMAIL)).then().log().all().statusCode(HttpStatus.OK.value());
	}
}
