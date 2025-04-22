package com.dntsystems.susu.utils;

public class ApiResponse<T> {

	private boolean status;
	private String message;
	private T result;
	public ApiResponse(boolean status, String message, T result) {
		super();
		this.status = status;
		this.message = message;
		this.result = result;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
}
