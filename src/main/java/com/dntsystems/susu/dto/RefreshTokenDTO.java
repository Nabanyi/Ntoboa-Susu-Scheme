package com.dntsystems.susu.dto;

public class RefreshTokenDTO {

	private String refreshToken;

	public RefreshTokenDTO() {
		super();
	}

	public RefreshTokenDTO(String refreshToken) {
		super();
		this.refreshToken = refreshToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	
}
