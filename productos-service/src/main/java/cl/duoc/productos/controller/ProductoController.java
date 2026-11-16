package cl.duoc.productos.controller;

import cl.duoc.productos.model.Producto;
import cl.duoc.productos.service.ProductoService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/productos")
@RestController
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(
            summary = "Obtener todos los productos",
            description = "Obtiene el listado completo de los productos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista obtenida correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "array", implementation = Producto.class)
                    )
            )
    })
    public CollectionModel<EntityModel<Producto>> listarProductos() {
        List<Producto> productos = productoService.getAll();

        List<EntityModel<Producto>> productosConLinks = productos.stream()
                .map(venta -> EntityModel.of(venta,
                        linkTo(methodOn(ProductoController.class).obtenerProducto(venta.getId())).withSelfRel()
                ))
                .collect(Collectors.toList());

        return CollectionModel.of(productosConLinks,
                linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel()
        );
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Obtener producto",
            description = "Obtiene los detalles del producto por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Producto.class),
                            examples = @ExampleObject(
                                    name = "Producto encontrado",
                                    value = """
                                        {
                                            "id": "1",
                                            "sku": "ABS-001",
                                            "categoria": "Gas",
                                            "nombre": "Cilindro Abastible 5kg",
                                            "descripcion": "Cilindro de gas de 5kg de la marca Abastible.",
                                            "precio": 11000,
                                            "marca": "Abastible",
                                            "stock": 10,
                                            "despacho": "Domicilio",
                                            "formato": "Cilindro individual",
                                            "informacion_adicional": "Envase retornable. No incluye manguera ni regulador."
                                        }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content
            )
    })
    public EntityModel<Producto> obtenerProducto(
            @Parameter(description = "ID del producto a buscar", example = "1", required = true)
            @PathVariable Long id
    ) {
        Producto producto = productoService.getOne(id);

        if (producto == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        return EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).obtenerProducto(id)).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarProductos()).withRel("productos")
        );
    }

    @PostMapping
    @Operation(
            summary = "Crear producto",
            description = "Crea un nuevo producto en el sistema con los datos proporcionados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto creado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Producto.class),
                            examples = @ExampleObject(
                                    name = "Producto creado",
                                    value = """
                                        {
                                            "id": "1",
                                            "sku": "ABS-001",
                                            "categoria": "Gas",
                                            "nombre": "Cilindro Abastible 5kg",
                                            "descripcion": "Cilindro de gas de 5kg de la marca Abastible.",
                                            "precio": 11000,
                                            "marca": "Abastible",
                                            "stock": 10,
                                            "despacho": "Domicilio",
                                            "formato": "Cilindro individual",
                                            "informacion_adicional": "Envase retornable. No incluye manguera ni regulador."
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
    public ResponseEntity<?> registrarProducto(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo producto",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ejemplo de creación",
                                    value = """
                                        {
                                            "sku": "ABS-001",
                                            "categoria": "Gas",
                                            "nombre": "Cilindro Abastible 5kg",
                                            "descripcion": "Cilindro de gas de 5kg de la marca Abastible.",
                                            "precio": 11000,
                                            "marca": "Abastible",
                                            "stock": 10,
                                            "despacho": "Domicilio",
                                            "formato": "Cilindro individual",
                                            "informacion_adicional": "Envase retornable. No incluye manguera ni regulador."
                                        }
                                """
                            )
                    )
            )
            @RequestBody Producto producto
    ) {
        try {
            Producto nuevoProducto = productoService.save(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar producto",
            description = "Elimina un producto del sistema por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto eliminado correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content
            )
    })
    public ResponseEntity<?> borrarProducto(
            @Parameter(description = "ID del producto a eliminar", example = "1", required = true)
            @PathVariable Long id
    ) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar producto",
            description = "Actualiza los datos de un producto existente identificado por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Producto.class),
                            examples = @ExampleObject(
                                    name = "Producto actualizado",
                                    value = """
                                        {
                                            "id": "1",
                                            "sku": "ABS-001",
                                            "categoria": "Gas",
                                            "nombre": "Cilindro Lipigas 5kg",
                                            "descripcion": "Cilindro de gas de 5kg de la marca Abastible.",
                                            "precio": 11000,
                                            "marca": "Abastible",
                                            "stock": 10,
                                            "despacho": "Domicilio",
                                            "formato": "Cilindro individual",
                                            "informacion_adicional": "Envase retornable. No incluye manguera ni regulador."
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
                    description = "Producto no encontrado",
                    content = @Content
            )
    })
    public ResponseEntity<?> actualizarProducto(
            @Parameter(description = "ID del producto a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del producto actualizados",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ejemplo de actualización",
                                    value = """
                                      {
                                            "sku": "ABS-001",
                                            "categoria": "Gas",
                                            "nombre": "Cilindro Lipigas 5kg",
                                            "descripcion": "Cilindro de gas de 5kg de la marca Abastible.",
                                            "precio": 11000,
                                            "marca": "Abastible",
                                            "stock": 10,
                                            "despacho": "Domicilio",
                                            "formato": "Cilindro individual",
                                            "informacion_adicional": "Envase retornable. No incluye manguera ni regulador."
                                      }
                                """
                            )
                    )
            )
            @RequestBody Producto producto
    ) {
        Producto productoactualizado = productoService.update(id, producto);

        if (productoactualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productoactualizado);
    }
}
