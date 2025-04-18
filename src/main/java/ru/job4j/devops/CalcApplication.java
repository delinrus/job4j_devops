package ru.job4j.devops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Calculation service.
 * This class initializes and starts the Spring Boot application.
 */
@SpringBootApplication
public class CalcApplication {

	/**
	 * Default constructor for CalcApplication.
	 * Initializes the Spring Boot application.
	 */
	public CalcApplication() {
	}

	/**
	 * Main entry point for the application.
	 * @param args command line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(CalcApplication.class, args);
	}
}
