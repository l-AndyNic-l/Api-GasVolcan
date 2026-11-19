package cl.duoc.ventas.controller;

import cl.duoc.ventas.model.Venta;
import cl.duoc.ventas.service.VentaService;
import cl.duoc.ventas.util.VentaTestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VentaController.class)
@DisplayName("Pruebas del Controlador de Ventas")
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VentaService ventaService;

    @Test
    @DisplayName("Obtener venta: debe retornar 200 y los datos de la venta")
    void obtenerVenta() throws Exception {
        // Arrange:
        Venta venta = VentaTestData.crearVentaValida();
        venta.setId(1L);

        when(ventaService.getOne(1L)).thenReturn(venta);

        // Act - Assert:
        mockMvc.perform(get("/api/v1/ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.subtotal").value(venta.getSubtotal()))
                .andExpect(jsonPath("$.total").value(venta.getTotal()));
    }

    @Test
    @DisplayName("Eliminar venta: debe retornar 204 cuando la venta existe")
    void eliminarVenta() throws Exception {
        // Arrange: no hace falta simular nada, delete() es void.

        // Act - Assert
        mockMvc.perform(delete("/api/v1/ventas/1"))
                .andExpect(status().isNoContent());

        // Verify:
        verify(ventaService, times(1)).delete(1L);
    }
}