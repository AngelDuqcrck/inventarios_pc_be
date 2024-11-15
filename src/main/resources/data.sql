ALTER DATABASE inventarios_pc_csa CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
-- Creamos la tabla roles si no existe
CREATE TABLE IF NOT EXISTS roles
(
    id
    INT
    NOT
    NULL
    AUTO_INCREMENT,
    nombre
    VARCHAR (100) NOT NULL,
    delete_flag BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
    );

-- Insertamos los roles si estos no existen en el sistema 
INSERT
IGNORE INTO roles (delete_flag,id,nombre) VALUES
    (0,1,"ADMIN"),
    (0,2,"EMPLEADO_ASISTENCIAL"),
    (0,3,"EMPLEADO_ADMINISTRATIVO"),
    (0,4,"TECNICO_SISTEMAS");

-- Creamos la tabla sedes_PC si no existe
CREATE TABLE IF NOT EXISTS sedes_pc
(
    id INT NOT NULL AUTO_INCREMENT,
    direccion VARCHAR(190) NOT NULL,
    descripcion TEXT NOT NULL,
    nombre VARCHAR(190) NOT NULL,
    delete_flag BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

-- Insertamos las sedes si no existen en el sistema
INSERT IGNORE INTO sedes_pc (id, direccion, descripcion, nombre, delete_flag) VALUES
    (1, 'Av. 11E# 8-41COLSAG', 'Principal...', 'Sede Principal', 0),
    (2, 'Cl. 8 # 11E-62 COLSAG', 'COLSAG...', 'Sede Colsag', 0),
    (3, 'COLSAG', 'ESPECIALISTAS...', 'Centro de Especialistas', 0);

-- Creamos la tabla areas_PC si no existe
CREATE TABLE IF NOT EXISTS areas_pc (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(190) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    delete_flag BOOLEAN NOT NULL,
    sede_id INT,
    rol_id INT,
    PRIMARY KEY (id),
    FOREIGN KEY (sede_id) REFERENCES sedes_pc(id),
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

-- Insertamos las áreas si no existen en el sistema
INSERT IGNORE INTO areas_pc (id, sede_id, rol_id, nombre, descripcion, delete_flag) VALUES
    (1, 1, 2, 'Consultorios', 'Zona de consultorios', 0),
    (2, 1, 3, 'Talento Humano', 'Zona empleados', 0),
    (3, 2, 3, 'Gerencia', 'Zona Jefe', 0),
    (4, 1, 4, 'Sistemas', 'Zona de Sistemas de la sede principal', 0),
    (5, 2, 4, 'Sistemas SC', 'Zona de Sistemas de la sede Colsag', 0),
    (6, 3, 4, 'Sistemas SCE', 'Zona de Sistemas del Centro de Especialistas', 0);

-- Creamos la tabla ubicaciones si no existe
CREATE TABLE IF NOT EXISTS ubicaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(190) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    esta_ocupada BOOLEAN NOT NULL,
    area_id INT,
    delete_flag BOOLEAN NOT NULL DEFAULT 0,
    CONSTRAINT fk_area FOREIGN KEY (area_id) REFERENCES areas_pc(id)
);


-- Insertamos las ubicaciones por defecto si no existen en el sistema
INSERT IGNORE INTO ubicaciones (area_id, id, nombre, descripcion, esta_ocupada, delete_flag) VALUES
    (1, 1, 'Consultorio 101', 'Consultorio', false, 0),
    (2, 2, 'Tesoreria', 'Finanzas csa', false, 0),
    (2, 3, 'Seguridad y Salud en el Trabajo', 'SGST', false, 0),
    (4, 4, 'Bodega de Sistemas', 'Bodega de Sistemas de la sede principal', false, 0),
    (5, 5, 'Bodega de Sistemas SC', 'Bodega de Sistemas de la sede Colsag', false, 0),
    (6, 6, 'Bodega de Sistemas SCE', 'Bodega de Sistemas del Centro de Especialistas', false, 0);


-- Creamos la tabla tipo_documento si no existe
CREATE TABLE IF NOT EXISTS tipo_documento (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(190) NOT NULL,
    abreviatura VARCHAR(10) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

-- Insertamos tipos de documento si no existen en el sistema
INSERT IGNORE INTO tipo_documento (id, nombre, abreviatura) VALUES
    (1, 'Cedula de Ciudadania', 'CC'),
    (2, 'Numero de identificacion Tributaria', 'NIT'),
    (3, 'Pasaporte', 'PA'),
    (4, 'Tarjeta de Identidad', 'TI'),
    (5, 'Menor sin identificacion', 'MS'),
    (6, 'Adulto sin identificacion', 'AS'),
    (7, 'Numero de Identificacion Personal', 'NIP'),
    (8, 'Cedula de Extranjeria', 'CE'),
    (9, 'Carnet Diplomatico', 'CD'),
    (10, 'Salvo Conducto', 'SC'),
    (11, 'Permiso Especial de Permanencia', 'PE'),
    (12, 'Permiso por Proteccion Temporal', 'PT'),
    (13, 'Documento Extranjero', 'DE');


-- Creamos la tabla usuarios si no existe
CREATE TABLE IF NOT EXISTS usuarios (
    id INT NOT NULL AUTO_INCREMENT,
    rol_id INT,
    primer_nombre VARCHAR(100) NOT NULL,
    segundo_nombre VARCHAR(100),
    primer_apellido VARCHAR(100) NOT NULL,
    segundo_apellido VARCHAR(100),
    tipo_documento_id INT,
    cedula VARCHAR(20) NOT NULL UNIQUE,
    correo VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(10),
    fecha_nacimiento DATE,
    ubicacion_id INT,
    delete_flag BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_rol FOREIGN KEY (rol_id) REFERENCES roles(id),
    CONSTRAINT fk_tipo_documento FOREIGN KEY (tipo_documento_id) REFERENCES tipo_documento(id),
    CONSTRAINT fk_ubicacion FOREIGN KEY (ubicacion_id) REFERENCES ubicaciones(id)
);

-- Insertamos un usuario ADMIN si no existe
-- Insertamos un usuario ADMIN si no existe
INSERT IGNORE INTO usuarios (
    rol_id, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, tipo_documento_id, cedula, correo, password, telefono, fecha_nacimiento, ubicacion_id, delete_flag
) VALUES (
    1, -- rol_id para ADMIN
    'Admin', -- primer_nombre
    'Super', -- segundo_nombre
    'Sistemas', -- primer_apellido
    'CSA', -- segundo_apellido
    1, -- tipo_documento_id, 1 representa 'Cédula de Ciudadanía'
    '1000000000', -- cédula
    'sistemas@clinicasantanasa.com', -- correo
    '$2a$10$O/RsohdsFehWq1UsTsuD.OCJbc/QIAy407wvaEPlBYILegt5kZ.JS', -- password encriptada (P@ssword123)
    '3001234567', -- teléfono (debe comenzar con '3' y tener 10 dígitos)
    '1995-01-01', -- fecha_nacimiento (ejemplo)
    4, -- ubicacion_id, puedes ajustar según el ID de una ubicación válida
    0 -- delete_flag
);


-- Creamos la tabla tipo_software si no existe
CREATE TABLE IF NOT EXISTS tipo_software
(
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(190) NOT NULL,
    delete_flag BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

-- Insertamos los tipos de software si no existen en el sistema
INSERT IGNORE INTO tipo_software (id, nombre, delete_flag) VALUES
    (1, "Software Libre", 0),
    (2, "Software Propietario", 0),
    (3, "Software Comercial", 0);


-- Creamos la tabla tipo_pc si no existe
CREATE TABLE IF NOT EXISTS tipo_pc
(
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(190) NOT NULL,
    delete_flag BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

-- Insertamos los tipos de computador si no existen en el sistema
INSERT IGNORE INTO tipo_pc (id, nombre, delete_flag) VALUES
    (1, "Torre", 0),
    (2, "Portatil", 0),
    (3, "All-in-One", 0),
    (4, "Mini PC", 0),
    (5, "Servidor", 0);

-- Creamos la tabla tipo_componentes si no existe
CREATE TABLE IF NOT EXISTS tipo_componentes
(
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(190) NOT NULL,
    delete_flag BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

-- Insertamos los tipos de componentes si no existen en el sistema
INSERT IGNORE INTO tipo_componentes (id, nombre, delete_flag) VALUES
    (1, "Procesador", 0),
    (2, "Memoria RAM", 0),
    (3, "Almacenamiento", 0);

    -- Creamos la tabla estado_dispositivos si no existe
CREATE TABLE IF NOT EXISTS estado_dispositivos
(
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(190) NOT NULL,
    delete_flag BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

-- Insertamos los estados de dispositivo si no existen en el sistema
INSERT IGNORE INTO estado_dispositivos (id, nombre, delete_flag) VALUES
    (1, "En uso", 0),
    (2, "En reparacion", 0),
    (3, "Averiado", 0),
    (4, "Disponible", 0),
    (5, "Baja", 0);

-- Creamos la tabla tipo_dispositivos si no existe
CREATE TABLE IF NOT EXISTS tipo_dispositivos
(
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(190) NOT NULL,
    delete_flag BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

-- Insertamos los tipos de dispositivos si no existen en el sistema
INSERT IGNORE INTO tipo_dispositivos (id, nombre, delete_flag) VALUES
    (1, "Teclado", 0),
    (2, "Raton", 0),
    (3, "Monitor", 0),
    (4, "Impresora", 0),
    (5, "Audifonos", 0),
    (6, "Unidad de fuente de alimentacion (PSU)", 0),
    (7, "Parlantes", 0),
    (8, "Torre", 0);

-- Creamos la tabla tipo_almacenamiento_ram si no existe
CREATE TABLE IF NOT EXISTS tipo_almacenamiento_ram
(
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(190) NOT NULL,
    delete_flag BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);



-- Creamos la tabla tipo_solicitudes si no existe
CREATE TABLE IF NOT EXISTS tipo_solicitudes
(
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(190) NOT NULL,
    delete_flag BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

-- Insertamos los tipos de solicitudes si no existen en el sistema
INSERT IGNORE INTO tipo_solicitudes (id, nombre, delete_flag) VALUES
    (1, 'Mantenimiento correctivo', 0),
    (2, 'Cambio de ubicacion', 0),
    (3, 'Mantenimiento preventivo', 0),
    (4, 'General', 0);

-- Creamos la tabla estado_solicitudes si no existe
CREATE TABLE IF NOT EXISTS estado_solicitudes
(
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(190) NOT NULL,
    delete_flag BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

-- Insertamos los estados de solicitudes si no existen en el sistema
INSERT IGNORE INTO estado_solicitudes (id, nombre, delete_flag) VALUES
    (1, 'Pendiente', 0),
    (2, 'En Proceso', 0),
    (3, 'Completada', 0),
    (4, 'Cancelada', 0),
    (5, 'Rechazada', 0),
    (6, 'En Revision', 0);

-- Crear la tabla estado_tickets si no existe
CREATE TABLE IF NOT EXISTS estado_tickets (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(190) NOT NULL,
    delete_flag BOOLEAN NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

INSERT IGNORE INTO estado_tickets (id, nombre, delete_flag) VALUES
    (1, 'En Proceso', 0),
    (2, 'Finalizado', 0),
    (3, 'Cancelado', 0),
    (4, 'Reasignado', 0);

-- Creamos la tabla marcas si no existe
CREATE TABLE IF NOT EXISTS marcas (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(190) NOT NULL,
    delete_flag BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

-- Insertamos las marcas si no existen en el sistema
INSERT IGNORE INTO marcas (id, nombre, delete_flag) VALUES
    (1, 'Hewlett-Packard', 0),
    (2, 'LG', 0),
    (3, 'Lenovo', 0),
    (4, 'Dell', 0),
    (5, 'Asus', 0),
    (6, 'Apple', 0),
    (7, 'Samsung', 0),
    (8, 'Toshiba', 0);

-- Creamos la tabla tipo_almacenamiento si no existe
CREATE TABLE IF NOT EXISTS tipo_almacenamiento (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(190) NOT NULL,
    delete_flag BOOLEAN NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

-- Insertamos tipos de almacenamiento si no existen en el sistema
INSERT IGNORE INTO tipo_almacenamiento (id, nombre, delete_flag) VALUES
    (1, 'SSD', 0),
    (2, 'HDD', 0),
    (3, 'NVMe', 0),
    (4, 'SATA', 0);

-- Creamos la tabla tipo_ram si no existe
CREATE TABLE IF NOT EXISTS tipo_ram (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(190) NOT NULL,
    delete_flag BOOLEAN NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

-- Insertamos tipos de RAM si no existen en el sistema
INSERT IGNORE INTO tipo_ram (id, nombre, delete_flag) VALUES
    (1, 'DDR3', 0),
    (2, 'DDR4', 0),
    (3, 'DDR5', 0),
    (4, 'LPDDR4', 0);

CREATE TABLE IF NOT EXISTS propietario (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(190) NOT NULL,
    delete_flag BOOLEAN NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

-- Insertamos propietarios si no existen en el sistema
INSERT IGNORE INTO propietario (id, nombre, delete_flag) VALUES
    (1, 'OFICANON', 0),
    (2, 'CSA', 0);


