package com.cnjava.ElectronicsStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.cnjava.ElectronicsStore") // Make sure the package is correct
@EntityScan("com.cnjava.ElectronicsStore.Model") // Make sure the package is correct
public class ElectronicsStoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(ElectronicsStoreApplication.class, args);
	}

}
