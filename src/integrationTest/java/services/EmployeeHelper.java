package services;

public class EmployeeHelper {

	public static String requestBody = "{\r\n" + "		\"id\": 5,\r\n" + "		\"name\": \"Ankit\",\r\n"
			+ "		\"email\": \"ankit@gmail.com\",\r\n" + "		\"designation\" : \"BA\",\r\n"
			+ "		\"salary\" : 19000,\r\n" + "		\"gender\" : \"M\",\r\n"
			+ "		\"address\": \"Jamnagar\"\r\n" + "	}";

	public static String requestBodyWithBlankName = "{\r\n" + "		\"id\": 5,\r\n" + "		\"name\": \"\",\r\n"
			+ "		\"email\": \"disha@gmail.com\",\r\n" + "		\"designation\" : \"HR\",\r\n"
			+ "		\"salary\" : 20000,\r\n" + "		\"gender\" : \"F\",\r\n"
			+ "		\"address\": \"Mathura\"\r\n" + "	}";

	public static String requestBodyWithBlankEmail = "{\r\n" + "		\"id\": 5,\r\n"
			+ "		\"name\": \"disha\",\r\n" + "		\"email\": \"\",\r\n" + "		\"designation\" : \"HR\",\r\n"
			+ "		\"salary\" : 20000,\r\n" + "		\"gender\" : \"F\",\r\n"
			+ "		\"address\": \"Mathura\"\r\n" + "	}";

	public static String requestBodyWithSameEmail = "{\r\n" + "		\"id\": 5,\r\n" + "		\"name\": \"disha\",\r\n"
			+ "		\"email\": \"ankit@gmail.com\",\r\n" + "		\"designation\" : \"HR\",\r\n"
			+ "		\"salary\" : 20000,\r\n" + "		\"gender\" : \"F\",\r\n"
			+ "		\"address\": \"Mathura\"\r\n" + "	}";

	public static String requestBodyWithBlankDesignation = "{\r\n" + "		\"id\": 5,\r\n"
			+ "		\"name\": \"disha\",\r\n" + "		\"email\": \"disha@gmail.com\",\r\n"
			+ "		\"designation\" : \"\",\r\n" + "		\"salary\" : 20000,\r\n" + "		\"gender\" : \"F\",\r\n"
			+ "		\"address\": \"Mathura\"\r\n" + "	}";

	public static String requestBodyWithSalaryLessThan10000 = "{\r\n" + "		\"id\": 5,\r\n"
			+ "		\"name\": \"disha\",\r\n" + "		\"email\": \"disha@gmail.com\",\r\n"
			+ "		\"designation\" : \"HR\",\r\n" + "		\"salary\" : 8000,\r\n" + "		\"gender\" : \"F\",\r\n"
			+ "		\"address\": \"Mathura\"\r\n" + "	}";

	public static String wrongSalary = "ABCD";

	public static String requestBodyWithSalaryInChar = "{\r\n" + "		\"id\": 5,\r\n"
			+ "		\"name\": \"disha\",\r\n" + "		\"email\": \"disha@gmail.com\",\r\n"
			+ "		\"designation\" : \"HR\",\r\n" + "		\"salary\" : ABCD,\r\n" + "		\"gender\" : \"F\",\r\n"
			+ "		\"address\": \"Mathura\"\r\n" + "	}";

	public static String requestBodyWithWrongGender = "{\r\n" + "		\"id\": 5,\r\n"
			+ "		\"name\": \"disha\",\r\n" + "		\"email\": \"disha@gmail.com\",\r\n"
			+ "		\"designation\" : \"HR\",\r\n" + "		\"salary\" : 20000,\r\n"
			+ "		\"gender\" : \"FF\",\r\n" + "		\"address\": \"Mathura\"\r\n" + "	}";

	public static String requestBodyWithBlankGender = "{\r\n" + "		\"id\": 5,\r\n"
			+ "		\"name\": \"disha\",\r\n" + "		\"email\": \"disha@gmail.com\",\r\n"
			+ "		\"designation\" : \"HR\",\r\n" + "		\"salary\" : 20000,\r\n" + "		\"gender\" : \"\",\r\n"
			+ "		\"address\": \"Mathura\"\r\n" + "	}";

	public static String requestBodyWithBlankAddress = "{\r\n" + "		\"id\": 5,\r\n"
			+ "		\"name\": \"disha\",\r\n" + "		\"email\": \"disha@gmail.com\",\r\n"
			+ "		\"designation\" : \"HR\",\r\n" + "		\"salary\" : 20000,\r\n" + "		\"gender\" : \"F\",\r\n"
			+ "		\"address\": \"\"\r\n" + "	}";

	public static String requestBodyWithAddressLessThan5Char = "{\r\n" + "		\"id\": 5,\r\n"
			+ "		\"name\": \"disha\",\r\n" + "		\"email\": \"disha@gmail.com\",\r\n"
			+ "		\"designation\" : \"HR\",\r\n" + "		\"salary\" : 20000,\r\n" + "		\"gender\" : \"F\",\r\n"
			+ "		\"address\": \"ABC\"\r\n" + "	}";

	public static String requestBodyWithAddressMoreThan100Char = "{\r\n" + "		\"id\": 5,\r\n"
			+ "		\"name\": \"disha\",\r\n" + "		\"email\": \"disha@gmail.com\",\r\n"
			+ "		\"designation\" : \"HR\",\r\n" + "		\"salary\" : 20000,\r\n" + "		\"gender\" : \"F\",\r\n"
			+ "		\"address\": \"6kMh7vixQk-y5?85/,)_bc8&*#hP6VnWEG(_@?tj:q(Fi{BG&ZE6mR;60A@{F*uLZUH!7=5YLZFRdTvuD$Fi(%9T1wF#SRxb#ark.\"\r\n"
			+ "	}";

	public static String requestBodyWithUpdatedSalary = "{\r\n" + "		\"id\": 5,\r\n"
			+ "		\"name\": \"Ankit\",\r\n" + "		\"email\": \"ankit@gmail.com\",\r\n"
			+ "		\"designation\" : \"BA\",\r\n" + "		\"salary\" : 21000,\r\n" + "		\"gender\" : \"M\",\r\n"
			+ "		\"address\": \"Jamnagar\"\r\n" + "	}";

}
