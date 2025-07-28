package com.example.haruapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HaruappApplication {

	public static void main(String[] args) {
		SpringApplication.run(HaruappApplication.class, args);
	}

}
