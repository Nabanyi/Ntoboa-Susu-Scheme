package com.dntsystems.susu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SusuApplication {

	public static void main(String[] args) {
		SpringApplication.run(SusuApplication.class, args);
		System.out.println("Susu running...");
	}
}
