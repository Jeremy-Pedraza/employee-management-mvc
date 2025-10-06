package com.mvc.demo.employee_management_mvc.web.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mvc.demo.employee_management_mvc.dto.EmployeeDto;
import com.mvc.demo.employee_management_mvc.exception.EmployeeNotFoundException;
import com.mvc.demo.employee_management_mvc.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador MVC para la gestión de empleados.
 * 
 * Este controlador maneja todas las peticiones HTTP relacionadas con empleados y coordina la
 * interacción entre el modelo (servicio) y la vista (templates).
 * 
 * Principios SOLID aplicados: - Single Responsibility: Solo maneja peticiones web de empleados -
 * Open/Closed: Extensible mediante herencia - Dependency Inversion: Depende de la abstracción
 * EmployeeService
 * 
 * Características de Programación Funcional: - Uso de Optional para valores ausentes - Operaciones
 * con Streams cuando se procesan listas - Métodos declarativos y expresivos
 * 
 * Patrón MVC: - Modelo: EmployeeDto - Vista: Thymeleaf templates - Controlador: Esta clase
 * 
 * @author Employee Management Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class EmployeeMvcController {

    // ========================================================================
    // DEPENDENCIAS
    // ========================================================================

    /**
     * Servicio de empleados (inyección de dependencias).
     */
    private final EmployeeService employeeService;

    // ========================================================================
    // ENDPOINTS DE VISUALIZACIÓN (GET)
    // ========================================================================

    /**
     * Página principal - Lista todos los empleados.
     * 
     * Endpoint: GET / Vista: list-employees.html
     * 
     * Ejemplo de programación funcional: obtiene la lista y la transforma en el servicio usando
     * Streams.
     * 
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la vista a renderizar
     */
    @GetMapping
    public String listEmployees(Model model) {
        log.info("Solicitando lista de todos los empleados");

        // Obtener todos los empleados (el servicio usa Streams internamente)
        List<EmployeeDto> employees = employeeService.getAllEmployees();

        // Agregar datos al modelo para la vista
        model.addAttribute("employees", employees);
        model.addAttribute("totalEmployees", employees.size());
        model.addAttribute("pageTitle", "Lista de Empleados");

        log.debug("Mostrando {} empleados", employees.size());

        return "list-employees";
    }

    /**
     * Formulario para agregar un nuevo empleado.
     * 
     * Endpoint: GET /addEmployee Vista: add-edit-employee.html
     * 
     * @param model Modelo para la vista
     * @return Nombre de la vista
     */
    @GetMapping("/addEmployee")
    public String showAddEmployeeForm(Model model) {
        log.info("Mostrando formulario para nuevo empleado");

        // Crear un DTO vacío para el formulario
        EmployeeDto employeeDto = EmployeeDto.builder().build();

        model.addAttribute("employee", employeeDto);
        model.addAttribute("pageTitle", "Agregar Empleado");
        model.addAttribute("isEdit", false);

        return "add-edit-employee";
    }

    /**
     * Formulario para editar un empleado existente.
     * 
     * Endpoint: GET /edit/{id} Vista: add-edit-employee.html
     * 
     * Ejemplo de uso de Optional para manejo funcional de ausencia.
     * 
     * @param id ID del empleado a editar
     * @param model Modelo para la vista
     * @return Nombre de la vista
     * @throws EmployeeNotFoundException si el empleado no existe
     */
    @GetMapping("/edit/{id}")
    public String showEditEmployeeForm(@PathVariable("id") Long id, Model model) {
        log.info("Mostrando formulario de edición para empleado ID: {}", id);

        // Uso de Optional con programación funcional
        EmployeeDto employeeDto = employeeService.getEmployeeById(id).orElseThrow(() -> {
            log.error("Empleado con ID {} no encontrado", id);
            return new EmployeeNotFoundException(id);
        });

        model.addAttribute("employee", employeeDto);
        model.addAttribute("pageTitle", "Editar Empleado");
        model.addAttribute("isEdit", true);

        log.debug("Editando empleado: {}", employeeDto.getFullName());

        return "add-edit-employee";
    }

    // ========================================================================
    // ENDPOINTS DE ACCIONES (POST/GET para acciones)
    // ========================================================================

    /**
     * Procesa el formulario de crear/actualizar empleado.
     * 
     * Endpoint: POST /createEmployee
     * 
     * Este método maneja tanto creación como actualización. Usa @Valid para validaciones
     * automáticas del DTO.
     * 
     * @param employeeDto DTO del empleado (con datos del formulario)
     * @param bindingResult Resultado de las validaciones
     * @param redirectAttributes Atributos para mensaje flash
     * @param model Modelo para la vista (en caso de error)
     * @return Redirección a lista o vuelta al formulario si hay errores
     */
    @PostMapping("/createEmployee")
    public String createOrUpdateEmployee(@Valid @ModelAttribute("employee") EmployeeDto employeeDto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        log.info("Procesando formulario de empleado: {}", employeeDto);

        // Si hay errores de validación, volver al formulario
        if (bindingResult.hasErrors()) {
            log.warn("Errores de validación encontrados");
            model.addAttribute("pageTitle",
                    employeeDto.isNew() ? "Agregar Empleado" : "Editar Empleado");
            model.addAttribute("isEdit", !employeeDto.isNew());
            return "add-edit-employee";
        }

        try {
            // Guardar el empleado
            EmployeeDto savedEmployee = employeeService.createOrUpdateEmployee(employeeDto);

            // Mensaje flash de éxito
            String successMessage = employeeDto.isNew()
                    ? "Empleado creado exitosamente: " + savedEmployee.getFullName()
                    : "Empleado actualizado exitosamente: " + savedEmployee.getFullName();

            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            log.info("Empleado guardado: ID={}, Nombre={}", savedEmployee.getId(),
                    savedEmployee.getFullName());

            // Redireccionar a la lista (patrón Post-Redirect-Get)
            return "redirect:/";

        } catch (IllegalArgumentException e) {
            log.error("Error de validación: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("pageTitle",
                    employeeDto.isNew() ? "Agregar Empleado" : "Editar Empleado");
            model.addAttribute("isEdit", !employeeDto.isNew());
            return "add-edit-employee";
        }
    }

    /**
     * Elimina un empleado.
     * 
     * Endpoint: GET /delete/{id}
     * 
     * Nota: En producción se debería usar DELETE HTTP method, pero para simplicidad usamos GET.
     * 
     * @param id ID del empleado a eliminar
     * @param redirectAttributes Atributos para mensaje flash
     * @return Redirección a la lista
     */
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Long id,
            RedirectAttributes redirectAttributes) {

        log.info("Solicitando eliminación de empleado ID: {}", id);

        try {
            // Obtener el nombre antes de eliminar (para el mensaje)
            Optional<String> employeeName =
                    employeeService.getEmployeeById(id).map(EmployeeDto::getFullName);

            // Eliminar el empleado
            employeeService.deleteEmployeeById(id);

            // Mensaje de éxito
            String message = employeeName.map(name -> "Empleado eliminado exitosamente: " + name)
                    .orElse("Empleado eliminado exitosamente");

            redirectAttributes.addFlashAttribute("successMessage", message);

            log.info("Empleado ID {} eliminado exitosamente", id);

        } catch (EmployeeNotFoundException e) {
            log.error("Error al eliminar: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/";
    }

    // ========================================================================
    // ENDPOINTS DE BÚSQUEDA
    // ========================================================================

    /**
     * Búsqueda de empleados.
     * 
     * Endpoint: GET /search?term=... Vista: list-employees.html
     * 
     * Ejemplo de programación funcional con filtrado.
     * 
     * @param searchTerm Término de búsqueda
     * @param model Modelo para la vista
     * @return Nombre de la vista
     */
    @GetMapping("/search")
    public String searchEmployees(@RequestParam(value = "term", required = false) String searchTerm,
            Model model) {

        log.info("Búsqueda de empleados con término: {}", searchTerm);

        List<EmployeeDto> employees;

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            // Si no hay término, mostrar todos
            employees = employeeService.getAllEmployees();
            model.addAttribute("searchInfo", "Mostrando todos los empleados");
        } else {
            // Búsqueda funcional usando el servicio
            employees = employeeService.searchEmployees(searchTerm);
            model.addAttribute("searchInfo", String.format("Encontrados %d empleados para '%s'",
                    employees.size(), searchTerm));
        }

        model.addAttribute("employees", employees);
        model.addAttribute("totalEmployees", employees.size());
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("pageTitle", "Búsqueda de Empleados");

        return "list-employees";
    }

    // ========================================================================
    // ENDPOINTS DE INFORMACIÓN/ESTADÍSTICAS
    // ========================================================================

    /**
     * Muestra estadísticas de empleados.
     * 
     * Endpoint: GET /stats
     * 
     * Ejemplo de uso de programación funcional para cálculos.
     * 
     * @param model Modelo para la vista
     * @return Nombre de la vista
     */
    @GetMapping("/stats")
    public String showStatistics(Model model) {
        log.info("Mostrando estadísticas de empleados");

        // Obtener estadísticas usando métodos funcionales del servicio
        long totalEmployees = employeeService.countAllEmployees();
        long employeesWithEmail = employeeService.countEmployeesWithEmail();
        long employeesWithoutEmail = totalEmployees - employeesWithEmail;

        // Calcular porcentajes
        double percentageWithEmail =
                totalEmployees > 0 ? (employeesWithEmail * 100.0 / totalEmployees) : 0.0;

        model.addAttribute("totalEmployees", totalEmployees);
        model.addAttribute("employeesWithEmail", employeesWithEmail);
        model.addAttribute("employeesWithoutEmail", employeesWithoutEmail);
        model.addAttribute("percentageWithEmail", String.format("%.1f", percentageWithEmail));
        model.addAttribute("pageTitle", "Estadísticas");

        return "statistics";
    }

    // ========================================================================
    // EXCEPTION HANDLERS LOCALES (complementarios al global)
    // ========================================================================

    /**
     * Maneja errores de validación a nivel de controlador.
     * 
     * Este handler es específico para este controlador. El GlobalExceptionHandler maneja errores a
     * nivel de aplicación.
     * 
     * @param ex Excepción de argumento ilegal
     * @param model Modelo para la vista
     * @return Vista de error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        log.error("Error de validación en controlador: {}", ex.getMessage());

        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("pageTitle", "Error de Validación");

        return "list-employees";
    }

    // ========================================================================
    // MÉTODOS AUXILIARES PRIVADOS
    // ========================================================================

    /**
     * Valida si una cadena está vacía. Función pura de utilidad.
     * 
     * @param str Cadena a validar
     * @return true si está vacía o es null
     */
    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
