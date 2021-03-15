package fr.dawan.calendarproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Classe de démarrage >> Spring scannera fr.dawan.calendarproject et tout ces sous packages

@SpringBootApplication
public class CalendarprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendarprojectApplication.class, args);
	}

}
