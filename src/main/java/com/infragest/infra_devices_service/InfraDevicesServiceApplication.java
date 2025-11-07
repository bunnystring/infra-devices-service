package com.infragest.infra_devices_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InfraDevicesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfraDevicesServiceApplication.class, args);
	}

}
