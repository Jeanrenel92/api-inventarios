package cl.duoc.api_inventarios.config;

import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    /*@Bean
    public OpenAPI OpenApiConfig(){
        return new OpenAPI()
                .info(new Info()
                        .title("API del Inventario")
                        .description("Ahi se gestionara todas las ordenes de compra")
                        .version("1.0")
                        .contact(new Contact()
                                .name("GRUPO2_Inv")
                                .email("bayr.cerda@duocuc.cl"))
                        .license(new License()
                                .name("solo para uso academico"))
                );
    }*/
}
