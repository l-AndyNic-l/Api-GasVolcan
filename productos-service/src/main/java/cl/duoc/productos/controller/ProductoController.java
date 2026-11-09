package cl.duoc.productos.controller;

import cl.duoc.productos.model.Producto;
import cl.duoc.productos.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/productos")
@RestController
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<?> listarProductos() {
        List<Producto> productos = productoService.getAll();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> obtenerProducto(@PathVariable Long id) {
        Producto producto = productoService.getOne(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(producto);
    }

    @PostMapping
    public ResponseEntity<?> registrarProducto(@Valid @RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.save(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrarProducto(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto productoactualizado = productoService.update(id, producto);

        if (productoactualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productoactualizado);
    }
}
