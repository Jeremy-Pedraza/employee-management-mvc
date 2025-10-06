package com.mvc.demo.employee_management_mvc.exception;

/**
 * Excepción personalizada para cuando un empleado no es encontrado.
 * 
 * Esta excepción se lanza cuando se intenta buscar un empleado por ID y no existe en la base de
 * datos.
 * 
 * Principios SOLID aplicados: - Single Responsibility: Solo representa el error de empleado no
 * encontrado - Open/Closed: Puede ser extendida para casos específicos
 * 
 * Características: - Extiende RuntimeException (unchecked exception) - Proporciona múltiples
 * constructores para flexibilidad - Incluye información contextual del error
 * 
 * @author Employee Management Team
 * @version 1.0.0
 */
public class EmployeeNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * ID del empleado que no fue encontrado.
     */
    private final Long employeeId;

    /**
     * Constructor con mensaje personalizado.
     * 
     * @param message Mensaje descriptivo del error
     */
    public EmployeeNotFoundException(String message) {
        super(message);
        this.employeeId = null;
    }

    /**
     * Constructor con ID del empleado. Genera un mensaje estándar automáticamente.
     * 
     * @param employeeId ID del empleado no encontrado
     */
    public EmployeeNotFoundException(Long employeeId) {
        super(String.format("Empleado con ID %d no fue encontrado", employeeId));
        this.employeeId = employeeId;
    }

    /**
     * Constructor con mensaje y causa.
     * 
     * @param message Mensaje descriptivo del error
     * @param cause Causa raíz del error
     */
    public EmployeeNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.employeeId = null;
    }

    /**
     * Constructor con ID, mensaje y causa.
     * 
     * @param employeeId ID del empleado no encontrado
     * @param message Mensaje descriptivo
     * @param cause Causa raíz del error
     */
    public EmployeeNotFoundException(Long employeeId, String message, Throwable cause) {
        super(message, cause);
        this.employeeId = employeeId;
    }

    /**
     * Obtiene el ID del empleado no encontrado.
     * 
     * @return ID del empleado, o null si no se especificó
     */
    public Long getEmployeeId() {
        return employeeId;
    }

    /**
     * Verifica si se especificó un ID de empleado.
     * 
     * @return true si hay un ID asociado a la excepción
     */
    public boolean hasEmployeeId() {
        return employeeId != null;
    }
}
