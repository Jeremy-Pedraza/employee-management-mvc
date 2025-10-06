package com.mvc.demo.employee_management_mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Employee Management System.
 * 
 * Esta clase sirve como punto de entrada para la aplicación Spring Boot. La
 * anotación @SpringBootApplication combina tres anotaciones importantes:
 * 
 * 1. @Configuration: Marca la clase como fuente de definiciones de beans
 * 2. @EnableAutoConfiguration: Habilita la configuración automática de Spring Boot
 * 3. @ComponentScan: Escanea componentes en el paquete actual y sus subpaquetes
 * 
 * Principios SOLID aplicados: - Single Responsibility: Esta clase tiene una única responsabilidad -
 * iniciar la aplicación - Open/Closed: Extensible mediante configuración, cerrada para modificación
 * - Dependency Inversion: Depende de abstracciones de Spring Framework
 * 
 * @author Employee Management Team
 * @version 1.0.0
 * @since 2024
 */
@SpringBootApplication
public class DemoApplication {

    /**
     * Método main - Punto de entrada de la aplicación.
     * 
     * Este método utiliza SpringApplication.run() para arrancar la aplicación, lo cual: - Crea un
     * ApplicationContext apropiado - Registra un CommandLinePropertySource para exponer argumentos
     * como propiedades - Actualiza el ApplicationContext - Dispara cualquier CommandLineRunner
     * beans
     * 
     * @param args Argumentos de línea de comandos pasados a la aplicación
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

        // Log de inicio exitoso
        System.out.println(
                "\n" + "╔═══════════════════════════════════════════════════════════════╗\n"
                        + "║                                                               ║\n"
                        + "║    🏢 EMPLOYEE MANAGEMENT SYSTEM - INICIADO EXITOSAMENTE      ║\n"
                        + "║                                                               ║\n"
                        + "║    📱 Aplicación: http://localhost:8080                       ║\n"
                        + "║    🗄️  Consola H2:  http://localhost:8080/h2-console          ║\n"
                        + "║                                                               ║\n"
                        + "║    ✅ Spring Boot 3.2.0                                      ║\n"
                        + "║    ✅ Java 17                                                ║\n"
                        + "║    ✅ Base de Datos H2 en Memoria                            ║\n"
                        + "║                                                               ║\n"
                        + "╚═══════════════════════════════════════════════════════════════╝\n");
    }
}
