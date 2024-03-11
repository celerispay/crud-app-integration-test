package crudapplication.crud.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Table(name = "employee")
@Data
@ApiModel
public class Employee {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank(message = "Please provide a Name")
	@ApiModelProperty(position = 1, notes = "The name should be a provided", required = true)
	private String name;

	@NotBlank(message = "Please provide a unique Email id")
	@ApiModelProperty(position = 2, notes = "The email should be valid", required = true)
	@Email
	private String email;

	@NotBlank(message = "Please provide a Designation")
	@ApiModelProperty(position = 3, notes = "The designation should be provided", required = true)
	private String designation;

	@Min(value = 10000, message = "Salary can't be less than 10000")
	@ApiModelProperty(position = 4, notes = "The salary can't be less than 10000", required = true)
	private long salary;

	@NotBlank(message = "gender can't be empty")
	@Pattern(regexp = "^(?:m|M|f|F|)$", message = "Please provide a gender M/F")
	@ApiModelProperty(position = 5, notes = "The gender should be M/F ", required = true)
	private String gender;

	@NotBlank(message = "Please provide a address")
	@Size(min = 5, max = 100, message = "Enter a vaild length Address")
	@ApiModelProperty(position = 6, notes = "The address should be of valid length", required = true)
	private String address;
}
