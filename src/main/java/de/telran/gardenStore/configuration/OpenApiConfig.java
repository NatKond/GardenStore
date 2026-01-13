package de.telran.gardenStore.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Graduation project",
                description = "OpenApi documentation for graduation project: online garden store",
                version = "1.0.0",
                contact = @Contact(
                        name = "CODE_CATS",
                        url = "https://github.com/NatKond/GardenStore"
                )
        ), servers = {
        @Server(
                description = "Local environment",
                url = "http://localhost:8080"
        ),
        @Server(
                description = "Deployed server",
                url = "http://51.20.105.119:8080"
        )
    },
        security = {
                @SecurityRequirement(name = "BearerAuth"),
                @SecurityRequirement(name = "BasicAuth")
        }
)
@SecuritySchemes({
    @SecurityScheme(
            name = "BearerAuth",
            description = "JWT authentication",
            scheme = "bearer",
            type = SecuritySchemeType.HTTP,
            bearerFormat = "JWT",
            in = SecuritySchemeIn.HEADER
    ),
    @SecurityScheme(
            name = "BasicAuth",
            description = "Basic authentication",
            scheme = "basic",
            type = SecuritySchemeType.HTTP,
            in = SecuritySchemeIn.HEADER
    )
})
public class OpenApiConfig {
}
