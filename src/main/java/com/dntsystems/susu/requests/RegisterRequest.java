package com.dntsystems.susu.requests;


import com.dntsystems.susu.entity.UserRoleEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class RegisterRequest {
	@NotEmpty(message = "Username is required")
	private String username;
	
	@NotEmpty(message = "Password is required")
    private String password;
    
	@NotNull(message = "User role name is required")
    private UserRoleEnum role;
    
    @JsonProperty("firstname")
    @NotEmpty(message = "First name is required")
    private String firstname;
    
    @JsonProperty("middlename")
    @NotEmpty(message = "Middle name is required")
    private String middlename;
    
    @JsonProperty("lastname")
    @NotEmpty(message = "Last name is required")
    private String lastname;
    
    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email is required")
    private String email;
    
    @NotEmpty(message = "Phone is required")
    private String phone;
    
    @NotEmpty(message = "Address is required")
    private String address;
    
	public RegisterRequest() {
	}
	
	public RegisterRequest(String username, String password, UserRoleEnum role, String firstname, String middlename,
			String lastname, String email, String phone, String address) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
		this.firstname = firstname;
		this.middlename = middlename;
		this.lastname = lastname;
		this.email = email;
		this.phone = phone;
		this.address = address;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public UserRoleEnum getRole() {
		return role;
	}
	public void setRole(UserRoleEnum role) {
		this.role = role;
	}


	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getMiddlename() {
		return middlename;
	}


	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}


	public String getLastname() {
		return lastname;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
