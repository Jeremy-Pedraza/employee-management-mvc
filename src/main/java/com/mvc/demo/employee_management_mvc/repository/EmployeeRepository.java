package com.mvc.demo.employee_management_mvc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mvc.demo.employee_management_mvc.model.EmployeeEntity;

/**
 * Repositorio para la entidad EmployeeEntity.
 * 
 * Esta interfaz extiende JpaRepository que proporciona operaciones CRUD automáticas y métodos
 * adicionales para consultas personalizadas.
 * 
 * Principios SOLID aplicados: - Single Responsibility: Solo maneja persistencia de empleados -
 * Interface Segregation: Define solo los métodos necesarios - Dependency Inversion: Los servicios
 * dependen de esta abstracción - Open/Closed: Abierta para extensión (agregar métodos), cerrada
 * para modificación
 * 
 * Características de Programación Funcional: - Retorna Optional para manejar ausencia de valores -
 * Usa Stream-friendly List para operaciones funcionales - Métodos declarativos (qué hacer, no cómo
 * hacerlo)
 * 
 * Spring Data JPA proporciona automáticamente: - save(entity): Guardar o actualizar - findById(id):
 * Buscar por ID - findAll(): Obtener todos - deleteById(id): Eliminar por ID - count(): Contar
 * registros - existsById(id): Verificar existencia
 * 
 * @author Employee Management Team
 * @version 1.0.0
 */
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

       // ========================================================================
       // CONSULTAS DERIVADAS (Query Methods)
       // Spring Data JPA genera automáticamente la implementación
       // ========================================================================

       /**
        * Busca un empleado por su correo electrónico.
        * 
        * Método de consulta derivado: Spring Data JPA crea la query automáticamente basándose en el
        * nombre del método.
        * 
        * Retorna Optional para programación funcional segura.
        * 
        * Query generada automáticamente: SELECT e FROM EmployeeEntity e WHERE e.email = ?1
        * 
        * @param email Correo electrónico del empleado
        * @return Optional con el empleado si existe, vacío si no
        */
       Optional<EmployeeEntity> findByEmail(String email);

       /**
        * Busca empleados por nombre (firstName).
        * 
        * Query generada: WHERE e.firstName = ?1
        * 
        * @param firstName Nombre a buscar
        * @return Lista de empleados con ese nombre
        */
       List<EmployeeEntity> findByFirstName(String firstName);

       /**
        * Busca empleados por apellido (lastName).
        * 
        * Query generada: WHERE e.lastName = ?1
        * 
        * @param lastName Apellido a buscar
        * @return Lista de empleados con ese apellido
        */
       List<EmployeeEntity> findByLastName(String lastName);

       /**
        * Busca empleados por nombre Y apellido.
        * 
        * Query generada: WHERE e.firstName = ?1 AND e.lastName = ?2
        * 
        * @param firstName Nombre del empleado
        * @param lastName Apellido del empleado
        * @return Lista de empleados que coinciden
        */
       List<EmployeeEntity> findByFirstNameAndLastName(String firstName, String lastName);

       /**
        * Busca empleados cuyo nombre contenga el texto especificado (ignorando mayúsculas).
        * 
        * Query generada: WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', ?1, '%'))
        * 
        * Ejemplo: findByFirstNameContainingIgnoreCase("mar") Encuentra: "María", "Mario", "Mariana"
        * 
        * @param firstName Texto a buscar en el nombre
        * @return Lista de empleados que coinciden
        */
       List<EmployeeEntity> findByFirstNameContainingIgnoreCase(String firstName);

       /**
        * Busca empleados cuyo apellido contenga el texto especificado (ignorando mayúsculas).
        * 
        * Query generada: WHERE LOWER(e.lastName) LIKE LOWER(CONCAT('%', ?1, '%'))
        * 
        * @param lastName Texto a buscar en el apellido
        * @return Lista de empleados que coinciden
        */
       List<EmployeeEntity> findByLastNameContainingIgnoreCase(String lastName);

       /**
        * Verifica si existe un empleado con el email especificado.
        * 
        * Útil para validar unicidad antes de guardar. Query generada: SELECT COUNT(e) > 0 FROM
        * EmployeeEntity e WHERE e.email = ?1
        * 
        * @param email Email a verificar
        * @return true si existe, false si no
        */
       boolean existsByEmail(String email);

       /**
        * Verifica si existe un empleado con email diferente al ID dado. Útil para validar unicidad
        * en actualizaciones.
        * 
        * Query generada: SELECT COUNT(e) > 0 FROM EmployeeEntity e WHERE e.email = ?1 AND e.id !=
        * ?2
        * 
        * @param email Email a verificar
        * @param id ID del empleado a excluir de la búsqueda
        * @return true si existe otro empleado con ese email
        */
       boolean existsByEmailAndIdNot(String email, Long id);

       // ========================================================================
       // CONSULTAS JPQL PERSONALIZADAS
       // Para casos más complejos que requieren control fino
       // ========================================================================

       /**
        * Busca empleados por nombre O apellido (búsqueda flexible).
        * 
        * Consulta JPQL personalizada para búsqueda más flexible. Usa @Query para definir la
        * consulta manualmente.
        * 
        * @param firstName Nombre a buscar
        * @param lastName Apellido a buscar
        * @return Lista de empleados que coinciden con nombre O apellido
        */
       @Query("SELECT e FROM EmployeeEntity e WHERE "
                     + "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) OR "
                     + "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
       List<EmployeeEntity> searchByFirstNameOrLastName(@Param("firstName") String firstName,
                     @Param("lastName") String lastName);

       /**
        * Busca empleados por cualquier campo (búsqueda global).
        * 
        * Esta consulta busca en nombre, apellido Y email simultáneamente. Útil para un campo de
        * búsqueda general en la UI.
        * 
        * @param searchTerm Término de búsqueda
        * @return Lista de empleados que coinciden en cualquier campo
        */
       @Query("SELECT e FROM EmployeeEntity e WHERE "
                     + "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
                     + "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
                     + "LOWER(e.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
       List<EmployeeEntity> globalSearch(@Param("searchTerm") String searchTerm);

       /**
        * Obtiene empleados ordenados por nombre completo.
        * 
        * @return Lista de empleados ordenados por firstName y luego lastName
        */
       @Query("SELECT e FROM EmployeeEntity e ORDER BY e.firstName ASC, e.lastName ASC")
       List<EmployeeEntity> findAllOrderedByName();

       /**
        * Obtiene empleados que tienen email registrado.
        * 
        * Útil para filtrar empleados con información de contacto completa.
        * 
        * @return Lista de empleados con email no nulo
        */
       @Query("SELECT e FROM EmployeeEntity e WHERE e.email IS NOT NULL AND e.email != ''")
       List<EmployeeEntity> findAllWithEmail();

       /**
        * Obtiene empleados sin email registrado.
        * 
        * Útil para identificar empleados con información incompleta.
        * 
        * @return Lista de empleados sin email
        */
       @Query("SELECT e FROM EmployeeEntity e WHERE e.email IS NULL OR e.email = ''")
       List<EmployeeEntity> findAllWithoutEmail();

       /**
        * Cuenta empleados por nombre.
        * 
        * Ejemplo de consulta de agregación.
        * 
        * @param firstName Nombre a buscar
        * @return Cantidad de empleados con ese nombre
        */
       @Query("SELECT COUNT(e) FROM EmployeeEntity e WHERE e.firstName = :firstName")
       long countByFirstName(@Param("firstName") String firstName);

       /**
        * Obtiene los nombres únicos de empleados.
        * 
        * Útil para estadísticas o autocompletado.
        * 
        * @return Lista de nombres únicos
        */
       @Query("SELECT DISTINCT e.firstName FROM EmployeeEntity e ORDER BY e.firstName")
       List<String> findDistinctFirstNames();

       /**
        * Obtiene los apellidos únicos de empleados.
        * 
        * @return Lista de apellidos únicos
        */
       @Query("SELECT DISTINCT e.lastName FROM EmployeeEntity e ORDER BY e.lastName")
       List<String> findDistinctLastNames();

       // ========================================================================
       // CONSULTAS SQL NATIVAS (Para casos muy específicos)
       // ========================================================================

       /**
        * Ejemplo de consulta SQL nativa.
        * 
        * Usa @Query con nativeQuery = true para SQL directo. Útil cuando JPQL no es suficiente o
        * cuando necesitas características específicas de la base de datos.
        * 
        * @param searchTerm Término de búsqueda
        * @return Lista de empleados
        */
       @Query(value = "SELECT * FROM TBL_EMPLOYEES WHERE "
                     + "LOWER(first_name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
                     + "LOWER(last_name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))",
                     nativeQuery = true)
       List<EmployeeEntity> nativeGlobalSearch(@Param("searchTerm") String searchTerm);

       /**
        * Obtiene estadísticas básicas de empleados usando SQL nativo.
        * 
        * @return Cantidad total de empleados
        */
       @Query(value = "SELECT COUNT(*) FROM TBL_EMPLOYEES", nativeQuery = true)
       long countAllNative();

       // ========================================================================
       // NOTAS SOBRE PROGRAMACIÓN FUNCIONAL
       // ========================================================================

       /*
        * Todos los métodos que retornan List<EmployeeEntity> pueden ser usados con Streams para
        * operaciones funcionales:
        * 
        * Ejemplos:
        * 
        * // Filtrar y transformar repository.findAll().stream() .filter(EmployeeEntity::hasEmail)
        * .map(EmployeeEntity::getFullName) .collect(Collectors.toList());
        * 
        * // Buscar el primero que cumple condición repository.findAll().stream() .filter(e ->
        * e.getFirstName().startsWith("M")) .findFirst();
        * 
        * // Contar empleados con condición long count = repository.findAll().stream() .filter(e ->
        * e.getEmail() != null) .count();
        * 
        * Los métodos que retornan Optional facilitan el manejo funcional:
        * 
        * repository.findByEmail("test@example.com") .map(EmployeeEntity::getFullName)
        * .orElse("No encontrado");
        */
}
