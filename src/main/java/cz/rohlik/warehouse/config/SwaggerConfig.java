package cz.rohlik.warehouse.config;

import static com.google.common.base.Predicates.not;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    public static final String TAG_PRODUCTS = "product";
    public static final String TAG_ORDERS = "order";
    private static final Tag PRODUCTS = new Tag(TAG_PRODUCTS, "Operations related to Product domain");
    private static final Tag ORDERS = new Tag(TAG_ORDERS, "Operations related to Order domain");

    @Bean
    public Docket publicApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .tags(PRODUCTS)
                .tags(ORDERS)
                .groupName("public")
                .apiInfo(publicApiInfo())
                .useDefaultResponseMessages(false)
                .select()
                // exclude all the Spring Controllers (such as Error, Management, etc.)
                .apis(not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .build();
    }

    private ApiInfo publicApiInfo() {
        return new ApiInfoBuilder()
                .title("Warehouse API")
                .description("The Warehouse API is a RESTful API that provides functionality for managing products and orders")
                .version("1.0")
                .build();
    }
}
