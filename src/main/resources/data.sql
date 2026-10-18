TRUNCATE TABLE envios;

INSERT INTO envios (
    id, codigo_seguimiento, peso, ancho, alto, largo, tarifa, estado,
    origen_calle, origen_numero, origen_ciudad, origen_codigo_postal,
    destino_calle, destino_numero, destino_ciudad, destino_codigo_postal,
    fecha_creacion, fecha_ultima_modificacion
) VALUES
      (UUID_TO_BIN('a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d', 1), 'TRACK-JUJ-001', 2.50, 10.0, 15.0, 20.0, 2125.00, 'PENDIENTE', 'Lavalle', '340', 'San Salvador de Jujuy', '4600', 'Av. 19 de Abril', '1050', 'San Salvador de Jujuy', '4600', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (UUID_TO_BIN('b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e', 1), 'TRACK-PAL-002', 5.00, 20.0, 20.0, 30.0, 2750.00, 'EN_RUTA',   'Alvear',  '112', 'San Salvador de Jujuy', '4600', 'Av. Martijena',    '320',  'Palpalá',              '4612', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (UUID_TO_BIN('c3d4e5f6-a7b8-9c0d-1e2f-3a4b5c6d7e8f', 1), 'TRACK-JUJ-003', 1.20, 5.0,  10.0, 15.0, 1800.00, 'ENTREGADO', 'Necochea', '55', 'San Salvador de Jujuy', '4600', 'Belgrano',         '450',  'San Salvador de Jujuy', '4600', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (UUID_TO_BIN('d4e5f6a7-b8c9-0d1e-2f3a-4b5c6d7e8f9a', 1), 'TRACK-PER-004', 3.00, 15.0, 15.0, 25.0, 2250.00, 'CANCELADO', 'Gorriti', '890', 'San Salvador de Jujuy', '4600', 'Av. Italia',       '125',  'Perico',                '4610', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);