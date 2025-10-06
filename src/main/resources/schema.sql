-- ===================================================================
-- ESQUEMA DE BASE DE DATOS - EMPLOYEE MANAGEMENT SYSTEM
-- Base de Datos: H2 (En memoria)
-- Versión: 1.0.0
-- ===================================================================

-- Eliminar tabla si existe (para desarrollo)
DROP TABLE IF EXISTS TBL_EMPLOYEES;

-- ===================================================================
-- TABLA: TBL_EMPLOYEES
-- Descripción: Almacena la información de los empleados
-- ===================================================================
CREATE TABLE TBL_EMPLOYEES (
    -- Identificador único del empleado (Clave Primaria)
    id INT AUTO_INCREMENT PRIMARY KEY,
    
    -- Nombre del empleado
    -- VARCHAR(250): Permite hasta 250 caracteres
    -- NOT NULL: Campo obligatorio
    first_name VARCHAR(250) NOT NULL,
    
    -- Apellido del empleado
    -- VARCHAR(250): Permite hasta 250 caracteres
    -- NOT NULL: Campo obligatorio
    last_name VARCHAR(250) NOT NULL,
    
    -- Correo electrónico del empleado
    -- VARCHAR(200): Permite hasta 200 caracteres
    -- DEFAULT NULL: Puede ser nulo (opcional)
    -- UNIQUE: No puede haber correos duplicados
    email VARCHAR(200) DEFAULT NULL UNIQUE,
    
    -- Auditoría: Fecha de creación del registro
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Auditoría: Fecha de última actualización del registro
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ===================================================================
-- ÍNDICES PARA OPTIMIZACIÓN DE CONSULTAS
-- ===================================================================

-- Índice para búsqueda por nombre completo
CREATE INDEX idx_employee_name ON TBL_EMPLOYEES(first_name, last_name);

-- Índice para búsqueda por email (ya es único, pero esto optimiza las búsquedas)
CREATE INDEX idx_employee_email ON TBL_EMPLOYEES(email);

-- ===================================================================
-- COMENTARIOS EN LA TABLA Y COLUMNAS (Para documentación)
-- ===================================================================

COMMENT ON TABLE TBL_EMPLOYEES IS 'Tabla principal que almacena información de empleados';
COMMENT ON COLUMN TBL_EMPLOYEES.id IS 'Identificador único del empleado';
COMMENT ON COLUMN TBL_EMPLOYEES.first_name IS 'Primer nombre del empleado';
COMMENT ON COLUMN TBL_EMPLOYEES.last_name IS 'Apellido del empleado';
COMMENT ON COLUMN TBL_EMPLOYEES.email IS 'Correo electrónico corporativo (único)';
COMMENT ON COLUMN TBL_EMPLOYEES.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN TBL_EMPLOYEES.updated_at IS 'Fecha y hora de última actualización';