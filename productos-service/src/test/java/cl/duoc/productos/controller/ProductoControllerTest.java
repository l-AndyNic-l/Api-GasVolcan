package cl.duoc.productos.controller;

import cl.duoc.productos.model.Producto;
import cl.duoc.productos.service.ProductoService;
import cl.duoc.productos.util.ProductoTestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
@DisplayName("Pruebas del Controlador de Productos")
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoService productoService;

    @Test
    @DisplayName("Obtener todos los productos: debe retornar 200 y la lista")
    void listarProductos_debeRetornar200ConLaLista() throws Exception {
        // Arrange:
        Producto p1 = ProductoTestData.crearProductoValido();
        p1.setId(1L);

        Producto p2 = ProductoTestData.crearProductoValido();
        p2.setId(2L);

        when(productoService.getAll()).thenReturn(List.of(p1, p2));

        // Act  Assert:
        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productoList.length()").value(2))
                .andExpect(jsonPath("$._embedded.productoList[0].id").value(1L))
                .andExpect(jsonPath("$._embedded.productoList[1].id").value(2L))
                .andExpect(jsonPath("$._embedded.productoList[0].nombre").value(p1.getNombre()))
                .andExpect(jsonPath("$._embedded.productoList[0].sku").value(p1.getSku()));
    }

    @Test
    @DisplayName("Actualizar producto: debe retornar 200 con el producto actualizado")
    void actualizarProducto() throws Exception {
        // Arrange:
        Producto producto = ProductoTestData.crearProductoValido();
        producto.setId(1L);
        producto.setNombre("Cilindro Lipigas 5kg");

        when(productoService.update(eq(1L), any(Producto.class))).thenReturn(producto);

        // Act - Assert:
        mockMvc.perform(put("/api/v1/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Cilindro Lipigas 5kg"))
                .andExpect(jsonPath("$.sku").value(producto.getSku()));
    }
}