package services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SignUpHelper {

	public static Map<Object, Object> jsonToMap(String path)
			throws JsonParseException, JsonMappingException, IOException {
		File jsonFile = new File(
				"C:\\Users\\Admin\\eclipse-workspace\\crud-application\\src\\integrationTest\\resources\\jsonFiles\\"
						+ path);
		ObjectMapper objectMapper = new ObjectMapper();
		Map<Object, Object> jsonMap = objectMapper.readValue(jsonFile, Map.class);
		return jsonMap;

	}
	
	public static Path createPath(String path) {
		Path filepath
        = Path.of(path);
		return filepath;
	}

}
