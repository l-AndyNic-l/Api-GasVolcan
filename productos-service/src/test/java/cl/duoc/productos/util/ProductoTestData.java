package cl.duoc.productos.util;

import cl.duoc.productos.model.Producto;
import net.datafaker.Faker;
import java.util.Locale;

public class ProductoTestData {
    private static final Faker faker = new Faker(Locale.of("es", "CL"));

    public static Producto crearProductoValido() {
        Producto p = new Producto();

        p.setSku("SKU-" + faker.number().digits(5));
        p.setNombre("Cilindro " + faker.company().name() + " 5kg");
        p.setMarca(faker.company().name());
        p.setCategoria("Gas");
        p.setPrecio(faker.number().numberBetween(5000, 50000));
        p.setStock(faker.number().numberBetween(1, 100));
        p.setDescripcion("Cilindro de gas de 5kg de la marca " + p.getMarca() + ".");
        p.setDespacho("Domicilio");
        p.setFormato("Cilindro individual");
        p.setInformacion_adicional("Envase retornable. No incluye manguera ni regulador.");

        return p;
    }
}