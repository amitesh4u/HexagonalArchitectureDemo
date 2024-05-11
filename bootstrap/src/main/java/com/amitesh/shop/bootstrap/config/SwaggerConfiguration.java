package com.amitesh.shop.bootstrap.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

  /**
   * Bean for OpenAPI (Swagger for Spring Boot 3.x)
   * @return OpenAPI object
   */
  @Bean
  public OpenAPI openApi() {
    return new OpenAPI()
        .info(
            new Info()
                .title("HexagonalArchitecture")
                .description("Hexagonal Architecture Shopping Application")
                .version("v1.0.0"))
        .externalDocs(
            new ExternalDocumentation()
                .description("Hexagonal Architecture Documentation")
                .url("https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)"));
  }

}
