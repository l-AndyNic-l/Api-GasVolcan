package cl.duoc.usuarios.util;

import cl.duoc.usuarios.model.Usuario;
import net.datafaker.Faker;
import java.util.Locale;

public class UsuarioTestData {
    private static final Faker faker = new Faker(new Locale("es-CL"));

    public static Usuario crearUsuarioValido() {
        Usuario u = new Usuario();

        u.setRut(String.valueOf(faker.number().numberBetween(10000000, 99999999)));
        u.setDv_rut(faker.regexify("[0-9K]"));
        u.setNombre(faker.name().firstName());
        u.setApellido(faker.name().lastName());
        u.setTelefono("9" + faker.number().digits(8));
        u.setDireccion(faker.address().fullAddress());
        u.setCorreo(faker.internet().emailAddress());
        u.setClave("Password" + faker.number().digits(4));
        u.setTipo_compra("Presencial");
        u.setTipo_cliente("Estándar");
        u.setFrecuencia_compra("Semanal");
        u.setLocal_comercial("Sucursal " + faker.address().cityName());
        u.setEstado_usuario("Activo");

        return u;
    }
}
