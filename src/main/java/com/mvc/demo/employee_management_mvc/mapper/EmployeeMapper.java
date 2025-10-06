package com.mvc.demo.employee_management_mvc.mapper;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.mvc.demo.employee_management_mvc.dto.EmployeeDto;
import com.mvc.demo.employee_management_mvc.model.EmployeeEntity;

/**
 * Mapper para convertir entre EmployeeEntity y EmployeeDto.
 * 
 * Esta clase aplica el patrón Mapper para separar la capa de persistencia de la capa de
 * presentación/transferencia de datos.
 * 
 * Principios SOLID aplicados: - Single Responsibility: Solo convierte entre Entity y DTO -
 * Open/Closed: Puede extenderse sin modificar el código existente - Dependency Inversion: Otros
 * componentes dependen de esta abstracción
 * 
 * Características de Programación Funcional: - Métodos puros sin efectos secundarios - Usa
 * Function<T, R> para composición de funciones - Operaciones con Streams para colecciones - Manejo
 * funcional con Optional - Inmutabilidad en las conversiones
 * 
 * @author Employee Management Team
 * @version 1.0.0
 */
@Component
public class EmployeeMapper {

    // ========================================================================
    // FUNCIONES DE MAPEO COMO INTERFACES FUNCIONALES
    // Estas son funciones puras que pueden componerse
    // ========================================================================

    /**
     * Función para convertir Entity a DTO. Esta es una interfaz funcional que puede ser reutilizada
     * y compuesta.
     */
    public final Function<EmployeeEntity, EmployeeDto> entityToDto =
            entity -> EmployeeDto.builder().id(entity.getId()).firstName(entity.getFirstName())
                    .lastName(entity.getLastName()).email(entity.getEmail())
                    .createdAt(entity.getCreatedAt()).updatedAt(entity.getUpdatedAt()).build();

    /**
     * Función para convertir DTO a Entity. Esta es una interfaz funcional para la conversión
     * inversa.
     */
    public final Function<EmployeeDto, EmployeeEntity> dtoToEntity =
            dto -> EmployeeEntity.builder().id(dto.getId()).firstName(dto.getFirstName())
                    .lastName(dto.getLastName()).email(dto.getEmail()).build();

    // ========================================================================
    // MÉTODOS DE CONVERSIÓN INDIVIDUAL
    // ========================================================================

    /**
     * Convierte una entidad a DTO. Método puro sin efectos secundarios.
     * 
     * @param entity Entidad a convertir
     * @return DTO correspondiente, o null si entity es null
     */
    public EmployeeDto toDto(EmployeeEntity entity) {
        if (entity == null) {
            return null;
        }
        return entityToDto.apply(entity);
    }

    /**
     * Convierte un DTO a entidad. Método puro sin efectos secundarios.
     * 
     * @param dto DTO a convertir
     * @return Entidad correspondiente, o null si dto es null
     */
    public EmployeeEntity toEntity(EmployeeDto dto) {
        if (dto == null) {
            return null;
        }
        return dtoToEntity.apply(dto);
    }

    // ========================================================================
    // MÉTODOS CON OPTIONAL (Programación Funcional Avanzada)
    // ========================================================================

    /**
     * Convierte un Optional&lt;Entity&gt; a Optional&lt;DTO&gt;. Ejemplo de composición funcional
     * con Optional.
     * 
     * Este método demuestra cómo trabajar con Optional de forma funcional: - map() aplica la
     * función de conversión si el Optional tiene valor - Retorna Optional.empty() si el Optional de
     * entrada está vacío
     * 
     * @param optionalEntity Optional que puede contener una entidad
     * @return Optional que puede contener un DTO
     */
    public Optional<EmployeeDto> toDto(Optional<EmployeeEntity> optionalEntity) {
        return optionalEntity.map(entityToDto);
    }

    /**
     * Convierte un Optional&lt;DTO&gt; a Optional&lt;Entity&gt;.
     * 
     * @param optionalDto Optional que puede contener un DTO
     * @return Optional que puede contener una entidad
     */
    public Optional<EmployeeEntity> toEntity(Optional<EmployeeDto> optionalDto) {
        return optionalDto.map(dtoToEntity);
    }

    // ========================================================================
    // MÉTODOS PARA COLECCIONES (Programación Funcional con Streams)
    // ========================================================================

    /**
     * Convierte una lista de entidades a lista de DTOs.
     * 
     * Ejemplo de programación funcional con Streams: - stream(): Convierte la lista en un Stream -
     * map(): Aplica la función de conversión a cada elemento - collect(): Recolecta los resultados
     * en una nueva lista
     * 
     * Esta operación es inmutable: no modifica la lista original.
     * 
     * @param entities Lista de entidades
     * @return Lista de DTOs (nueva lista, no modifica la original)
     */
    public List<EmployeeDto> toDtoList(List<EmployeeEntity> entities) {
        if (entities == null) {
            return List.of(); // Retorna lista inmutable vacía
        }

        return entities.stream().map(entityToDto).collect(Collectors.toList());
    }

    /**
     * Convierte una lista de DTOs a lista de entidades.
     * 
     * @param dtos Lista de DTOs
     * @return Lista de entidades (nueva lista)
     */
    public List<EmployeeEntity> toEntityList(List<EmployeeDto> dtos) {
        if (dtos == null) {
            return List.of(); // Retorna lista inmutable vacía
        }

        return dtos.stream().map(dtoToEntity).collect(Collectors.toList());
    }

    // ========================================================================
    // MÉTODOS DE ACTUALIZACIÓN (Merge)
    // ========================================================================

    /**
     * Actualiza una entidad existente con datos de un DTO.
     * 
     * Este método NO crea una nueva entidad, sino que actualiza la existente. Es útil para
     * operaciones de actualización donde queremos mantener el mismo objeto JPA (para que Hibernate
     * detecte los cambios).
     * 
     * Principio: Modifica el objeto existente pero de forma controlada.
     * 
     * @param entity Entidad existente a actualizar
     * @param dto DTO con los nuevos datos
     * @return La misma entidad actualizada (para permitir method chaining)
     */
    public EmployeeEntity updateEntityFromDto(EmployeeEntity entity, EmployeeDto dto) {
        if (entity == null || dto == null) {
            return entity;
        }

        // Actualizar solo los campos que vienen del DTO
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        // NO actualizamos id, createdAt ni updatedAt
        // updatedAt se actualiza automáticamente con @UpdateTimestamp

        return entity;
    }

    /**
     * Crea una nueva entidad con los datos del DTO preservando el ID.
     * 
     * Útil cuando queremos crear una nueva instancia pero mantener la referencia al ID existente.
     * 
     * @param dto DTO con los datos
     * @param existingId ID a preservar
     * @return Nueva entidad con el ID especificado
     */
    public EmployeeEntity toEntityWithId(EmployeeDto dto, Long existingId) {
        if (dto == null) {
            return null;
        }

        return EmployeeEntity.builder().id(existingId) // Preservar el ID existente
                .firstName(dto.getFirstName()).lastName(dto.getLastName()).email(dto.getEmail())
                .build();
    }

    // ========================================================================
    // MÉTODOS FUNCIONALES AVANZADOS
    // ========================================================================

    /**
     * Convierte y filtra empleados que tienen email.
     * 
     * Ejemplo de composición de operaciones funcionales: - Filtra solo los que tienen email -
     * Convierte a DTO - Retorna lista inmutable
     * 
     * @param entities Lista de entidades
     * @return Lista de DTOs solo de empleados con email
     */
    public List<EmployeeDto> toDtoListWithEmail(List<EmployeeEntity> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream().filter(EmployeeEntity::hasEmail).map(entityToDto)
                .collect(Collectors.toUnmodifiableList()); // Lista inmutable
    }

    /**
     * Convierte y ordena empleados por nombre completo.
     * 
     * Ejemplo de operaciones funcionales encadenadas.
     * 
     * @param entities Lista de entidades
     * @return Lista de DTOs ordenada por nombre
     */
    public List<EmployeeDto> toDtoListSortedByName(List<EmployeeEntity> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream().sorted((e1, e2) -> e1.getFullName().compareTo(e2.getFullName()))
                .map(entityToDto).collect(Collectors.toList());
    }

    /**
     * Extrae solo los nombres completos de una lista de entidades.
     * 
     * Ejemplo de transformación directa usando programación funcional.
     * 
     * @param entities Lista de entidades
     * @return Lista de nombres completos
     */
    public List<String> extractFullNames(List<EmployeeEntity> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream().map(EmployeeEntity::getFullName).collect(Collectors.toList());
    }

    /**
     * Convierte entidades a DTOs y cuenta cuántos tienen email.
     * 
     * Ejemplo de operación funcional con estadísticas.
     * 
     * @param entities Lista de entidades
     * @return Cantidad de empleados con email
     */
    public long countWithEmail(List<EmployeeEntity> entities) {
        if (entities == null) {
            return 0L;
        }

        return entities.stream().filter(EmployeeEntity::hasEmail).count();
    }

    // ========================================================================
    // VALIDACIONES Y UTILIDADES
    // ========================================================================

    /**
     * Verifica si un DTO tiene datos válidos mínimos. Función pura para validación.
     * 
     * @param dto DTO a validar
     * @return true si tiene al menos nombre y apellido
     */
    public boolean isValidDto(EmployeeDto dto) {
        return dto != null && dto.getFirstName() != null && !dto.getFirstName().trim().isEmpty()
                && dto.getLastName() != null && !dto.getLastName().trim().isEmpty();
    }

    /**
     * Verifica si una entidad tiene datos válidos mínimos.
     * 
     * @param entity Entidad a validar
     * @return true si tiene al menos nombre y apellido
     */
    public boolean isValidEntity(EmployeeEntity entity) {
        return entity != null && entity.getFirstName() != null
                && !entity.getFirstName().trim().isEmpty() && entity.getLastName() != null
                && !entity.getLastName().trim().isEmpty();
    }
}
