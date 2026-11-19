package cl.duoc.usuarios.controller;

import cl.duoc.usuarios.model.Usuario;
import cl.duoc.usuarios.security.JwtUtil;
import cl.duoc.usuarios.service.UsuarioService;
import cl.duoc.usuarios.util.UsuarioTestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@DisplayName("Pruebas del Controlador de Usuarios")
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("Registrar usuario: debe retornar 201 y el usuario creado")
    void registrarUsuario() throws Exception {
        // Arrange:
        Usuario usuario = UsuarioTestData.crearUsuarioValido();
        usuario.setId(1L);

        when(usuarioService.save(any(Usuario.class))).thenReturn(usuario);

        // Act - Assert:
        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.correo").value(usuario.getCorreo()))
                .andExpect(jsonPath("$.nombre").value(usuario.getNombre()));
    }

    @Test
    @DisplayName("Login: debe retornar el token cuando las credenciales son válidas")
    void login() throws Exception {
        // Arrange:
        Usuario usuario = UsuarioTestData.crearUsuarioValido();
        usuario.setId(1L);

        when(usuarioService.login(usuario.getCorreo(), "Password123")).thenReturn(usuario);
        when(jwtUtil.generateToken(usuario.getId(), usuario.getCorreo(), usuario.getTipo_cliente()))
                .thenReturn("token-de-prueba");

        String loginJson = """
                {
                  "correo": "%s",
                  "clave": "Password123"
                }
                """.formatted(usuario.getCorreo());

        // Act - Assert:
        mockMvc.perform(post("/api/v1/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-de-prueba"));
    }
}
