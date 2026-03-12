package com.pragma.bootcamps.capability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CapabilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapabilityApplication.class, args);
	}

}
