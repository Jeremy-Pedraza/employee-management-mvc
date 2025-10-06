package com.mvc.demo.employee_management_mvc.service;

import java.util.List;
import java.util.Optional;

import com.mvc.demo.employee_management_mvc.dto.EmployeeDto;

/**
 * Interface del servicio de empleados.
 * 
 * Define el contrato para las operaciones de negocio relacionadas con empleados. Esta interfaz
 * abstrae la implementación específica, permitiendo múltiples implementaciones y facilitando el
 * testing con mocks.
 * 
 * Principios SOLID aplicados: - Single Responsibility: Define solo operaciones de negocio de
 * empleados - Interface Segregation: Métodos cohesivos y específicos - Dependency Inversion: Los
 * controladores dependen de esta abstracción - Open/Closed: Abierta para extensión (nuevas
 * implementaciones) - Liskov Substitution: Cualquier implementación es intercambiable
 * 
 * Características de Programación Funcional: - Retorna Optional para manejar ausencia de valores -
 * Retorna listas inmutables - Métodos declarativos (qué hacer, no cómo)
 * 
 * @author Employee Management Team
 * @version 1.0.0
 */
public interface EmployeeService {

    // ========================================================================
    // OPERACIONES CRUD BÁSICAS
    // ========================================================================

    /**
     * Obtiene todos los empleados del sistema.
     * 
     * @return Lista de todos los empleados (puede estar vacía, nunca null)
     */
    List<EmployeeDto> getAllEmployees();

    /**
     * Busca un empleado por su ID.
     * 
     * Retorna Optional para manejar de forma funcional la posible ausencia.
     * 
     * @param id ID del empleado a buscar
     * @return Optional con el empleado si existe, vacío si no
     * @throws IllegalArgumentException si el ID es null o negativo
     */
    Optional<EmployeeDto> getEmployeeById(Long id);

    /**
     * Crea o actualiza un empleado.
     * 
     * Si el DTO tiene ID, se actualiza el empleado existente. Si no tiene ID, se crea un nuevo
     * empleado.
     * 
     * @param employeeDto DTO con los datos del empleado
     * @return DTO del empleado guardado (con ID asignado si es nuevo)
     * @throws IllegalArgumentException si el DTO es null o inválido
     * @throws com.mvc.demo.exception.EmployeeNotFoundException si se intenta actualizar un empleado
     *         inexistente
     */
    EmployeeDto createOrUpdateEmployee(EmployeeDto employeeDto);

    /**
     * Elimina un empleado por su ID.
     * 
     * @param id ID del empleado a eliminar
     * @throws IllegalArgumentException si el ID es null
     * @throws com.mvc.demo.exception.EmployeeNotFoundException si el empleado no existe
     */
    void deleteEmployeeById(Long id);

    /**
     * Verifica si existe un empleado con el ID especificado.
     * 
     * @param id ID del empleado
     * @return true si existe, false si no
     */
    boolean existsById(Long id);

    // ========================================================================
    // OPERACIONES DE BÚSQUEDA
    // ========================================================================

    /**
     * Busca un empleado por su correo electrónico.
     * 
     * @param email Email del empleado
     * @return Optional con el empleado si existe
     */
    Optional<EmployeeDto> getEmployeeByEmail(String email);

    /**
     * Busca empleados por nombre.
     * 
     * @param firstName Nombre a buscar
     * @return Lista de empleados con ese nombre
     */
    List<EmployeeDto> getEmployeesByFirstName(String firstName);

    /**
     * Busca empleados por apellido.
     * 
     * @param lastName Apellido a buscar
     * @return Lista de empleados con ese apellido
     */
    List<EmployeeDto> getEmployeesByLastName(String lastName);

    /**
     * Realiza una búsqueda global en todos los campos.
     * 
     * Busca en nombre, apellido y email simultáneamente.
     * 
     * @param searchTerm Término de búsqueda
     * @return Lista de empleados que coinciden
     */
    List<EmployeeDto> searchEmployees(String searchTerm);

    // ========================================================================
    // OPERACIONES FUNCIONALES AVANZADAS
    // ========================================================================

    /**
     * Obtiene todos los empleados ordenados por nombre completo.
     * 
     * @return Lista ordenada de empleados
     */
    List<EmployeeDto> getAllEmployeesSortedByName();

    /**
     * Obtiene empleados que tienen email registrado.
     * 
     * @return Lista de empleados con email
     */
    List<EmployeeDto> getEmployeesWithEmail();

    /**
     * Obtiene empleados sin email registrado.
     * 
     * @return Lista de empleados sin email
     */
    List<EmployeeDto> getEmployeesWithoutEmail();

    /**
     * Cuenta el total de empleados en el sistema.
     * 
     * @return Cantidad total de empleados
     */
    long countAllEmployees();

    /**
     * Cuenta empleados con email registrado.
     * 
     * @return Cantidad de empleados con email
     */
    long countEmployeesWithEmail();

    /**
     * Obtiene las iniciales de todos los empleados.
     * 
     * Ejemplo de método funcional que transforma datos.
     * 
     * @return Lista de iniciales (ej: ["JD", "MG", "CR"])
     */
    List<String> getAllEmployeeInitials();

    /**
     * Obtiene los nombres completos de todos los empleados.
     * 
     * @return Lista de nombres completos
     */
    List<String> getAllEmployeeFullNames();

    // ========================================================================
    // VALIDACIONES
    // ========================================================================

    /**
     * Verifica si un email ya está registrado.
     * 
     * Útil para validaciones antes de crear un empleado.
     * 
     * @param email Email a verificar
     * @return true si el email ya existe
     */
    boolean emailExists(String email);

    /**
     * Verifica si un email ya está registrado por otro empleado.
     * 
     * Útil para validaciones al actualizar un empleado.
     * 
     * @param email Email a verificar
     * @param excludeId ID del empleado a excluir de la verificación
     * @return true si otro empleado tiene ese email
     */
    boolean emailExistsForOtherEmployee(String email, Long excludeId);

    /**
     * Valida si un DTO de empleado tiene datos válidos.
     * 
     * @param employeeDto DTO a validar
     * @return true si es válido
     */
    boolean isValidEmployee(EmployeeDto employeeDto);
}
