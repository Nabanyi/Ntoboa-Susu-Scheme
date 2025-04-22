package com.dntsystems.susu.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UserUpdateRequest {
	@NotEmpty(message = "Username is required")
	private String username;
    
    @JsonProperty("firstname")
    @NotEmpty(message = "First name is required")
    private String firstname;
    
    @JsonProperty("middlename")
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

	public UserUpdateRequest() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstName(String firstname) {
		this.firstname = firstname;
	}

	public String getMiddlename() {
		return middlename;
	}

	public void setMiddleName(String middlename) {
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
