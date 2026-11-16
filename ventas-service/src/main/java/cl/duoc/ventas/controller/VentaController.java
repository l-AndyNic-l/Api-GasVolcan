package cl.duoc.ventas.controller;

import cl.duoc.ventas.model.Venta;
import cl.duoc.ventas.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/ventas")
@RestController
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    @Operation(
            summary = "Obtener todos las ventas",
            description = "Obtiene el listado completo de las ventas"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista obtenida correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "array", implementation = Venta.class)
                    )
            )
    })
    public CollectionModel<EntityModel<Venta>> listarVentas() {
        List<Venta> ventas = ventaService.getAll();

        List<EntityModel<Venta>> ventasConLinks = ventas.stream()
                .map(venta -> EntityModel.of(venta,
                        linkTo(methodOn(VentaController.class).obtenerVenta(venta.getId())).withSelfRel()
                ))
                .collect(Collectors.toList());

        return CollectionModel.of(ventasConLinks,
                linkTo(methodOn(VentaController.class).listarVentas()).withSelfRel()
        );
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Obtener venta",
            description = "Obtiene los detalles de la venta por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Venta encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Venta.class),
                            examples = @ExampleObject(
                                    name = "Venta encontrada",
                                    value = """
                                        {
                                            "id": "1",
                                            "subtotal": 10000,
                                            "iva": 1900,
                                            "descuento": 500,
                                            "total": 11400,
                                            "fechaVenta": "2024-05-20T14:30:00"
                                        }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venta no encontrado",
                    content = @Content
            )
    })
    public EntityModel<Venta> obtenerVenta(
            @Parameter(description = "ID de la venta a buscar", example = "1", required = true)
            @PathVariable Long id
    ) {
        Venta venta = ventaService.getOne(id);

        if (venta == null) {
            throw new RuntimeException("Venta no encontrada");
        }

        return EntityModel.of(venta,
                linkTo(methodOn(VentaController.class).obtenerVenta(id)).withSelfRel(),
                linkTo(methodOn(VentaController.class).listarVentas()).withRel("ventas")
        );
    }

    @PostMapping
    @Operation(
            summary = "Crear venta",
            description = "Crea una nueva venta en el sistema con los datos proporcionados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Venta creada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Venta.class),
                            examples = @ExampleObject(
                                    name = "Venta creada",
                                    value = """
                                        {
                                            "id": "1",
                                            "subtotal": 10000,
                                            "iva": 1900,
                                            "descuento": 500,
                                            "total": 11400,
                                            "fechaVenta": "2024-05-20T14:30:00"
                                        }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content
            )
    })
    public ResponseEntity<?> registrarVenta(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la nueva venta",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ejemplo de creación",
                                    value = """
                                        {
                                            "subtotal": 10000,
                                            "iva": 1900,
                                            "descuento": 500,
                                            "total": 11400,
                                            "fechaVenta": "2024-05-20T14:30:00"
                                        }
                                """
                            )
                    )
            )
            @RequestBody Venta venta
    ) {
        try {
            Venta nuevaVenta = ventaService.save(venta);
            return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar venta",
            description = "Elimina una venta del sistema por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Venta eliminada correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venta no encontrada",
                    content = @Content
            )
    })
    public ResponseEntity<?> borrarVenta(
            @Parameter(description = "ID de la venta a eliminar", example = "1", required = true)
            @PathVariable Long id
    ) {
        ventaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar venta",
            description = "Actualiza los datos de una venta existente identificada por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Venta actualizada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Venta.class),
                            examples = @ExampleObject(
                                    name = "Venta actualizada",
                                    value = """
                                        {
                                            "id": "1",
                                            "subtotal": 12590,
                                            "iva": 1900,
                                            "descuento": 680,
                                            "total": 11400,
                                            "fechaVenta": "2024-05-20T14:30:00"
                                        }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venta no encontrada",
                    content = @Content
            )
    })
    public ResponseEntity<?> actualizarVenta(
            @Parameter(description = "ID de la venta a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la venta actualizados",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ejemplo de actualización",
                                    value = """
                                        {
                                            "subtotal": 12590,
                                            "iva": 1900,
                                            "descuento": 680,
                                            "total": 11400,
                                            "fechaVenta": "2024-05-20T14:30:00"
                                        }
                                """
                            )
                    )
            )
            @RequestBody Venta venta
    ) {
        Venta ventaActualizada = ventaService.update(id, venta);

        if (ventaActualizada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ventaActualizada);
    }
}
