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
public class EmployeeIntegrationTests {

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
	public void saveEmployeeTest() {
		RestAssured.given().log().all().when().body(EmployeeHelper.requestBody).post(Enums.SAVE.actualValue).then()
				.log().all().statusCode(HttpStatus.OK.value()).body(Enums.NAME_VAR.actualValue, equalTo("Ankit"),
						Enums.EMAIL.actualValue, equalTo(Enums.DUMMY_EMAIL.actualValue));
	}

	@Test
	@Order(2)
	public void saveEmployeeTestWithBlankName() {
		RestAssured.given().log().all().when().body(EmployeeHelper.requestBodyWithBlankName)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("Please provide a Name"));
	}

	@Test
	@Order(3)
	public void saveEmployeeTestWithBlankEmail() {
		RestAssured.given().log().all().when().body(EmployeeHelper.requestBodyWithBlankEmail)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("Please provide a unique Email id"));
	}

	@Test
	@Order(4)
	public void saveEmployeeTestWithSameEmail() {
		RestAssured.given().log().all().when().body(EmployeeHelper.requestBodyWithSameEmail)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("Employee with this email id Already Exists"));
	}

	@Test
	@Order(5)
	public void saveEmployeeTestWithBlankDesignation() {
		RestAssured.given().log().all().when().body(EmployeeHelper.requestBodyWithBlankDesignation)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("Please provide a Designation"));
	}

	@Test
	@Order(6)
	public void saveEmployeeTestWithSalaryLessThan10000() {
		RestAssured.given().log().all().when().body(EmployeeHelper.requestBodyWithSalaryLessThan10000)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("Salary can't be less than 10000"));
	}

	@Test
	@Order(7)
	public void saveEmployeeTestWithSalaryInChar() {
		RestAssured.given().log().all().when().body(EmployeeHelper.requestBodyWithSalaryInChar)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, containsString(
						String.format("JSON parse error: Unrecognized token '%s'", EmployeeHelper.wrongSalary)));
	}

	@Test
	@Order(8)
	public void saveEmployeeTestWithGenderOtherThanMAndF() {
		RestAssured.given().log().all().when().body(EmployeeHelper.requestBodyWithWrongGender)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("Please provide a gender M/F"));
	}

	@Test
	@Order(9)
	public void saveEmployeeTestWithBlankGender() {
		RestAssured.given().log().all().when().body(EmployeeHelper.requestBodyWithBlankGender)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("gender can't be empty"));
	}

	@Test
	@Order(10)
	public void saveEmployeeTestWithBlankAddress() {
		RestAssured.given().log().all().when().body(EmployeeHelper.requestBodyWithBlankAddress)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(400).body(Enums.ERROR.actualValue, allOf(
						containsString("Please provide a address"), containsString("Enter a vaild length Address")));
	}

	@Test
	@Order(11)
	public void saveEmployeeTestWithLessThan5CharAddress() {
		RestAssured.given().log().all().when().body(EmployeeHelper.requestBodyWithAddressLessThan5Char)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("Enter a vaild length Address"));
	}

	@Test
	@Order(12)
	public void saveEmployeeTestWithMoreThan100CharAddress() {
		RestAssured.given().log().all().when().body(EmployeeHelper.requestBodyWithAddressMoreThan100Char)
				.post(Enums.SAVE.actualValue).then().log().all().statusCode(400)
				.body(Enums.ERROR.actualValue, equalTo("Enter a vaild length Address"));
	}

	@Test
	@Order(13)
	public void updateEmployeeTest() {
		RestAssured.given().log().all().when().body(EmployeeHelper.requestBodyWithUpdatedSalary)
				.put(Enums.UPDATE.actualValue).then().log().all().statusCode(HttpStatus.OK.value())
				.body(Enums.NAME_VAR.actualValue, equalTo("Ankit"), "salary", equalTo(21000));
	}

	@Test
	@Order(14)
	public void getEmployeeTest() {
		RestAssured.given().log().all().when().get(Enums.LIST.actualValue).then().log().all()
				.statusCode(HttpStatus.OK.value()).body(Enums.NAME_VAR.actualValue, hasItems("Ankit"),
						Enums.EMAIL.actualValue, hasItems(Enums.DUMMY_EMAIL.actualValue));
	}

	@Test
	@Order(15)
	public void getEmployeeWithEmailTest() {
		RestAssured.given().log().all().when().get(Enums.DUMMY_EMAIL.actualValue).then().log().all()
				.statusCode(HttpStatus.OK.value()).body(Enums.NAME_VAR.actualValue, equalTo("Ankit"),
						Enums.EMAIL.actualValue, equalTo(Enums.DUMMY_EMAIL.actualValue));
	}

	@Test
	@Order(16)
	public void deleteEmployeeTest() {
		RestAssured.given().log().all().when().delete(Enums.DUMMY_EMAIL.actualValue).then().log().all()
				.statusCode(HttpStatus.OK.value());
	}
}
