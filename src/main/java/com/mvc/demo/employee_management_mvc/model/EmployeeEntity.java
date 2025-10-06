package com.mvc.demo.employee_management_mvc.model;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidad JPA que representa un Empleado en el sistema.
 * 
 * Esta clase mapea la tabla TBL_EMPLOYEES en la base de datos.
 * 
 * Principios SOLID aplicados: - Single Responsibility: Solo representa la estructura de datos de un
 * empleado - Open/Closed: Abierta para extensión (herencia), cerrada para modificación
 * 
 * Características: - Usa Lombok para reducir código boilerplate - Inmutable parcialmente (los
 * setters son controlados) - Incluye campos de auditoría (created_at, updated_at) - Validaciones en
 * la capa de persistencia
 * 
 * @author Employee Management Team
 * @version 1.0.0
 */
@Entity
@Table(name = "TBL_EMPLOYEES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"createdAt", "updatedAt"})
public class EmployeeEntity {

    /**
     * Identificador único del empleado. Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /**
     * Primer nombre del empleado. Campo obligatorio con máximo 250 caracteres.
     */
    @Column(name = "first_name", nullable = false, length = 250)
    private String firstName;

    /**
     * Apellido del empleado. Campo obligatorio con máximo 250 caracteres.
     */
    @Column(name = "last_name", nullable = false, length = 250)
    private String lastName;

    /**
     * Correo electrónico corporativo del empleado. Campo opcional pero único (no puede haber
     * duplicados).
     */
    @Column(name = "email", unique = true, length = 200)
    private String email;

    /**
     * Fecha y hora de creación del registro. Se establece automáticamente al crear el registro.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de última actualización del registro. Se actualiza automáticamente en cada
     * modificación.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ========================================================================
    // MÉTODOS DE NEGOCIO (Business Logic Methods)
    // ========================================================================

    /**
     * Obtiene el nombre completo del empleado. Ejemplo de método que combina POO con programación
     * funcional.
     * 
     * @return Nombre completo en formato "Nombre Apellido"
     */
    public String getFullName() {
        return String.format("%s %s", firstName != null ? firstName : "",
                lastName != null ? lastName : "").trim();
    }

    /**
     * Verifica si el empleado tiene un email válido. Método funcional puro (sin efectos
     * secundarios).
     * 
     * @return true si tiene email, false si es null o vacío
     */
    public boolean hasEmail() {
        return email != null && !email.trim().isEmpty();
    }

    /**
     * Obtiene las iniciales del empleado. Ejemplo de programación funcional con Optional.
     * 
     * @return Iniciales en formato "FL" o cadena vacía si no hay datos
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

    // ========================================================================
    // EQUALS Y HASHCODE (Importante para colecciones y JPA)
    // ========================================================================

    /**
     * Compara dos entidades de empleados por su ID. Implementación correcta para entidades JPA.
     * 
     * @param o Objeto a comparar
     * @return true si son el mismo empleado (mismo ID)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        EmployeeEntity that = (EmployeeEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    /**
     * Genera un código hash basado en el ID. Consistente con equals() para uso en colecciones.
     * 
     * @return Código hash del empleado
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // ========================================================================
    // CALLBACKS DE JPA (Lifecycle Callbacks)
    // ========================================================================

    /**
     * Callback ejecutado antes de persistir la entidad. Útil para validaciones o establecer valores
     * por defecto.
     */
    @PrePersist
    protected void prePersist() {
        // Normalizar el email a minúsculas si existe
        if (email != null) {
            email = email.toLowerCase().trim();
        }

        // Capitalizar primera letra de nombres si no están vacíos
        if (firstName != null && !firstName.isEmpty()) {
            firstName = capitalizeFirstLetter(firstName.trim());
        }

        if (lastName != null && !lastName.isEmpty()) {
            lastName = capitalizeFirstLetter(lastName.trim());
        }
    }

    /**
     * Callback ejecutado antes de actualizar la entidad.
     */
    @PreUpdate
    protected void preUpdate() {
        prePersist(); // Aplicar las mismas validaciones
    }

    // ========================================================================
    // MÉTODOS AUXILIARES PRIVADOS (Helper Methods)
    // ========================================================================

    /**
     * Capitaliza la primera letra de una cadena. Método auxiliar privado (Single Responsibility).
     * 
     * @param text Texto a capitalizar
     * @return Texto con primera letra en mayúscula
     */
    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}
