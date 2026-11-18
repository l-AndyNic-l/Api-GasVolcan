package cl.duoc.usuarios.controller;

import cl.duoc.usuarios.dto.LoginRequest;
import cl.duoc.usuarios.model.Usuario;
import cl.duoc.usuarios.security.JwtUtil;
import cl.duoc.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RequestMapping("/api/v1/usuarios")
@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Obtiene el listado completo de los usuarios"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista obtenida correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "array", implementation = Usuario.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autorizado. Debes enviar un token JWT válido en el header Authorization",
                    content = @Content
            ),
    })
    public CollectionModel<EntityModel<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.getAll();

        List<EntityModel<Usuario>> usuariosConLinks = usuarios.stream()
                .map(usuario -> EntityModel.of(usuario,
                        linkTo(methodOn(UsuarioController.class).obtenerUsuario(usuario.getId())).withSelfRel()
                ))
                .collect(Collectors.toList());

        return CollectionModel.of(usuariosConLinks,
                linkTo(methodOn(UsuarioController.class).listarUsuarios()).withSelfRel()
        );
    }

    @GetMapping("{id}")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Obtener usuario",
            description = "Obtiene los detalles del usuario por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(
                                    name = "Usuario encontrado",
                                    value = """
                                        {
                                            "id": 1,
                                            "rut": "98765432",
                                            "dv_rut": "K",
                                            "nombre": "María",
                                            "apellido": "González",
                                            "telefono": "912345678",
                                            "direccion": "Calle Falsa 123, Depto 4B",
                                            "correo": "maria.gonzalez@example.com",
                                            "tipo_compra": "Presencial",
                                            "tipo_cliente": "Estándar",
                                            "frecuencia_compra": "Semanal",
                                            "local_comercial": "Sucursal Providencia",
                                            "estado_usuario": "Activo"
                                        }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autorizado. Debes enviar un token JWT válido en el header Authorization",
                    content = @Content
            ),
    })
    public EntityModel<Usuario> obtenerUsuario(
            @Parameter(description = "ID del usuario a buscar", example = "1", required = true)
            @PathVariable Long id
    ) {
        Usuario usuario = usuarioService.getOne(id);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtenerUsuario(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("usuarios")
        );
    }

    @PostMapping
    @Operation(
            summary = "Crear usuario",
            description = "Crea un nuevo usuario en el sistema con los datos proporcionados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(
                                    name = "Usuario creado",
                                    value = """
                                        {
                                            "id": 1,
                                            "rut": "98765432",
                                            "dv_rut": "K",
                                            "nombre": "María",
                                            "apellido": "González",
                                            "telefono": "912345678",
                                            "direccion": "Calle Falsa 123, Depto 4B",
                                            "correo": "maria.gonzalez@example.com",
                                            "tipo_compra": "Presencial",
                                            "tipo_cliente": "Estándar",
                                            "frecuencia_compra": "Semanal",
                                            "local_comercial": "Sucursal Providencia",
                                            "estado_usuario": "Activo"
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
                    responseCode = "401",
                    description = "No autorizado. Debes enviar un token JWT válido en el header Authorization",
                    content = @Content
            ),
    })
    public ResponseEntity<?> registrarUsuario(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo usuario",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ejemplo de creación",
                                    value = """
                                {
                                  "rut": "98765432",
                                  "dv_rut": "K",
                                  "nombre": "María",
                                  "apellido": "González",
                                  "telefono": "912345678",
                                  "direccion": "Calle Falsa 123, Depto 4B",
                                  "correo": "maria.gonzalez@example.com",
                                  "clave": "Segura2024",
                                  "tipo_compra": "Presencial",
                                  "tipo_cliente": "Estándar",
                                  "frecuencia_compra": "Semanal",
                                  "local_comercial": "Sucursal Providencia",
                                  "estado_usuario": "Activo"
                                }
                                """
                            )
                    )
            )
            @RequestBody Usuario usuario
    ) {
        try {
            Usuario nuevoUsuario = usuarioService.save(usuario);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario del sistema por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuario eliminado correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content
            )
    })
    public ResponseEntity<?> borrarUsuario(
            @Parameter(description = "ID del usuario a eliminar", example = "1", required = true)
            @PathVariable Long id
    ) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza los datos de un usuario existente identificado por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(
                                    name = "Usuario actualizado",
                                    value = """
                                        {
                                            "id": 1,
                                            "rut": "98765432",
                                            "dv_rut": "K",
                                            "nombre": "Pedro",
                                            "apellido": "González",
                                            "telefono": "912345678",
                                            "direccion": "Calle Falsa 123, Depto 4B",
                                            "correo": "maria.gonzalez@example.com",
                                            "tipo_compra": "Presencial",
                                            "tipo_cliente": "Estándar",
                                            "frecuencia_compra": "Semanal",
                                            "local_comercial": "Sucursal Providencia",
                                            "estado_usuario": "Activo"
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
                    description = "Usuario no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autorizado. Debes enviar un token JWT válido en el header Authorization",
                    content = @Content
            ),
    })
    public ResponseEntity<?> actualizarUsuario(
            @Parameter(description = "ID del usuario a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del usuario actualizados",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ejemplo de actualización",
                                    value = """
                                {
                                  "rut": "98765432",
                                  "dv_rut": "K",
                                  "nombre": "Pedro",
                                  "apellido": "González",
                                  "telefono": "912345678",
                                  "direccion": "Calle Falsa 123, Depto 4B",
                                  "correo": "maria.gonzalez@example.com",
                                  "clave": "Segura2024",
                                  "tipo_compra": "Presencial",
                                  "tipo_cliente": "Estándar",
                                  "frecuencia_compra": "Semanal",
                                  "local_comercial": "Sucursal Providencia",
                                  "estado_usuario": "Activo"
                                }
                                """
                            )
                    )
            )
            @RequestBody Usuario usuario
    ) {
        Usuario usuarioActualizado = usuarioService.update(id, usuario);

        if (usuarioActualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuarioActualizado);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Valida las credenciales del usuario y devuelve un token JWT para autenticar las siguientes peticiones."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login exitoso. Devuelve el token JWT.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Login exitoso",
                                    value = """
                                    {
                                        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXJpYS5nb256YWxlekBleGFtcGxlLmNvbSIsInVzZXJJZCI6MSwidGlwb0NsaWVudGUiOiJFc3TDoW5kYXIiLCJpYXQiOjE3MzE4MDAwMDAsImV4cCI6MTczMTgwMzYwMH0.abc123def456ghi789jkl012mno345pqr678stu901vwx234yzA"
                                    }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas. El correo no existe o la contraseña es incorrecta.",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "Credenciales inválidas",
                                    value = "Credenciales inválidas"
                            )
                    )
            )
    })
    public ResponseEntity<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciales del usuario (correo y clave en texto plano)",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ejemplo de login",
                                    value = """
                                    {
                                      "correo": "maria.gonzalez@example.com",
                                      "clave": "Segura2024"
                                    }
                                """
                            )
                    )
            )
            @RequestBody LoginRequest request
    ) {
        Usuario usuario = usuarioService.login(request.correo(), request.clave());

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(
                usuario.getId(),
                usuario.getCorreo(),
                usuario.getTipo_cliente()
        );

        return ResponseEntity.ok(Map.of("token", token));
    }
}
