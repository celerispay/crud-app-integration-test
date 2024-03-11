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
public class EmployeeIntegrationTests {


	 @LocalServerPort
	 private int port;
	 
	 @BeforeEach
	    public void setUp() {
	       RestAssured.port = port;
	    }

	    @Test()
	    @Order(1)
	    public void saveEmployeeTest() {
	    	String requestBody = "{\r\n"
	    			+ "		\"id\": 5,\r\n"
	    			+ "		\"name\": \"Ankit\",\r\n"
	    			+ "		\"email\": \"ankit@gmail.com\",\r\n"
	    			+ "		\"designation\" : \"BA\",\r\n"
	    			+ "		\"salary\" : 19000,\r\n"
	    			+ "		\"gender\" : \"M\",\r\n"
	    			+ "		\"address\": \"Jamnagar\"\r\n"
	    			+ "	}";
	        
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .body(requestBody)
		        .post("/crud-app/employee/save")
	               .then().log().all() .statusCode(HttpStatus.OK.value())
	               .body("name", equalTo("Ankit"),
	                     "email", equalTo("ankit@gmail.com"));
	    }
	    
	    @Test()
	    @Order(1)
	    public void saveEmployeeTestWithBlankName() {
	    	String requestBody = "{\r\n"
	    			+ "		\"id\": 5,\r\n"
	    			+ "		\"name\": \"\",\r\n"
	    			+ "		\"email\": \"disha@gmail.com\",\r\n"
	    			+ "		\"designation\" : \"HR\",\r\n"
	    			+ "		\"salary\" : 20000,\r\n"
	    			+ "		\"gender\" : \"F\",\r\n"
	    			+ "		\"address\": \"Mathura\"\r\n"
	    			+ "	}";
	        
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .body(requestBody)
		        .post("/crud-app/employee/save")
	               .then().log().all() .statusCode(400)
	               .body("errors", equalTo("Please provide a Name"));
	    }
	    
	    @Test()
	    @Order(1)
	    public void saveEmployeeTestWithBlankEmail() {
	    	String requestBody = "{\r\n"
	    			+ "		\"id\": 5,\r\n"
	    			+ "		\"name\": \"disha\",\r\n"
	    			+ "		\"email\": \"\",\r\n"
	    			+ "		\"designation\" : \"HR\",\r\n"
	    			+ "		\"salary\" : 20000,\r\n"
	    			+ "		\"gender\" : \"F\",\r\n"
	    			+ "		\"address\": \"Mathura\"\r\n"
	    			+ "	}";
	        
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .body(requestBody)
		        .post("/crud-app/employee/save")
	               .then().log().all() .statusCode(400)
	               .body("errors", equalTo("Please provide a unique Email id"));
	    }
	    
	    @Test()
	    @Order(1)
	    public void saveEmployeeTestWithSameEmail() {
	    	String requestBody = "{\r\n"
	    			+ "		\"id\": 5,\r\n"
	    			+ "		\"name\": \"disha\",\r\n"
	    			+ "		\"email\": \"ankit@gmail.com\",\r\n"
	    			+ "		\"designation\" : \"HR\",\r\n"
	    			+ "		\"salary\" : 20000,\r\n"
	    			+ "		\"gender\" : \"F\",\r\n"
	    			+ "		\"address\": \"Mathura\"\r\n"
	    			+ "	}";
	        
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .body(requestBody)
		        .post("/crud-app/employee/save")
	               .then().log().all() .statusCode(400)
	               .body("errors", equalTo("Employee with this email id Already Exists"));
	    }
	    
	    @Test()
	    @Order(1)
	    public void saveEmployeeTestWithBlankDesignation() {
	    	String requestBody = "{\r\n"
	    			+ "		\"id\": 5,\r\n"
	    			+ "		\"name\": \"disha\",\r\n"
	    			+ "		\"email\": \"disha@gmail.com\",\r\n"
	    			+ "		\"designation\" : \"\",\r\n"
	    			+ "		\"salary\" : 20000,\r\n"
	    			+ "		\"gender\" : \"F\",\r\n"
	    			+ "		\"address\": \"Mathura\"\r\n"
	    			+ "	}";
	        
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .body(requestBody)
		        .post("/crud-app/employee/save")
	               .then().log().all() .statusCode(400)
	               .body("errors", equalTo("Please provide a Designation"));
	    }
	    
	    @Test()
	    @Order(1)
	    public void saveEmployeeTestWithSalaryLessThan10000() {
	    	String requestBody = "{\r\n"
	    			+ "		\"id\": 5,\r\n"
	    			+ "		\"name\": \"disha\",\r\n"
	    			+ "		\"email\": \"disha@gmail.com\",\r\n"
	    			+ "		\"designation\" : \"HR\",\r\n"
	    			+ "		\"salary\" : ABCD,\r\n"
	    			+ "		\"gender\" : \"F\",\r\n"
	    			+ "		\"address\": \"Mathura\"\r\n"
	    			+ "	}";
	        
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .body(requestBody)
		        .post("/crud-app/employee/save")
	               .then().log().all() .statusCode(400)
	               .body("errors", equalTo("Salary can't be less than 10000"));
	    }
	    
	    @Test()
	    @Order(1)
	    public void saveEmployeeTestWithSalaryInChar() {
	    	String wrongSalary = "ABCD";
	    	String requestBody = "{\r\n"
	    			+ "		\"id\": 5,\r\n"
	    			+ "		\"name\": \"disha\",\r\n"
	    			+ "		\"email\": \"disha@gmail.com\",\r\n"
	    			+ "		\"designation\" : \"HR\",\r\n"
	    			+ "		\"salary\" : "+ wrongSalary +",\r\n"
	    			+ "		\"gender\" : \"F\",\r\n"
	    			+ "		\"address\": \"Mathura\"\r\n"
	    			+ "	}";
	        
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .body(requestBody)
		        .post("/crud-app/employee/save")
	               .then().log().all() .statusCode(400)
	               .body("errors", containsString(String.format("JSON parse error: Unrecognized token '%s'", wrongSalary)));
	    }
	    
	    @Test()
	    @Order(1)
	    public void saveEmployeeTestWithGenderOtherThanMAndF() {
	    	String wrongGender = "B";
	    	String requestBody="{\r\n"
	        + "        \"id\": 5,\r\n"
	        + "        \"name\": \"Disha\",\r\n"
	        + "        \"email\": \"disha@gmail.com\",\r\n"
	        + "        \"designation\" : \"HR\",\r\n"
	        + "        \"salary\" : 20000,\r\n"
	        + "        \"gender\" : \"" + wrongGender + "\",\r\n"
	        + "        \"address\": \"Mathura\"\r\n"
	        + "    }";
	    	
	        
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .body(requestBody)
		        .post("/crud-app/employee/save")
	               .then().log().all() .statusCode(400)
	               .body("errors", equalTo("Please provide a gender M/F"));
	    }
	    
	    @Test()
	    @Order(1)
	    public void saveEmployeeTestWithBlankGender() {
	    	String requestBody = "{\r\n"
	    			+ "		\"id\": 5,\r\n"
	    			+ "		\"name\": \"disha\",\r\n"
	    			+ "		\"email\": \"disha@gmail.com\",\r\n"
	    			+ "		\"designation\" : \"HR\",\r\n"
	    			+ "		\"salary\" : 20000,\r\n"
	    			+ "		\"gender\" : \"\",\r\n"
	    			+ "		\"address\": \"Mathura\"\r\n"
	    			+ "	}";
	        
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .body(requestBody)
		        .post("/crud-app/employee/save")
	               .then().log().all() .statusCode(400)
	               .body("errors", equalTo("gender can't be empty"));
	    }
	    
	    @Test()
	    @Order(1)
	    public void saveEmployeeTestWithBlankAddress() {
	    	String requestBody = "{\r\n"
	    			+ "		\"id\": 5,\r\n"
	    			+ "		\"name\": \"disha\",\r\n"
	    			+ "		\"email\": \"disha@gmail.com\",\r\n"
	    			+ "		\"designation\" : \"HR\",\r\n"
	    			+ "		\"salary\" : 20000,\r\n"
	    			+ "		\"gender\" : \"F\",\r\n"
	    			+ "		\"address\": \"\"\r\n"
	    			+ "	}";
	        
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .body(requestBody)
		        .post("/crud-app/employee/save")
	               .then().log().all() .statusCode(400)
	               .body("errors", allOf(containsString("Please provide a address"), containsString("Enter a vaild length Address")));
	    }
	    
	    @Test()
	    @Order(1)
	    public void saveEmployeeTestWithLessThan5CharAddress() {
	    	String requestBody = "{\r\n"
	    			+ "		\"id\": 5,\r\n"
	    			+ "		\"name\": \"disha\",\r\n"
	    			+ "		\"email\": \"disha@gmail.com\",\r\n"
	    			+ "		\"designation\" : \"HR\",\r\n"
	    			+ "		\"salary\" : 20000,\r\n"
	    			+ "		\"gender\" : \"F\",\r\n"
	    			+ "		\"address\": \"ABC\"\r\n"
	    			+ "	}";
	        
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .body(requestBody)
		        .post("/crud-app/employee/save")
	               .then().log().all() .statusCode(400)
	               .body("errors", equalTo("Enter a vaild length Address"));
	    }
	    
	    @Test()
	    @Order(1)
	    public void saveEmployeeTestWithMoreThan100CharAddress() {
	    	String requestBody = "{\r\n"
	    			+ "		\"id\": 5,\r\n"
	    			+ "		\"name\": \"disha\",\r\n"
	    			+ "		\"email\": \"disha@gmail.com\",\r\n"
	    			+ "		\"designation\" : \"HR\",\r\n"
	    			+ "		\"salary\" : 20000,\r\n"
	    			+ "		\"gender\" : \"F\",\r\n"
	    			+ "		\"address\": \"6kMh7vixQk-y5?85/,)_bc8&*#hP6VnWEG(_@?tj:q(Fi{BG&ZE6mR;60A@{F*uLZUH!7=5YLZFRdTvuD$Fi(%9T1wF#SRxb#ark.\"\r\n"
	    			+ "	}";
	        
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .body(requestBody)
		        .post("/crud-app/employee/save")
	               .then().log().all() .statusCode(400)
	               .body("errors", equalTo("Enter a vaild length Address"));
	    }
	    
	    @Test()
	    @Order(2)
	    public void updateEmployeeTest() {
	    	String requestBody = "{\r\n"
	    			+ "		\"id\": 5,\r\n"
	    			+ "		\"name\": \"Ankit\",\r\n"
	    			+ "		\"email\": \"ankit@gmail.com\",\r\n"
	    			+ "		\"designation\" : \"BA\",\r\n"
	    			+ "		\"salary\" : 21000,\r\n"
	    			+ "		\"gender\" : \"M\",\r\n"
	    			+ "		\"address\": \"Jamnagar\"\r\n"
	    			+ "	}";
	        
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .body(requestBody)
		        .put("/crud-app/employee/update")
	               .then().log().all() .statusCode(HttpStatus.OK.value())
	               .body("name", equalTo("Ankit"),
	                     "salary", equalTo(21000));
	    }
	    
	    @Test()
	    @Order(3)
	    public void getEmployeeTest() {
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .get("/crud-app/employee/list")
	               .then().log().all() .statusCode(HttpStatus.OK.value())
	               .body("name", hasItems("Ankit"),
	                     "email", hasItems("ankit@gmail.com"));
	    }
	    
	    @Test()
	    @Order(4)
	    public void getEmployeeWithEmailTest() {
	    	String email = "ankit@gmail.com";
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .get("/crud-app/employee/" + email)
	               .then().log().all() .statusCode(HttpStatus.OK.value())
	               .body(
	                     "name", equalTo("Ankit"),
	                     "email", equalTo("ankit@gmail.com"));
	    }
	    
	    @Test()
	    @Order(5)
	    public void deleteEmployeeTest() {
	    	String email = "ankit@gmail.com";
	    	 RestAssured.given().log().all()
	    	 .auth()
	    	 .basic("vaibhav", "admin123")
	    	 .baseUri("http://localhost:" + port)
		        .header("Content-Type", "application/json")
		        .when()
		        .delete("/crud-app/employee/" + email)
	               .then().log().all() .statusCode(HttpStatus.OK.value());
	    }
}
