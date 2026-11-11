CREATE DATABASE IF NOT EXISTS repuestos4x4
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE repuestos4x4;

-- HU-01: Roles y usuarios
CREATE TABLE rol (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE usuario (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre    VARCHAR(80)  NOT NULL,
    correo    VARCHAR(120) NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL,
    telefono  VARCHAR(20),
    activo    BOOLEAN NOT NULL DEFAULT TRUE,
    rol_id    BIGINT NOT NULL,
    FOREIGN KEY (rol_id) REFERENCES rol(id)
);

-- HU-02 y HU-03: Catálogo
CREATE TABLE categoria (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(60) NOT NULL UNIQUE,
    activa  BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE marca (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(60) NOT NULL UNIQUE
);

CREATE TABLE producto (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo         VARCHAR(40)  NOT NULL UNIQUE,
    nombre         VARCHAR(120) NOT NULL,
    descripcion    TEXT,
    precio         DECIMAL(10,2) NOT NULL,
    stock          INT NOT NULL DEFAULT 0,
    imagen_url     VARCHAR(255),
    compatibilidad VARCHAR(255),
    activo         BOOLEAN NOT NULL DEFAULT TRUE,
    categoria_id   BIGINT NOT NULL,
    marca_id       BIGINT NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id),
    FOREIGN KEY (marca_id) REFERENCES marca(id)
);

-- HU-04: Carrito
CREATE TABLE carrito (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id  BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE carrito_item (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    carrito_id   BIGINT NOT NULL,
    producto_id  BIGINT NOT NULL,
    cantidad     INT NOT NULL,
    FOREIGN KEY (carrito_id) REFERENCES carrito(id),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

-- HU-05: Pedidos (tabla transaccional)
CREATE TABLE pedido (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id        BIGINT NOT NULL,
    fecha             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado            VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    direccion_entrega VARCHAR(255) NOT NULL,
    metodo_pago       VARCHAR(30) NOT NULL,
    total             DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE detalle_pedido (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id        BIGINT NOT NULL,
    producto_id      BIGINT NOT NULL,
    cantidad         INT NOT NULL,
    precio_unitario  DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

-- Datos iniciales
INSERT INTO rol (nombre) VALUES ('ADMIN'), ('CLIENTE'), ('ENCARGADO');

INSERT INTO categoria (nombre) VALUES
  ('Luces LED'), ('Winches y accesorios'), ('Suspensión'),
  ('Accesorios exteriores'), ('Transmisión');

INSERT INTO marca (nombre) VALUES
  ('Toyota'), ('Jeep'), ('Nissan'), ('Mitsubishi'), ('Universal');

INSERT INTO producto
  (codigo, nombre, descripcion, precio, stock, imagen_url, compatibilidad, categoria_id, marca_id)
VALUES
  ('LED-001', 'Barra LED 22"', 'Barra de luz LED doble hilera', 45000.00, 15, 'https://via.placeholder.com/300', 'Universal', 1, 5),
  ('WIN-001', 'Winch 9500 lb', 'Winch eléctrico con control remoto', 185000.00, 5, 'https://via.placeholder.com/300', 'Toyota Hilux 2005-2020', 2, 1),
  ('SUS-001', 'Kit de suspensión 2"', 'Lift kit con amortiguadores', 220000.00, 0, 'https://via.placeholder.com/300', 'Jeep Wrangler JK 2007-2018', 3, 2);