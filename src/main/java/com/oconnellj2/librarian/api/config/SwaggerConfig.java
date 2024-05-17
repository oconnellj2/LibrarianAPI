package com.oconnellj2.librarian.api.config;

import java.util.Arrays;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Librarian API")
						.description("Spring Boot RESTful service representing a library.")
						.version("v1.0.0")
						.contact(new Contact()
								.name("James O'Connell")
								.email("jdo.info@pm.me")
								.url("http://oconnellj2.github.io"))
						.license(new License()
								.name("MIT License")
								.url("https://opensource.org/licenses/MIT")))
				.servers(Arrays.asList(
						new Server().url("http://localhost:8080").description("Development Server")));
	}

	@Bean
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder()
				.group("public")
				.pathsToMatch("/books/**")
				.build();
	}
}