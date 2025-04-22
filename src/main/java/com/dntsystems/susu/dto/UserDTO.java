package com.dntsystems.susu.dto;

import com.dntsystems.susu.entity.UserRoleEnum;
import com.dntsystems.susu.entity.UserStatusEnum;

public class UserDTO {
    private Long id;
    private String username;
    private UserRoleEnum role;
    private String firstname;
    private String middlename;
    private String lastname;
    private String email;
    private String phone;
    private String address;
    private String token;
    private String refreshToken;
    private UserStatusEnum status;
    
    
	public UserDTO() {
		super();
	}
	
	public UserDTO(Long id, String username, UserRoleEnum role, String firstname, String middlename, String lastname,
			String email, String phone, String address, String token, String refreshToken, UserStatusEnum status) {
		super();
		this.id = id;
		this.username = username;
		this.role = role;
		this.firstname = firstname;
		this.middlename = middlename;
		this.lastname = lastname;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.token = token;
		this.refreshToken = refreshToken;
		this.status = status;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public UserStatusEnum getStatus() {
		return status;
	}
	public void setStatus(UserStatusEnum status) {
		this.status = status;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
