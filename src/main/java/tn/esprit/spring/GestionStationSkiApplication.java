package tn.esprit.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication // Marks this class as a Spring Boot application
@EnableScheduling      // Enables Spring’s scheduled task execution
public class GestionStationSkiApplication {

	// Main method to start the Spring Boot application
	public static void main(String[] args) {
		SpringApplication.run(GestionStationSkiApplication.class, args);
	}
}
