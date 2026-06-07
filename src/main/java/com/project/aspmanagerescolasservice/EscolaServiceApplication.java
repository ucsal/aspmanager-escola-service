package com.project.aspmanagerescolasservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "ASPManager API - Escolas", version = "1.0", 
		description = "Microserviço de Gestão de Escolas, Instituições e Disciplinas"),
 	servers = {
        @Server(url = "http://localhost:8083", description = "Ambiente Local (Desenvolvimento)"),
        @Server(url = "http://localhost:8080/api/v1/orq/escola", description = "API Gateway (Produção)")
    })
@EnableDiscoveryClient
public class EscolaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EscolaServiceApplication.class, args);
	}

}
