package de.telran.gardenStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GardenStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(GardenStoreApplication.class, args);
	}

}
