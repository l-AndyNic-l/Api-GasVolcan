package cl.duoc.productos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El sku es obligatorio.")
    @Size(min = 3, message = "El sku debe tener al menos 3 caracteres.")
    @Column(unique = true)
    private String sku;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres.")
    private String nombre;

    @NotBlank(message = "La marca es obligatoria.")
    @Size(min = 3, message = "La marca debe tener al menos 3 caracteres.")
    private String marca;

    @Min(value = 0, message = "El precio no puede ser negativo.")
    private int precio;

    @Min(value = 0, message = "El stock no puede ser negativo.")
    private int stock;

    @NotBlank(message = "La descripción es obligatoria.")
    private String descripcion;

    @NotBlank(message = "El despacho es obligatorio.")
    private String despacho;

    @NotBlank(message = "El formato es obligatorio.")
    private String formato;

    private String informacion_adicional;
}
