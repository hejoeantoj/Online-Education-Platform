package com.cts.assignmentmodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class AssignmentModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssignmentModuleApplication.class, args);
	}

}
