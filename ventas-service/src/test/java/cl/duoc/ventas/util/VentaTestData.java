package cl.duoc.ventas.util;

import cl.duoc.ventas.model.Venta;
import net.datafaker.Faker;
import java.time.LocalDateTime;
import java.util.Locale;

public class VentaTestData {
    private static final Faker faker = new Faker(Locale.of("es", "CL"));

    public static Venta crearVentaValida() {
        Venta v = new Venta();

        v.setSubtotal(faker.number().numberBetween(1000, 50000));
        v.setIva(faker.number().numberBetween(100, 10000));
        v.setDescuento(faker.number().numberBetween(0, 5000));
        v.setTotal(v.getSubtotal() + v.getIva() - v.getDescuento());
        v.setFechaVenta(LocalDateTime.now());

        return v;
    }
}