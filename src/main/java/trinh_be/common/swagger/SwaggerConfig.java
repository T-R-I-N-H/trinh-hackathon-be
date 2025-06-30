package trinh_be.common.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "VPBank Hackathon 2025 Backend API",
                version = "1.0",
                description = "Trình"
        ),
        servers = {
                @Server(url = "13.250.85.147", description = "Production Server"),
                @Server(url = "http://localhost:8080", description = "Local Server")
        }
)
public class SwaggerConfig {
}
