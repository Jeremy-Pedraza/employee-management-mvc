package com.mvc.demo.employee_management_mvc.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) para Empleado.
 * 
 * Esta clase transporta datos entre capas sin exponer la entidad directamente.
 * 
 * Principios SOLID aplicados: - Single Responsibility: Solo transporta datos de empleado -
 * Interface Segregation: No hereda métodos innecesarios - Dependency Inversion: No depende de
 * implementaciones concretas
 * 
 * Características de Programación Funcional: - Inmutabilidad parcial (usar toBuilder() para copias)
 * - Métodos puros sin efectos secundarios - Thread-safe en operaciones de lectura - Ideal para
 * operaciones con Streams
 * 
 * Anotaciones Lombok: - @Data: Genera getters, setters, equals, hashCode y toString - @Builder:
 * Patrón Builder para construcción fluida - @NoArgsConstructor: Constructor sin argumentos
 * (requerido por Thymeleaf) - @AllArgsConstructor: Constructor con todos los campos (usado por
 * Builder)
 * 
 * @author Employee Management Team
 * @version 1.0.0
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del empleado. Puede ser null para empleados nuevos.
     */
    private Long id;

    /**
     * Primer nombre del empleado. Validaciones: - No puede estar en blanco - Debe tener entre 2 y
     * 250 caracteres
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 250, message = "El nombre debe tener entre 2 y 250 caracteres")
    private String firstName;

    /**
     * Apellido del empleado. Validaciones: - No puede estar en blanco - Debe tener entre 2 y 250
     * caracteres
     */
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 250, message = "El apellido debe tener entre 2 y 250 caracteres")
    private String lastName;

    /**
     * Correo electrónico corporativo. Validaciones: - Debe ser un email válido (si se proporciona)
     * - Máximo 200 caracteres
     */
    @Email(message = "El email debe ser válido")
    @Size(max = 200, message = "El email no puede exceder 200 caracteres")
    private String email;

    /**
     * Fecha de creación del registro. Solo para lectura (no se envía en formularios).
     */
    private LocalDateTime createdAt;

    /**
     * Fecha de última actualización. Solo para lectura (no se envía en formularios).
     */
    private LocalDateTime updatedAt;

    // ========================================================================
    // MÉTODOS DE NEGOCIO (Business Logic Methods)
    // Estos son métodos funcionales puros sin efectos secundarios
    // ========================================================================

    /**
     * Obtiene el nombre completo del empleado. Método funcional puro.
     * 
     * @return Nombre completo en formato "Nombre Apellido"
     */
    public String getFullName() {
        return String.format("%s %s", firstName != null ? firstName : "",
                lastName != null ? lastName : "").trim();
    }

    /**
     * Verifica si el empleado tiene email. Función pura sin efectos secundarios.
     * 
     * @return true si tiene email válido
     */
    public boolean hasEmail() {
        return email != null && !email.trim().isEmpty();
    }

    /**
     * Obtiene las iniciales del empleado. Ejemplo de programación funcional.
     * 
     * @return Iniciales en formato "FL"
     */
    public String getInitials() {
        String firstInitial = firstName != null && !firstName.isEmpty()
                ? String.valueOf(firstName.charAt(0)).toUpperCase()
                : "";
        String lastInitial = lastName != null && !lastName.isEmpty()
                ? String.valueOf(lastName.charAt(0)).toUpperCase()
                : "";
        return firstInitial + lastInitial;
    }

    /**
     * Verifica si es un empleado nuevo (sin ID). Función pura para lógica de negocio.
     * 
     * @return true si el ID es null (empleado nuevo)
     */
    public boolean isNew() {
        return id == null;
    }

    /**
     * Crea una copia de este DTO con un nuevo nombre. Ejemplo de inmutabilidad: no modifica el
     * objeto actual, crea uno nuevo.
     * 
     * @param newFirstName Nuevo nombre
     * @return Nuevo DTO con el nombre actualizado
     */
    public EmployeeDto withFirstName(String newFirstName) {
        return toBuilder().firstName(newFirstName).build();
    }

    /**
     * Crea una copia de este DTO con un nuevo apellido.
     * 
     * @param newLastName Nuevo apellido
     * @return Nuevo DTO con el apellido actualizado
     */
    public EmployeeDto withLastName(String newLastName) {
        return toBuilder().lastName(newLastName).build();
    }

    /**
     * Crea una copia de este DTO con un nuevo email.
     * 
     * @param newEmail Nuevo email
     * @return Nuevo DTO con el email actualizado
     */
    public EmployeeDto withEmail(String newEmail) {
        return toBuilder().email(newEmail).build();
    }

    /**
     * Obtiene una representación en formato display para la UI.
     * 
     * @return String formateado para mostrar en interfaz
     */
    public String toDisplayString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        if (hasEmail()) {
            sb.append(" (").append(email).append(")");
        }
        return sb.toString();
    }
}
