package fr.diginamic.VroomVroomCar.config;

import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API VroomVroomCar")
                        .version("1.0")
                        .description("Cette API fournit des données pour le site VroomVroomCar.")
                        .contact(new Contact().name("VroomVroomCar Corp.")));
    }
}
