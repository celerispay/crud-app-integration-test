package crudapplication.crud.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class User {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotEmpty(message = "username can't be empty, only unique username")
	@ApiModelProperty(position = 1, notes = "The username should be unique", required = true)
	private String username;
	
	@Pattern(regexp="^.{8,}$",
			 message="Should contain atleast 8 digits")
	@ApiModelProperty(position = 2, notes = "The password length should be minimum of 8 characters", required = true)
	private String password;
	
	@NotEmpty(message = "employee should have atleast 1 role")
	@ApiModelProperty(position = 3, notes = "The roles should be csv of valid roles", required = true)
	private String roles;
}
