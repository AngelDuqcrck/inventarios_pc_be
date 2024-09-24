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