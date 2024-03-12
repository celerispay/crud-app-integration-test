package enums;

import lombok.Getter;

@Getter
public enum Enums {

	USERNAME("Vaibhav"), 
	PASSWORD("admin123"), 
	SAVE("save"), 
	LIST("list"), 
	UPDATE("update"),
	DUMMY_NAME("tom"),
	ERROR("errors"),
	ROLES("roles"),
	USERNAME_VAR("username"),
	NAME_VAR("name"),
	EMAIL("email"),
	DUMMY_EMAIL("ankit@gmail.com");

	public final String actualValue;

	private Enums(String value) {
		actualValue = value;
	}

}
