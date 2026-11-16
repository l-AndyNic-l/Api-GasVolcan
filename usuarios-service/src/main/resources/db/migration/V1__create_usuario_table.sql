CREATE TABLE usuario (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         rut VARCHAR(8) NOT NULL,
                         dv_rut VARCHAR(1) NOT NULL,
                         nombre VARCHAR(255) NOT NULL,
                         apellido VARCHAR(255) NOT NULL,
                         telefono VARCHAR(9) NOT NULL,
                         direccion VARCHAR(255) NOT NULL,
                         correo VARCHAR(255) NOT NULL UNIQUE,
                         clave VARCHAR(255) NOT NULL,
                         tipo_compra VARCHAR(255) NOT NULL,
                         tipo_cliente VARCHAR(255) NOT NULL,
                         frecuencia_compra VARCHAR(255) NOT NULL,
                         local_comercial VARCHAR(255) NOT NULL,
                         estado_usuario VARCHAR(255) NOT NULL,
                         CONSTRAINT uk_usuario_correo UNIQUE (correo)
);