package cl.duoc.api_inventarios.config;

// ¡Fíjate que ahora todas dicen "models", no "annotations"!
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de gestion de Inventario y Auditoria")
                        .description("Ahí se gestionarán el inventario y las auditorias")
                        .version("1.0")
                        .contact(new Contact()
                                .name("GRUPO2_Inv")
                                .email("bayr.cerda@duocuc.cl"))
                        .license(new License()
                                .name("solo para uso académico"))
                );
    }
}