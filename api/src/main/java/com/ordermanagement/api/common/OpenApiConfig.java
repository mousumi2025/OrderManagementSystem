package com.ordermanagement.api.common;

import java.util.List;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadata shown in Swagger UI (/swagger-ui.html) and the OpenAPI spec (/v3/api-docs).
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI orderManagementOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Management API")
                        .version("1.0.0")
                        .description("""
                                CRUD operations for the Order Management System PostgreSQL database \
                                (customers, products, orders, order items and payments).

                                Errors are returned as RFC 9457 problem details: **400** validation failure, \
                                **404** record or referenced record not found, **409** unique or foreign-key \
                                constraint violation."""))
                .tags(List.of(
                        new Tag().name("Customers").description("Customer contact details"),
                        new Tag().name("Products").description("Product catalog, prices and stock"),
                        new Tag().name("Orders").description("Orders placed by customers"),
                        new Tag().name("Order Items").description("Line items linking orders to products"),
                        new Tag().name("Payments").description("Payments recorded against orders")));
    }
}
