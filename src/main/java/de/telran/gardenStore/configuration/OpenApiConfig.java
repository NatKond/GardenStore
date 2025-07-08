package de.telran.gardenStore.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Graduation project",
                description = "OpenApi documentation for graduation project: online garden store",
                version = "1.0.0",
                contact = @Contact(
                        name = "Natalia Kondratenko"
                )
        ), servers = {
            @Server(
                    description = "Local env",
                    url = "http://localhost:8082"
            )
        }
)
public class OpenApiConfig {

}
