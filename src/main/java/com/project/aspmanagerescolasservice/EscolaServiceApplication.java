package com.project.aspmanagerescolasservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EscolaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EscolaServiceApplication.class, args);
	}

}
