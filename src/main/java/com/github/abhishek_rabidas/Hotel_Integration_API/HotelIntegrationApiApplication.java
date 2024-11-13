package com.github.abhishek_rabidas.Hotel_Integration_API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class HotelIntegrationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelIntegrationApiApplication.class, args);
	}

}
