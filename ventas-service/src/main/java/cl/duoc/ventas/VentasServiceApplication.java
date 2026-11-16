package cl.duoc.ventas;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "API Gas El Volcán",
				version = "1.0",
				description = """
            API RESTful que centraliza y facilita la comunicación con los 
            distintos microservicios de Gas El Volcán. Proporciona acceso 
            unificado a las funcionalidades de gestión de productos, usuarios 
            y ventas, permitiendo administrar la información y las operaciones 
            de la empresa de manera organizada, segura y eficiente.

            Esta API utiliza una arquitectura de microservicios y un API Gateway 
            como punto de entrada centralizado para las solicitudes.
            """
		)
)
public class VentasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VentasServiceApplication.class, args);
	}

}
