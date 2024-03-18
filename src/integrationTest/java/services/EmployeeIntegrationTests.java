package services;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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
public class EmployeeIntegrationTests extends AbstractContainerBaseTest{
	
	static {
		RestAssured.filters(new ReportPortalRestAssuredLoggingFilter(42, LogLevel.INFO));
	}

	private String employeeSchemaFilePath1 = "C:\\Users\\Admin\\eclipse-workspace\\crud-application\\src\\integrationTest\\resources\\jsonSchemas\\employeeDummyTestJson1Schema.json";
	private String employeeSchemaFilePath2 = "C:\\Users\\Admin\\eclipse-workspace\\crud-application\\src\\integrationTest\\resources\\jsonSchemas\\employeeDummyTestJson2Schema.json";

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
		RequestSpecification rq = new RequestSpecBuilder()
				.setBaseUri("http://localhost:" + port + "/crud-app/employee/")
				.addHeader("Content-Type", "application/json").build();
		RestAssured.requestSpecification = rq;
		RestAssured.authentication = RestAssured.basic(Enums.USERNAME.actualValue, Enums.PASSWORD.actualValue);
	}

	@Test
	@Order(1)
	public void saveEmployeeTest() throws JsonParseException, JsonMappingException, IOException {
		Path path = EmployeeHelper.createPath(employeeSchemaFilePath1);
		String jsonSchema = Files.readString(path);
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson1.json");
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(HttpStatus.OK.value()).body((JsonSchemaValidator.matchesJsonSchema(jsonSchema)))
				.body(Enums.NAME_VAR.actualValue, equalTo("Ankit"), Enums.EMAIL.actualValue,
						equalTo(Enums.DUMMY_EMAIL.actualValue));
	}

	@Test
	@Order(2)
	public void saveEmployeeTestWithBlankName() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("name", "");
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(400).body(Enums.ERROR.actualValue, equalTo("Please provide a Name"));
	}

	@Test
	@Order(3)
	public void saveEmployeeTestWithBlankEmail() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("email", "");
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(400).body(Enums.ERROR.actualValue, equalTo("Please provide a unique Email id"));
	}

	@Test
	@Order(4)
	public void saveEmployeeTestWithSameEmail() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("email", "ankit@gmail.com");
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(400).body(Enums.ERROR.actualValue, equalTo("Employee with this email id Already Exists"));
	}

	@Test
	@Order(5)
	public void saveEmployeeTestWithBlankDesignation() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("designation", "");
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(400).body(Enums.ERROR.actualValue, equalTo("Please provide a Designation"));
	}

	@Test
	@Order(6)
	public void saveEmployeeTestWithSalaryLessThan10000() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("salary", "8000");
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(400).body(Enums.ERROR.actualValue, equalTo("Salary can't be less than 10000"));
	}

	@Test
	@Order(7)
	public void saveEmployeeTestWithSalaryInUnknownToken() {
		RestAssured.given().log().all().when().body(EmployeeHelper.requestBodyWithSalaryInUnknownToken)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, containsString(
						String.format("JSON parse error: Unrecognized token '%s'", EmployeeHelper.wrongSalary)));
	}

	@Test
	@Order(8)
	public void saveEmployeeTestWithSalaryInChar() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		String value = "ABCD";
		jsonMap.put("salary", value);
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(400).body(Enums.ERROR.actualValue,
						containsString(String.format(
								"JSON parse error: Cannot deserialize value of type `long` from String \"%s\"",
								EmployeeHelper.wrongSalary)));
	}

	@Test
	@Order(9)
	public void saveEmployeeTestWithGenderOtherThanMAndF()
			throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("gender", "FF");
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(400).body(Enums.ERROR.actualValue, equalTo("Please provide a gender M/F"));
	}

	@Test
	@Order(10)
	public void saveEmployeeTestWithBlankGender() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("gender", "");
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(400).body(Enums.ERROR.actualValue, equalTo("gender can't be empty"));
	}

	@Test
	@Order(11)
	public void saveEmployeeTestWithBlankAddress() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("address", "");
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(400).body(Enums.ERROR.actualValue, allOf(containsString("Please provide a address"),
						containsString("Enter a vaild length Address")));
	}

	@Test
	@Order(12)
	public void saveEmployeeTestWithLessThan5CharAddress()
			throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("address", "ABC");
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(400).body(Enums.ERROR.actualValue, equalTo("Enter a vaild length Address"));
	}

	@Test
	@Order(13)
	public void saveEmployeeTestWithMoreThan100CharAddress()
			throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("address",
				"6kMh7vixQk-y5?85/,)_bc8&*#hP6VnWEG(_@?tj:q(Fi{BG&ZE6mR;60A@{F*uLZUH!7=5YLZFRdTvuD$Fi(%9T1wF#SRxb#ark.");
		RestAssured.given().log().all().when().body(jsonMap).post(Enums.SAVE.actualValue).then().log().all()
				.statusCode(400).body(Enums.ERROR.actualValue, equalTo("Enter a vaild length Address"));
	}

	@Test
	@Order(14)
	public void updateEmployeeTest() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson1.json");
		jsonMap.put("salary", "21000");
		Path path = EmployeeHelper.createPath(employeeSchemaFilePath1);
		String jsonSchema = Files.readString(path);
		RestAssured.given().log().all().when().body(jsonMap).put(Enums.UPDATE.actualValue).then().log().all()
				.statusCode(HttpStatus.OK.value()).body((JsonSchemaValidator.matchesJsonSchema(jsonSchema)))
				.body(Enums.NAME_VAR.actualValue, equalTo("Ankit"), Enums.SALARY_VAR.actualValue, equalTo(21000));
	}

	@Test
	@Order(15)
	public void getEmployeeTest() throws IOException {
		Path path = EmployeeHelper.createPath(employeeSchemaFilePath2);
		String jsonSchema = Files.readString(path);
		RestAssured.given().log().all().when().get(Enums.LIST.actualValue).then().log().all()
				.statusCode(HttpStatus.OK.value()).body((JsonSchemaValidator.matchesJsonSchema(jsonSchema)))
				.body(Enums.NAME_VAR.actualValue, hasItems("Ankit"), Enums.EMAIL.actualValue,
						hasItems(Enums.DUMMY_EMAIL.actualValue));
	}

	@Test
	@Order(16)
	public void getEmployeeWithEmailTest() throws IOException {
		Path path = EmployeeHelper.createPath(employeeSchemaFilePath1);
		String jsonSchema = Files.readString(path);
		RestAssured.given().log().all().when().get(Enums.DUMMY_EMAIL.actualValue).then().log().all()
				.statusCode(HttpStatus.OK.value()).body((JsonSchemaValidator.matchesJsonSchema(jsonSchema)))
				.body(Enums.NAME_VAR.actualValue, equalTo("Ankit"), Enums.EMAIL.actualValue,
						equalTo(Enums.DUMMY_EMAIL.actualValue));
	}

	@Test
	@Order(17)
	public void deleteEmployeeTest() {
		RestAssured.given().log().all().when().delete(Enums.DUMMY_EMAIL.actualValue).then().log().all()
				.statusCode(HttpStatus.OK.value());
	}
}
