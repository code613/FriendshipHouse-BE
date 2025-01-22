package com.levdevs.freindshipbe;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@OpenAPIDefinition(info = @Info(title = "Your API", version = "v1"))
@SpringBootApplication
@EnableAsync
public class FreindshipbeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FreindshipbeApplication.class, args);
	}
}
