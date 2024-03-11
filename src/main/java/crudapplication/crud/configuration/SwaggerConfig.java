package crudapplication.crud.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfig implementation for custom configuration of documentation.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
		
	/**
	 * It create Docket using api information
	 * @return {@link Docket}
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
//				.ignoredParameterTypes(Iterable.class)
				.select()
				.apis(RequestHandlerSelectors.withClassAnnotation(EnableDocumentation.class))
				.paths(PathSelectors.any())
				.build();
	}
	
	/**
	 * It create API information using title, description and version
	 * @return {@link ApiInfo}
	 */
	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()              
                .title("Crud Application")
                .description("Simple Crud Application using MVC and REST api")
                .version("1.0.0")
                .build();
    }
}
