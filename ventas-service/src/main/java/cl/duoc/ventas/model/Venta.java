package cl.duoc.ventas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0, message = "El subtotal no puede ser negativo.")
    private int subtotal;

    @Min(value = 0, message = "El iva no puede ser negativo.")
    private int iva;

    @Min(value = 0, message = "El descuento no puede ser negativo.")
    private int descuento;

    @Min(value = 0, message = "El total no puede ser negativo.")
    private int total;

    @NotNull(message = "La fecha de venta es obligatoria.")
    @Column(nullable = false)
    private LocalDateTime fechaVenta;
}