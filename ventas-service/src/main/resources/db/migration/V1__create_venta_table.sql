CREATE TABLE venta (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       subtotal INT NOT NULL CHECK (subtotal >= 0),
                       iva INT NOT NULL CHECK (iva >= 0),
                       descuento INT NOT NULL CHECK (descuento >= 0),
                       total INT NOT NULL CHECK (total >= 0),
                       fecha_venta DATETIME(6) NOT NULL
);