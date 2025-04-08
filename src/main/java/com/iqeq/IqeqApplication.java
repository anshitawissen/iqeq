package com.iqeq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class IqeqApplication {

	public static void main(String[] args) {
		SpringApplication.run(IqeqApplication.class, args);
	}

}
