package cl.duoc.usuarios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El rut es obligatorio.")
    @Size(min = 8, message = "El rut debe tener al menos 8 caracteres.")
    private String rut;

    @NotBlank(message = "El dv_rut es obligatorio.")
    @Size(min = 1, max = 1, message = "El dv_rut debe tener 1 caracteres.")
    private String dv_rut;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres.")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio.")
    @Size(min = 3, message = "El apellido debe tener al menos 3 caracteres.")
    private String apellido;

    @NotBlank(message = "El telefono es obligatorio.")
    @Size(min = 9, message = "El telefono debe tener 9 caracteres.")
    private String telefono;

    @NotBlank(message = "La direccion es obligatoria.")
    private String direccion;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "El correo debe tener un formato válido.")
    @Column(unique = true)
    private String correo;

    @NotBlank(message = "La clave es obligatoria.")
    @Size(min = 8, message = "La clave debe tener al menos 8 caracteres.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "La clave debe contener al menos una mayúscula, una minúscula y un número."
    )
    private String clave;

    @NotBlank(message = "El tipo de compra es obligatorio.")
    private String tipo_compra;

    @NotBlank(message = "El tipo de cliente es obligatorio.")
    private String tipo_cliente;

    @NotBlank(message = "La frecuencia de compra es obligatoria.")
    private String frecuencia_compra;

    @NotBlank(message = "El local comercial es obligatorio.")
    private String local_comercial;

    @NotBlank(message = "El estado de usuario es obligatorio.")
    private String estado_usuario;
}