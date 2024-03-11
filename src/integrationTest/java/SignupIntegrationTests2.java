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
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import static org.hamcrest.Matchers.*;

import javax.activation.DataSource;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = CrudApplication.class)
@ActiveProfiles("integrationTest")
@TestMethodOrder(OrderAnnotation.class)
public class SignupIntegrationTests2 {


	 @LocalServerPort
	 private int port;
	 
	 @BeforeEach
	    public void setUp() {
	       RestAssured.port = port;
	    }

	    @Test()
	    @Order(1)
	    public void saveUserTest() {
	    	String requestBody = "{\r\n"
	    			+ "		\"id\": 1,\r\n"
	    			+ "		\"username\": \"Tom\",\r\n"
	    			+ "		\"password\": \"12345678\",\r\n"
	    			+ "		\"roles\" : \"ADMIN\"\r\n"
	    			+ "}";
	        
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .body(requestBody)
		        .post("/crud-app/signup/save")
	               .then().log().all() .statusCode(HttpStatus.OK.value())
	               .body("username", equalTo("tom"),
	                     "roles", equalTo("ADMIN"));
	    }
	    
	    @Test()
	    @Order(2)
	    public void updateUserTest() {
	    	String requestBody = "{\r\n"
	    			+ "		\"id\": 1,\r\n"
	    			+ "		\"username\": \"Tom\",\r\n"
	    			+ "		\"password\": \"12345678\",\r\n"
	    			+ "		\"roles\" : \"USER\"\r\n"
	    			+ "}";
	        
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .body(requestBody)
		        .put("/crud-app/signup/update")
	               .then().log().all() .statusCode(HttpStatus.OK.value())
	               .body("username", equalTo("tom"),
	                     "roles", equalTo("USER"));
	    }
	    
	    @Test()
	    @Order(3)
	    public void getUserTest() {
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .get("/crud-app/signup/list")
	               .then().log().all() .statusCode(HttpStatus.OK.value())
	               .body("username", hasItems("tom"),
	                     "roles", hasItems("USER"));
	    }
	    
	    @Test()
	    @Order(4)
	    public void getUserWithUsernameTest() {
	    	String username = "Tom";
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .get("/crud-app/signup/" + username)
	               .then().log().all() .statusCode(HttpStatus.OK.value())
	               .body(
	                     "username", equalTo("tom"),
	                     "roles", equalTo("USER"));
	    }
	    
	    @Test()
	    @Order(5)
	    public void deleteUserTest() {
	    	String username = "Tom";
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .delete("/crud-app/signup/" + username)
	               .then()
	               .log()
	               .all()
	               .statusCode(HttpStatus.OK.value());
	    }
}
