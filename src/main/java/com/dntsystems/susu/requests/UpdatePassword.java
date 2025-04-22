package com.dntsystems.susu.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;

public class UpdatePassword {
	@JsonProperty("current_password")
	@NotEmpty(message = "Current Password is required")
	private String currentPassword;
    
    @JsonProperty("new_password")
    @NotEmpty(message = "New Password is required")
    private String newPassword;
    
    @JsonProperty("confirm_password")
    @NotEmpty(message = "Confirm Password is required")
    private String confirmPassword;
    

	public UpdatePassword() {
		super();
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
