-- Desactivar temporalmente chequeo de claves foráneas para truncar tablas
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE envios;
TRUNCATE TABLE repartidores;
TRUNCATE TABLE clientes;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO clientes (id, nombre, email, telefono, created_at, updated_at) VALUES
    (UUID_TO_BIN('11111111-1111-1111-1111-111111111111', 1), 'Juan Perez', 'juan.perez@logitrack.com', '+54 388 4123456', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (UUID_TO_BIN('22222222-2222-2222-2222-222222222222', 1), 'Maria Lopez', 'maria.lopez@logitrack.com', '+54 388 4987654', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (UUID_TO_BIN('33333333-3333-3333-3333-333333333333', 1), 'Lucas Gomez', 'lucas.gomez@logitrack.com', '+54 388 4771122', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


INSERT INTO repartidores (id, nombre, tipo_vehiculo, estado, fecha_creacion, fecha_ultima_modificacion) VALUES
    (UUID_TO_BIN('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 1), 'Carlos Gomez', 'MOTO', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 1), 'Lucia Fernandez', 'UTILITARIO', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (UUID_TO_BIN('cccccccc-cccc-cccc-cccc-cccccccccccc', 1), 'Pedro Diaz', 'BICICLETA', 'INACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO envios (
    id, codigo_seguimiento, peso, ancho, alto, largo, tarifa, estado,
    origen_calle, origen_numero, origen_ciudad, origen_codigo_postal,
    destino_calle, destino_numero, destino_ciudad, destino_codigo_postal,
    repartidor_id, fecha_creacion, fecha_ultima_modificacion
) VALUES
    (UUID_TO_BIN('a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d', 1), 'TRACK-JUJ-001', 2.50, 10.0, 15.0, 20.0, 2125.00, 'PENDIENTE', 'Lavalle', '340', 'San Salvador de Jujuy', '4600', 'Av. 19 de Abril', '1050', 'San Salvador de Jujuy', '4600', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (UUID_TO_BIN('b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e', 1), 'TRACK-PAL-002', 5.00, 20.0, 20.0, 30.0, 2750.00, 'EN_RUTA',   'Alvear',  '112', 'San Salvador de Jujuy', '4600', 'Av. Martijena',    '320',  'Palpalá',              '4612', UUID_TO_BIN('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (UUID_TO_BIN('c3d4e5f6-a7b8-9c0d-1e2f-3a4b5c6d7e8f', 1), 'TRACK-JUJ-003', 1.20, 5.0,  10.0, 15.0, 1800.00, 'ENTREGADO', 'Necochea', '55',  'San Salvador de Jujuy', '4600', 'Belgrano',         '450',  'San Salvador de Jujuy', '4600', UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 1), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (UUID_TO_BIN('d4e5f6a7-b8c9-0d1e-2f3a-4b5c6d7e8f9a', 1), 'TRACK-PER-004', 3.00, 15.0, 15.0, 25.0, 2250.00, 'CANCELADO', 'Gorriti',  '890', 'San Salvador de Jujuy', '4600', 'Av. Italia',       '125',  'Perico',                '4610', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);