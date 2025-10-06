package com.mvc.demo.employee_management_mvc.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * Manejador global de excepciones para toda la aplicación.
 * 
 * Esta clase centraliza el manejo de excepciones usando Spring's @ControllerAdvice. Captura
 * excepciones lanzadas por los controladores y las maneja de forma uniforme.
 * 
 * Principios SOLID aplicados: - Single Responsibility: Solo maneja excepciones - Open/Closed:
 * Abierto para agregar nuevos manejadores de excepción - Dependency Inversion: Depende de
 * abstracciones de Spring
 * 
 * Características de Programación Funcional: - Usa Streams para procesar errores de validación -
 * Métodos puros para construcción de mensajes - Inmutabilidad en construcción de respuestas
 * 
 * @author Employee Management Team
 * @version 1.0.0
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ========================================================================
    // MANEJO DE EMPLEADO NO ENCONTRADO
    // ========================================================================

    /**
     * Maneja la excepción cuando un empleado no es encontrado.
     * 
     * Este método captura EmployeeNotFoundException y retorna una vista de error amigable al
     * usuario.
     * 
     * @param ex La excepción lanzada
     * @param model Modelo para pasar datos a la vista
     * @return Vista de error
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleEmployeeNotFoundException(EmployeeNotFoundException ex, Model model) {

        log.error("Empleado no encontrado: {}", ex.getMessage());

        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("errorTitle", "Empleado No Encontrado");
        mav.addObject("errorMessage", ex.getMessage());
        mav.addObject("errorDetails",
                ex.hasEmployeeId() ? String.format("ID del empleado: %d", ex.getEmployeeId())
                        : "No se especificó ID de empleado");
        mav.addObject("statusCode", HttpStatus.NOT_FOUND.value());

        return mav;
    }

    // ========================================================================
    // MANEJO DE ERRORES DE VALIDACIÓN
    // ========================================================================

    /**
     * Maneja errores de validación de datos.
     * 
     * Este método captura MethodArgumentNotValidException que se lanza cuando fallan las
     * validaciones de @Valid en los DTOs.
     * 
     * Usa Streams para procesar errores de forma funcional.
     * 
     * @param ex La excepción de validación
     * @param model Modelo para la vista
     * @return Vista con errores de validación
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleValidationExceptions(MethodArgumentNotValidException ex,
            Model model) {

        log.warn("Errores de validación detectados");

        BindingResult bindingResult = ex.getBindingResult();

        // Uso de Streams para procesar errores de forma funcional
        Map<String, String> errors = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage()
                                : "Error de validación",
                        (existing, replacement) -> existing // En caso de duplicados, mantener el
                                                            // primero
                ));

        // Log de errores detallados
        errors.forEach((field, message) -> log.debug("Campo '{}': {}", field, message));

        ModelAndView mav = new ModelAndView("error/validation");
        mav.addObject("errorTitle", "Errores de Validación");
        mav.addObject("errorMessage", "Por favor corrija los siguientes errores:");
        mav.addObject("validationErrors", errors);
        mav.addObject("statusCode", HttpStatus.BAD_REQUEST.value());

        return mav;
    }

    // ========================================================================
    // MANEJO DE EXCEPCIONES DE BASE DE DATOS
    // ========================================================================

    /**
     * Maneja excepciones relacionadas con la base de datos.
     * 
     * @param ex La excepción de base de datos
     * @param model Modelo para la vista
     * @return Vista de error de base de datos
     */
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView handleDataIntegrityViolation(
            org.springframework.dao.DataIntegrityViolationException ex, Model model) {

        log.error("Error de integridad de datos: {}", ex.getMessage());

        ModelAndView mav = new ModelAndView("error/database");
        mav.addObject("errorTitle", "Error de Integridad de Datos");

        // Detectar tipo de error específico
        String errorMessage = determineDataIntegrityErrorMessage(ex);

        mav.addObject("errorMessage", errorMessage);
        mav.addObject("errorDetails", "Por favor verifique que no existan datos duplicados");
        mav.addObject("statusCode", HttpStatus.CONFLICT.value());

        return mav;
    }

    // ========================================================================
    // MANEJO DE EXCEPCIONES GENÉRICAS
    // ========================================================================

    /**
     * Maneja todas las excepciones no capturadas por otros handlers.
     * 
     * Este es el manejador de último recurso que captura cualquier excepción no manejada
     * específicamente.
     * 
     * @param ex La excepción genérica
     * @param model Modelo para la vista
     * @return Vista de error genérico
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGenericException(Exception ex, Model model) {

        log.error("Error inesperado en la aplicación", ex);

        ModelAndView mav = new ModelAndView("error/generic");
        mav.addObject("errorTitle", "Error Inesperado");
        mav.addObject("errorMessage",
                "Ha ocurrido un error inesperado. Por favor intente nuevamente.");
        mav.addObject("errorDetails", ex.getMessage());
        mav.addObject("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return mav;
    }

    // ========================================================================
    // MÉTODOS AUXILIARES PRIVADOS (Helper Methods)
    // ========================================================================

    /**
     * Determina un mensaje de error amigable basado en el error de integridad. Método funcional
     * puro sin efectos secundarios.
     * 
     * @param ex Excepción de integridad de datos
     * @return Mensaje de error amigable
     */
    private String determineDataIntegrityErrorMessage(
            org.springframework.dao.DataIntegrityViolationException ex) {

        String message = ex.getMessage();

        if (message != null) {
            if (message.contains("email") || message.contains("EMAIL")) {
                return "El correo electrónico ya está registrado en el sistema";
            } else if (message.contains("PRIMARY KEY") || message.contains("primary key")) {
                return "Ya existe un empleado con ese identificador";
            } else if (message.contains("UNIQUE") || message.contains("unique")) {
                return "Ya existe un registro con esa información";
            }
        }

        return "Error al guardar los datos. Por favor verifique la información e intente nuevamente";
    }

    /**
     * Crea un mapa de errores para respuestas JSON (para APIs REST futuras). Ejemplo de
     * construcción inmutable usando programación funcional.
     * 
     * @param message Mensaje de error
     * @param details Detalles adicionales
     * @return Mapa inmutable con información del error
     */
    private Map<String, Object> createErrorResponse(String message, String details) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", java.time.LocalDateTime.now());
        errorResponse.put("message", message);
        errorResponse.put("details", details);
        return errorResponse;
    }
}
