package com.dntsystems.susu.exception;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 7108053801965763454L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}
