package extentReportUtilities;

import java.util.Map;

import org.springframework.http.HttpStatus;

import enums.Enums;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;

public class RestUtils {
	
	private static RequestSpecification getRequestSpecification(String endpoint, Object requestPayload) {
		
		return RestAssured.given().baseUri(endpoint).auth().basic(Enums.USERNAME.actualValue, Enums.PASSWORD.actualValue).header("Content-Type", "application/json").body(requestPayload);
	}
	
public static Response performPost(String endpoint, Map<Object, Object> requestPayload, String path) {
		RequestSpecification requestSpecification = getRequestSpecification(endpoint, requestPayload);
		Response response = requestSpecification.post(path);
		printRequestLogInReport(requestSpecification);
		printResponseLogInReport(response);
		return response;
	}

public static void performDelete(String endpoint, String email) {
	RestAssured.given().baseUri(endpoint).auth().basic(Enums.USERNAME.actualValue, Enums.PASSWORD.actualValue).when().delete(email).then().log().all()
	.statusCode(HttpStatus.OK.value());
}

	private static void printRequestLogInReport(RequestSpecification requestSpecification) {

		QueryableRequestSpecification queryableRequestSpecification = SpecificationQuerier.query(requestSpecification);
		ExtentReportManager.logInfoDetails("Endpoint is " + queryableRequestSpecification.getBaseUri());
		ExtentReportManager.logInfoDetails("Method is " + queryableRequestSpecification.getMethod());
		ExtentReportManager.logInfoDetails("Headers are " + queryableRequestSpecification.getHeaders().asList().toString());
		ExtentReportManager.logInfoDetails("Request body is ");
		ExtentReportManager.logJson(queryableRequestSpecification.getBody());
	}

	private static void printResponseLogInReport(Response response) {
		ExtentReportManager.logInfoDetails("Response status is " + response.getStatusCode());
		ExtentReportManager.logInfoDetails("Response Headers are " + response.getHeaders().asList().toString());
		ExtentReportManager.logInfoDetails("Response body is ");
		ExtentReportManager.logJson(response.getBody().asPrettyString());
	}
}
