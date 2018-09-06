package com.kodgames.wechattools.configurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfigurer {
    public static boolean active = false;

    @Bean
    public Docket api() {
//        boolean flag = !"pro".equalsIgnoreCase(active);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(active)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kodgames.wechattools"))
//                .paths(PathSelectors.any())
                .paths(PathSelectors.regex("/withdraw.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("微信公众后台系统API文档")
                .description("错误码列表: http://172.16.2.136:9010/html/code.html")
                .termsOfServiceUrl("http://www.kodgames.com/")
                .version("1.0")
                .build();
    }
}
