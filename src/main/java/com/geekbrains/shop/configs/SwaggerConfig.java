package com.geekbrains.shop.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //указываем где лежат наши рест контроллеры
                .apis(RequestHandlerSelectors.basePackage("com.geekbrains.shop.controllers"))
                //и для каких потей нам нужна эта документация
                .paths(PathSelectors.regex("/api.*"))
                .build();
    }
}
