package com.vitorsaucedo.buscadorjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BuscadorjavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuscadorjavaApplication.class, args);
	}

}
