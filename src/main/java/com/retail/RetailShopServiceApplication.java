package com.retail;

import com.google.maps.GeoApiContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableConfigurationProperties
@EnableSwagger2
public class RetailShopServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(RetailShopServiceApplication.class, args);
	}

	@Bean
	public Docket serviceApi() {
		ApiInfo apiInfo = new ApiInfoBuilder()
				.title("Retail Shop Service API")
				.build();

		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("retailShopService")
				.apiInfo(apiInfo)
				.genericModelSubstitutes(ResponseEntity.class)
				.pathMapping("/")
				.select()
					.apis(RequestHandlerSelectors.any())
					.paths(PathSelectors.ant("/api/**"))
					.build();
	}

	@Bean
	GeoApiContext geoApiContext() {
		return new GeoApiContext();
	}
}
