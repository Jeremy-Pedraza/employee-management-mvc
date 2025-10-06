package com.mvc.demo.employee_management_mvc.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.demo.employee_management_mvc.dto.EmployeeDto;
import com.mvc.demo.employee_management_mvc.exception.EmployeeNotFoundException;
import com.mvc.demo.employee_management_mvc.mapper.EmployeeMapper;
import com.mvc.demo.employee_management_mvc.model.EmployeeEntity;
import com.mvc.demo.employee_management_mvc.repository.EmployeeRepository;
import com.mvc.demo.employee_management_mvc.service.EmployeeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de empleados.
 * 
 * Esta clase contiene toda la lógica de negocio relacionada con empleados. Aplica principios SOLID
 * y combina POO con Programación Funcional.
 * 
 * Principios SOLID aplicados: - Single Responsibility: Solo lógica de negocio de empleados -
 * Open/Closed: Extensible mediante herencia o composición - Liskov Substitution: Implementa
 * completamente EmployeeService - Interface Segregation: Implementa solo métodos necesarios -
 * Dependency Inversion: Depende de abstracciones (Repository, Mapper)
 * 
 * Características de Programación Funcional: - Uso extensivo de Streams API - Operaciones con
 * Optional - Funciones puras sin efectos secundarios - Composición de funciones - Inmutabilidad en
 * DTOs
 * 
 * @author Employee Management Team
 * @version 1.0.0
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    // ========================================================================
    // DEPENDENCIAS (Inyección de Dependencias - DI)
    // ========================================================================

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    // ========================================================================
    // OPERACIONES CRUD BÁSICAS
    // ========================================================================

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getAllEmployees() {
        log.debug("Obteniendo todos los empleados");

        return employeeRepository.findAll().stream().map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeDto> getEmployeeById(Long id) {
        log.debug("Buscando empleado con ID: {}", id);

        validateId(id);

        return employeeRepository.findById(id).map(employeeMapper::toDto).map(dto -> {
            log.debug("Empleado encontrado: {}", dto.getFullName());
            return dto;
        });
    }

    @Override
    public EmployeeDto createOrUpdateEmployee(EmployeeDto employeeDto) {
        log.debug("Guardando empleado: {}", employeeDto);

        validateEmployeeDto(employeeDto);
        validateEmailUniqueness(employeeDto);

        EmployeeEntity savedEntity = employeeDto.isNew() ? createNewEmployee(employeeDto)
                : updateExistingEmployee(employeeDto);

        EmployeeDto savedDto = employeeMapper.toDto(savedEntity);
        log.info("Empleado guardado exitosamente: ID={}, Nombre={}", savedDto.getId(),
                savedDto.getFullName());

        return savedDto;
    }

    @Override
    public void deleteEmployeeById(Long id) {
        log.debug("Eliminando empleado con ID: {}", id);

        validateId(id);

        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        employeeRepository.deleteById(id);

        log.info("Empleado eliminado: ID={}, Nombre={}", id, employee.getFullName());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        validateId(id);
        return employeeRepository.existsById(id);
    }

    // ========================================================================
    // OPERACIONES DE BÚSQUEDA
    // ========================================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeDto> getEmployeeByEmail(String email) {
        log.debug("Buscando empleado por email: {}", email);

        validateEmail(email);

        return employeeRepository.findByEmail(email).map(employeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getEmployeesByFirstName(String firstName) {
        log.debug("Buscando empleados por nombre: {}", firstName);

        return employeeRepository.findByFirstNameContainingIgnoreCase(firstName).stream()
                .map(employeeMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getEmployeesByLastName(String lastName) {
        log.debug("Buscando empleados por apellido: {}", lastName);

        return employeeRepository.findByLastNameContainingIgnoreCase(lastName).stream()
                .map(employeeMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> searchEmployees(String searchTerm) {
        log.debug("Búsqueda global con término: {}", searchTerm);

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllEmployees();
        }

        return employeeRepository.globalSearch(searchTerm.trim()).stream()
                .map(employeeMapper::toDto).collect(Collectors.toList());
    }

    // ========================================================================
    // OPERACIONES FUNCIONALES AVANZADAS
    // ========================================================================

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getAllEmployeesSortedByName() {
        log.debug("Obteniendo empleados ordenados por nombre");

        return employeeRepository.findAllOrderedByName().stream().map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getEmployeesWithEmail() {
        log.debug("Obteniendo empleados con email");

        return employeeRepository.findAllWithEmail().stream().map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getEmployeesWithoutEmail() {
        log.debug("Obteniendo empleados sin email");

        return employeeRepository.findAllWithoutEmail().stream().map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countAllEmployees() {
        return employeeRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countEmployeesWithEmail() {
        return employeeRepository.findAll().stream().filter(EmployeeEntity::hasEmail).count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllEmployeeInitials() {
        return employeeRepository.findAll().stream().map(EmployeeEntity::getInitials)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllEmployeeFullNames() {
        return employeeRepository.findAll().stream().map(EmployeeEntity::getFullName)
                .collect(Collectors.toList());
    }

    // ========================================================================
    // VALIDACIONES
    // ========================================================================

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return employeeRepository.existsByEmail(email.trim().toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExistsForOtherEmployee(String email, Long excludeId) {
        if (email == null || email.trim().isEmpty() || excludeId == null) {
            return false;
        }
        return employeeRepository.existsByEmailAndIdNot(email.trim().toLowerCase(), excludeId);
    }

    @Override
    public boolean isValidEmployee(EmployeeDto employeeDto) {
        return employeeMapper.isValidDto(employeeDto);
    }

    // ========================================================================
    // MÉTODOS PRIVADOS AUXILIARES (Helper Methods)
    // ========================================================================

    private EmployeeEntity createNewEmployee(EmployeeDto employeeDto) {
        log.debug("Creando nuevo empleado: {}", employeeDto.getFullName());

        EmployeeEntity newEntity = employeeMapper.toEntity(employeeDto);
        return employeeRepository.save(newEntity);
    }

    private EmployeeEntity updateExistingEmployee(EmployeeDto employeeDto) {
        log.debug("Actualizando empleado ID: {}", employeeDto.getId());

        EmployeeEntity existingEntity = employeeRepository.findById(employeeDto.getId())
                .orElseThrow(() -> new EmployeeNotFoundException(employeeDto.getId()));

        employeeMapper.updateEntityFromDto(existingEntity, employeeDto);

        return employeeRepository.save(existingEntity);
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser null");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
    }

    private void validateEmployeeDto(EmployeeDto employeeDto) {
        if (employeeDto == null) {
            throw new IllegalArgumentException("El empleado no puede ser null");
        }
        if (!employeeMapper.isValidDto(employeeDto)) {
            throw new IllegalArgumentException("El empleado debe tener nombre y apellido válidos");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
    }

    private void validateEmailUniqueness(EmployeeDto employeeDto) {
        if (employeeDto.getEmail() == null || employeeDto.getEmail().trim().isEmpty()) {
            return;
        }

        boolean emailExists = employeeDto.isNew() ? emailExists(employeeDto.getEmail())
                : emailExistsForOtherEmployee(employeeDto.getEmail(), employeeDto.getId());

        if (emailExists) {
            throw new IllegalArgumentException(
                    "El email " + employeeDto.getEmail() + " ya está registrado");
        }
    }
}
