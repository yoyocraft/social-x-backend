package com.youyi.infra.mvc.configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

// @Configuration
// @EnableSwagger2
public class Swagger2Config {

    // @Bean
    public Docket coreApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(adminApiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.youyi.runner"))
            .paths(PathSelectors.any())
            .build();
    }

    private ApiInfo adminApiInfo() {
        return new ApiInfoBuilder()
            .title("SocialX")
            .version("1.0")
            .build();
    }

}
