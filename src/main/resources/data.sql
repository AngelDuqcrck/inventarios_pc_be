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
    PRIMARY KEY (id),
    UNIQUE KEY (nombre)
    );

-- Insertamos los roles si estos no existen en el sistema 
INSERT
IGNORE INTO roles (id,nombre) VALUES
    (1,"ADMIN"),
    (2,"EMPLEADO_ASISTENCIAL"),
    (3,"EMPLEADO_ADMINISTRATIVO"),
    (4,"TECNICO_SISTEMAS");

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
    (2, "Tarjeta de Identidad"),
    (3, "Cedula de Extranjeria"),
    (4, "Pasaporte");
