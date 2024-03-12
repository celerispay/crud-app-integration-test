package services;

public class SignUpHelper {
	
	public static String requestBody = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Tom\",\r\n"
			+ "		\"password\": \"12345678\",\r\n" + "		\"roles\" : \"ADMIN\"\r\n" + "}";
	
	public static String requestBodyWithThreeRoles = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Ron\",\r\n"
			+ "		\"password\": \"12345678\",\r\n" + "		\"roles\" : \"ADMIN,USER,SERVER_ADMIN\"\r\n" + "}";
	
	public static String requestBodyWithRoleOtherThanCSV = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Ron\",\r\n"
			+ "		\"password\": \"12345678\",\r\n" + "		\"roles\" : \"EMPLOYEE\"\r\n" + "}";
	
	public static String requestBodyWithWrongCredentials = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Tom\",\r\n"
			+ "		\"password\": \"12345678\",\r\n" + "		\"roles\" : \"ADMIN\"\r\n" + "}";
	
	public static String requestBodyWithBlankUsername = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"\",\r\n"
			+ "		\"password\": \"12345678\",\r\n" + "		\"roles\" : \"ADMIN\"\r\n" + "}";
	
	public static String requestBodyWithSameUserName = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"vaibhav\",\r\n"
			+ "		\"password\": \"12345678\",\r\n" + "		\"roles\" : \"ADMIN\"\r\n" + "}";
	
	public static String requestBodyWithPasswordLessThan8Char = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Tom\",\r\n"
			+ "		\"password\": \"1234567\",\r\n" + "		\"roles\" : \"ADMIN\"\r\n" + "}";
	
	public static String requestBodyWithPassowrdLength7WithSpaceAtEnd = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Ron\",\r\n"
			+ "		\"password\": \"1234567 \",\r\n" + "		\"roles\" : \"ADMIN\"\r\n" + "}";
	
	public static String requestBodyWithPasswordLength7DigitWithSpaceAtStart = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Ron\",\r\n"
			+ "		\"password\": \" 1234567 \",\r\n" + "		\"roles\" : \"ADMIN\"\r\n" + "}";
	
	public static String requestBodyWithBlankRole = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Tom\",\r\n"
			+ "		\"password\": \"12345678\",\r\n" + "		\"roles\" : \"\"\r\n" + "}";
	
	public static String requestBodyWithUpdatedUser = "{\r\n" + "		\"id\": 1,\r\n" + "		\"username\": \"Tom\",\r\n"
			+ "		\"password\": \"12345678\",\r\n" + "		\"roles\" : \"USER\"\r\n" + "}";
	
	
	
	
	
	
}
