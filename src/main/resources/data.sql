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
CREATE TABLE IF NOT EXISTS sedes_PC
(
    id INT NOT NULL AUTO_INCREMENT,
    direccion VARCHAR(255) NOT NULL,
    descripcion TEXT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    delete_flag BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

-- Insertamos las sedes si no existen en el sistema
INSERT IGNORE INTO sedes_PC (id, direccion, descripcion, nombre, delete_flag) VALUES
    (1, 'Av. 11E# 8-41COLSAG', 'Principalasddddddddddddddddddddd', 'Sede Principal', 0),
    (2, 'Cl. 8 # 11E-62 COLSAG', 'COLSAAAAAAAAAAAAAAAAAAAAAAAAAAG', 'Sede Colsag', 0),
    (3, 'COLSAG', 'ESPECIALIStassssssssssssssssss', 'Centro de Especialistas', 0);

-- Creamos la tabla areas_PC si no existe
CREATE TABLE IF NOT EXISTS areas_PC
(
    id INT NOT NULL AUTO_INCREMENT,
    sede_id INT,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT NOT NULL,
    delete_flag BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre),
    CONSTRAINT fk_sede FOREIGN KEY (sede_id) REFERENCES sedes_PC(id) ON DELETE SET NULL
);

-- Insertamos las áreas si no existen en el sistema
INSERT IGNORE INTO areas_PC (id, sede_id, nombre, descripcion, delete_flag) VALUES
    (1, 1, 'Urgencias', 'Zona urgente', 0),
    (2, 1, 'Talento Humano', 'Zona empleados', 0),
    (3, 2, 'Gerencia', 'Zona Jefe', 0);

-- Creamos la tabla ubicaciones si no existe
CREATE TABLE IF NOT EXISTS ubicaciones
(
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    area_id INT,
    descripcion TEXT NOT NULL,
    delete_flag BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_area FOREIGN KEY (area_id) REFERENCES areas_PC(id) ON DELETE SET NULL
);

-- Insertamos las ubicaciones si no existen en el sistema
INSERT IGNORE INTO ubicaciones (area_id, id, nombre, descripcion, delete_flag) VALUES
    (1,1, 'Consultorio 101', 'Consultorios el primero', 0),
    (2,2, 'Tesoreria', 'Finanzas csa', 0),
    (2,3, 'Seguridad y Salud en el Trabajo', 'SGST', 0);
    

-- Creamos la tabla tipo_documento si no existe
CREATE TABLE IF NOT EXISTS tipo_documento
(
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

-- Insertamos los tipos de documento si no existen en el sistema
INSERT IGNORE INTO tipo_documento (id, nombre) VALUES
    (1, "Cedula de Ciudadania"),
    (2, "Cedula de Extranjeria"),
    (3, "Pasaporte");

-- Creamos la tabla tipo_software si no existe
CREATE TABLE IF NOT EXISTS tipo_software
(
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
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
    nombre VARCHAR(255) NOT NULL,
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
    nombre VARCHAR(255) NOT NULL,
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
    nombre VARCHAR(255) NOT NULL,
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
    nombre VARCHAR(255) NOT NULL,
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
    (7, "Parlantes", 0);

-- Creamos la tabla tipo_almacenamiento_ram si no existe
CREATE TABLE IF NOT EXISTS tipo_almacenamiento_ram
(
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    delete_flag BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
);

-- Insertamos los tipos de almacenamiento y RAM si no existen en el sistema
INSERT IGNORE INTO tipo_almacenamiento_ram (id, nombre, delete_flag) VALUES
    (1, "HDD", 0),
    (2, "SSD", 0),
    (3, "NVMe", 0),
    (4, "RAM DDR3", 0),
    (5, "RAM DDR4", 0),
    (6, "RAM DDR5", 0);

-- Crear la tabla computadores si no existe
CREATE TABLE IF NOT EXISTS computadores (
    id INT NOT NULL AUTO_INCREMENT,
    tipo_pc_id INT NOT NULL,
    responsable_id INT NOT NULL,
    ubicacion_id INT NOT NULL,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    modelo VARCHAR(255),
    serial VARCHAR(255),
    marca_id INT NOT NULL,
    procesador_id INT NOT NULL,
    ram_id INT NOT NULL,
    almacenamiento_id INT NOT NULL,
    tipo_almacenamiento_id INT NOT NULL,
    tipo_ram_id INT NOT NULL,
    placa VARCHAR(255) NOT NULL UNIQUE,
    ip_asignada VARCHAR(15),
    estado_dispositivo_id INT,
    PRIMARY KEY (id),
    CONSTRAINT fk_tipo_pc FOREIGN KEY (tipo_pc_id) REFERENCES tipo_pc(id) ON DELETE SET NULL,
    CONSTRAINT fk_responsable FOREIGN KEY (responsable_id) REFERENCES usuarios(id) ON DELETE SET NULL,
    CONSTRAINT fk_ubicacion FOREIGN KEY (ubicacion_id) REFERENCES ubicaciones(id) ON DELETE SET NULL,
    CONSTRAINT fk_marca FOREIGN KEY (marca_id) REFERENCES marcas(id) ON DELETE SET NULL,
    CONSTRAINT fk_procesador FOREIGN KEY (procesador_id) REFERENCES componentes(id) ON DELETE SET NULL,
    CONSTRAINT fk_ram FOREIGN KEY (ram_id) REFERENCES componentes(id) ON DELETE SET NULL,
    CONSTRAINT fk_almacenamiento FOREIGN KEY (almacenamiento_id) REFERENCES componentes(id) ON DELETE SET NULL,
    CONSTRAINT fk_tipo_almacenamiento FOREIGN KEY (tipo_almacenamiento_id) REFERENCES tipo_almacenamiento_ram(id) ON DELETE SET NULL,
    CONSTRAINT fk_tipo_ram FOREIGN KEY (tipo_ram_id) REFERENCES tipo_almacenamiento_ram(id) ON DELETE SET NULL,
    CONSTRAINT fk_estado_dispositivo FOREIGN KEY (estado_dispositivo_id) REFERENCES estado_dispositivos(id) ON DELETE SET NULL
);

-- Insertar el dato de prueba
INSERT IGNORE INTO computadores (
    id,
    tipo_pc_id,
    responsable_id,
    ubicacion_id,
    nombre,
    modelo,
    serial,
    marca_id,
    procesador_id,
    ram_id,
    almacenamiento_id,
    tipo_almacenamiento_id,
    tipo_ram_id,
    placa,
    ip_asignada,
    estado_dispositivo_id
) VALUES (
    2, -- Id
    1,          -- tipoPC
    1,          -- responsable
    2,          -- ubicacion
    'Tesoreria 002',     -- nombre
    'Dell Inspiron 3501',-- modelo
    'ABC123XYZ',         -- serial
    4,          -- marca
    1,          -- procesador
    3,          -- ram
    4,          -- almacenamiento
    1,          -- tipoAlmacenamiento
    4,          -- tipoRam
    'DELL-002', -- placa
    '192.168.1.15',       -- ipAsignada
    4           --Estado del dispositivo (Disponible)
);
