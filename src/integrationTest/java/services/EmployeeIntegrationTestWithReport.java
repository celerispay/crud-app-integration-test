package services;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import crudapplication.crud.CrudApplication;
import enums.Enums;
import extentReportUtilities.RestUtils;
import extentReportUtilities.SetUp;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = CrudApplication.class)
@ActiveProfiles("integrationTest")
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(SetUp.class)
public class EmployeeIntegrationTestWithReport {

	public static String baseUrl = "http://localhost:8081/crud-app/employee/";

	@LocalServerPort
	private int port;

	@Test
	@Order(1)
	public void saveEmployeeTest() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson1.json");
		Response response = RestUtils.performPost(baseUrl, jsonMap, Enums.SAVE.actualValue);
		response.then().statusCode(200).body(Enums.NAME_VAR.actualValue, equalTo("Ankit"), Enums.EMAIL.actualValue,
				equalTo(Enums.DUMMY_EMAIL.actualValue));
	}

	@Test
	@Order(2)
	public void saveEmployeeTestWithBlankName() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("name", "");
		Response response = RestUtils.performPost(baseUrl, jsonMap, Enums.SAVE.actualValue);
		response.then().statusCode(400).body(Enums.ERROR.actualValue, equalTo("Please provide a Name"));
	}

	@Test
	@Order(3)
	public void saveEmployeeTestWithBlankEmail() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("email", "");
		Response response = RestUtils.performPost(baseUrl, jsonMap, Enums.SAVE.actualValue);
		response.then().statusCode(400).body(Enums.ERROR.actualValue, equalTo("Please provide a unique Email id"));
	}

//
	@Test
	@Order(4)
	public void saveEmployeeTestWithSameEmail() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("email", "ankit@gmail.com");
		Response response = RestUtils.performPost(baseUrl, jsonMap, Enums.SAVE.actualValue);
		response.then().statusCode(400).body(Enums.ERROR.actualValue,
				equalTo("Employee with this email id Already Exists"));
	}

	@Test
	@Order(5)
	public void saveEmployeeTestWithBlankDesignation() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("designation", "");
		Response response = RestUtils.performPost(baseUrl, jsonMap, Enums.SAVE.actualValue);
		response.then().statusCode(400).body(Enums.ERROR.actualValue, equalTo("Please provide a Designation"));
	}

	@Test
	@Order(6)
	public void saveEmployeeTestWithSalaryLessThan10000() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("salary", "8000");
		Response response = RestUtils.performPost(baseUrl, jsonMap, Enums.SAVE.actualValue);
		response.then().statusCode(400).body(Enums.ERROR.actualValue, equalTo("Salary can't be less than 10000"));
	}

//	@Test
//	@Order(7)
//	public void saveEmployeeTestWithSalaryInUnknownToken() {
//		Response response = RestUtils.performPost(baseUrl, EmployeeHelper.requestBodyWithSalaryInUnknownToken, Enums.SAVE.actualValue);
//		response.then().statusCode(400).body(Enums.ERROR.actualValue, containsString(
//						String.format("JSON parse error: Unrecognized token '%s'", EmployeeHelper.wrongSalary)));
//	}

	@Test
	@Order(8)
	public void saveEmployeeTestWithSalaryInChar() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		String value = "ABCD";
		jsonMap.put("salary", value);
		Response response = RestUtils.performPost(baseUrl, jsonMap, Enums.SAVE.actualValue);
		response.then().statusCode(400).body(Enums.ERROR.actualValue,
				containsString(
						String.format("JSON parse error: Cannot deserialize value of type `long` from String \"%s\"",
								EmployeeHelper.wrongSalary)));
	}

	@Test
	@Order(9)
	public void saveEmployeeTestWithGenderOtherThanMAndF()
			throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("gender", "FF");
		Response response = RestUtils.performPost(baseUrl, jsonMap, Enums.SAVE.actualValue);
		response.then().statusCode(400).body(Enums.ERROR.actualValue, equalTo("Please provide a gender M/F"));
	}

	@Test
	@Order(10)
	public void saveEmployeeTestWithBlankGender() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("gender", "");
		Response response = RestUtils.performPost(baseUrl, jsonMap, Enums.SAVE.actualValue);
		response.then().statusCode(400).body(Enums.ERROR.actualValue, equalTo("gender can't be empty"));
	}

	@Test
	@Order(11)
	public void saveEmployeeTestWithBlankAddress() throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("address", "");
		Response response = RestUtils.performPost(baseUrl, jsonMap, Enums.SAVE.actualValue);
		response.then().statusCode(400).body(Enums.ERROR.actualValue,
				allOf(containsString("Please provide a address"), containsString("Enter a vaild length Address")));
	}

	@Test
	@Order(12)
	public void saveEmployeeTestWithLessThan5CharAddress()
			throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("address", "ABC");
		Response response = RestUtils.performPost(baseUrl, jsonMap, Enums.SAVE.actualValue);
		response.then().statusCode(400).body(Enums.ERROR.actualValue, equalTo("Enter a vaild length Address"));
	}

	@Test
	@Order(13)
	public void saveEmployeeTestWithMoreThan100CharAddress()
			throws JsonParseException, JsonMappingException, IOException {
		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson2.json");
		jsonMap.put("address",
				"6kMh7vixQk-y5?85/,)_bc8&*#hP6VnWEG(_@?tj:q(Fi{BG&ZE6mR;60A@{F*uLZUH!7=5YLZFRdTvuD$Fi(%9T1wF#SRxb#ark.");
		Response response = RestUtils.performPost(baseUrl, jsonMap, Enums.SAVE.actualValue);
		response.then().statusCode(400).body(Enums.ERROR.actualValue, equalTo("Enter a vaild length Address"));
		RestUtils.performDelete(baseUrl, Enums.DUMMY_EMAIL.actualValue);
	}

//	@Test
//	@Order(14)
//	public void updateEmployeeTest() throws JsonParseException, JsonMappingException, IOException {
//		Map<Object, Object> jsonMap = EmployeeHelper.jsonToMap("employeeDummyTestJson1.json");
//		jsonMap.put("salary", "21000");
//		RestAssured.given().log().all().when().body(jsonMap).put(Enums.UPDATE.actualValue).then().log().all()
//				.statusCode(HttpStatus.OK.value())
//				.body(Enums.NAME_VAR.actualValue, equalTo("Ankit"), Enums.SALARY_VAR.actualValue, equalTo(21000));
//	}
//
//	@Test
//	@Order(15)
//	public void getEmployeeTest() {
//		RestAssured.given().log().all().when().get(Enums.LIST.actualValue).then().log().all()
//				.statusCode(HttpStatus.OK.value()).body(Enums.NAME_VAR.actualValue, hasItems("Ankit"),
//						Enums.EMAIL.actualValue, hasItems(Enums.DUMMY_EMAIL.actualValue));
//	}
//
//	@Test
//	@Order(16)
//	public void getEmployeeWithEmailTest() {
//		RestAssured.given().log().all().when().get(Enums.DUMMY_EMAIL.actualValue).then().log().all()
//				.statusCode(HttpStatus.OK.value()).body(Enums.NAME_VAR.actualValue, equalTo("Ankit"),
//						Enums.EMAIL.actualValue, equalTo(Enums.DUMMY_EMAIL.actualValue));
//	}
//
//	@Test
//	@Order(17)
//	public void deleteEmployeeTest() {
//		RestAssured.given().log().all().when().delete(Enums.DUMMY_EMAIL.actualValue).then().log().all()
//				.statusCode(HttpStatus.OK.value());
//	}

}
