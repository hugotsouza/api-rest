package com.api.model;

import javax.persistence.Entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Student extends AbstractEntity{

	@NotEmpty(message="The student name field is required")
	private String name;
	@NotEmpty
	@Email(message="Enter a valid email address")
	private String email;
	
	public Student(String name, String email) {
		this.name = name;
		this.email = email;
	}
	
	public Student(Long id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}
	
	public Student(){}

	@Override
	public String toString() {
		return "id: " + id + " name: " + name + " email: " + email; 
	}


	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
