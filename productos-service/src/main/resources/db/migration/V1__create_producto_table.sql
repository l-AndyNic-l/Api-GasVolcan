CREATE TABLE producto (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          sku VARCHAR(255) NOT NULL,
                          nombre VARCHAR(255) NOT NULL,
                          marca VARCHAR(255) NOT NULL,
                          categoria VARCHAR(255) NOT NULL,
                          precio INT NOT NULL,
                          stock INT NOT NULL,
                          descripcion VARCHAR(255) NOT NULL,
                          despacho VARCHAR(255) NOT NULL,
                          formato VARCHAR(255) NOT NULL,
                          informacion_adicional VARCHAR(255),

                          CONSTRAINT uk_producto_sku UNIQUE (sku)
);