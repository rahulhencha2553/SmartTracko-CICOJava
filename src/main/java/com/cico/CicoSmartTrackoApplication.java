package com.cico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CicoSmartTrackoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CicoSmartTrackoApplication.class, args);
	}

}
