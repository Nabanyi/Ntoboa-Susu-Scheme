package com.dntsystems.susu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private String email;
    private String firstname;
    private String middlename;
    private String lastname;
    private String phone;
    
    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(Long id, String address, String email, String firstname, String middlename, String lastname,
			String phone, String username, String password, UserRoleEnum role, UserStatusEnum status) {
		super();
		this.id = id;
		this.address = address;
		this.email = email;
		this.firstname = firstname;
		this.middlename = middlename;
		this.lastname = lastname;
		this.phone = phone;
		this.username = username;
		this.password = password;
		this.role = role;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public UserStatusEnum getStatus() {
		return status;
	}

	public void setStatus(UserStatusEnum status) {
		this.status = status;
	}
}

