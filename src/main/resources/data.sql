-- ===================================================================
-- DATOS INICIALES - EMPLOYEE MANAGEMENT SYSTEM
-- Descripción: Datos de prueba para el sistema de empleados
-- ===================================================================

-- Limpiar datos existentes (opcional, útil en desarrollo)
DELETE FROM TBL_EMPLOYEES;

-- Reiniciar el contador de auto-incremento
ALTER TABLE TBL_EMPLOYEES ALTER COLUMN id RESTART WITH 1;

-- ===================================================================
-- INSERCIÓN DE EMPLEADOS DE PRUEBA
-- ===================================================================

-- Empleado 1: Lokesh Gupta
INSERT INTO TBL_EMPLOYEES (first_name, last_name, email) 
VALUES ('Lokesh', 'Gupta', 'howtodoinjava@gmail.com');

-- Empleado 2: John Doe
INSERT INTO TBL_EMPLOYEES (first_name, last_name, email) 
VALUES ('John', 'Doe', 'xyz@email.com');

-- Empleado 3: María García
INSERT INTO TBL_EMPLOYEES (first_name, last_name, email) 
VALUES ('María', 'García', 'maria.garcia@company.com');

-- Empleado 4: Carlos Rodríguez
INSERT INTO TBL_EMPLOYEES (first_name, last_name, email) 
VALUES ('Carlos', 'Rodríguez', 'carlos.rodriguez@company.com');

-- Empleado 5: Ana Martínez
INSERT INTO TBL_EMPLOYEES (first_name, last_name, email) 
VALUES ('Ana', 'Martínez', 'ana.martinez@company.com');

-- Empleado 6: Pedro López (sin email - para probar campos opcionales)
INSERT INTO TBL_EMPLOYEES (first_name, last_name, email) 
VALUES ('Pedro', 'López', NULL);

-- ===================================================================
-- VERIFICACIÓN DE DATOS INSERTADOS
-- ===================================================================

-- Esta consulta muestra todos los empleados insertados
-- Se puede usar para verificar en la consola H2
-- SELECT * FROM TBL_EMPLOYEES ORDER BY id;