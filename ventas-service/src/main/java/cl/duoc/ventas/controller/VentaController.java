package cl.duoc.ventas.controller;

import cl.duoc.ventas.model.Venta;
import cl.duoc.ventas.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/ventas")
@RestController
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public ResponseEntity<?> listarVentas() {
        List<Venta> ventas = ventaService.getAll();
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> obtenerVenta(@PathVariable Long id) {
        Venta venta = ventaService.getOne(id);

        if (venta == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(venta);
    }

    @PostMapping
    public ResponseEntity<?> registrarVenta(@Valid @RequestBody Venta venta) {
        try {
            Venta nuevaVenta = ventaService.save(venta);
            return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrarVenta(@PathVariable Long id) {
        ventaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVenta(@PathVariable Long id, @RequestBody Venta venta) {
        Venta ventaActualizada = ventaService.update(id, venta);

        if (ventaActualizada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ventaActualizada);
    }
}
