package com.mvc.demo.employee_management_mvc.config;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * Configuración Web para Spring MVC.
 * 
 * <p>
 * Esta clase implementa {@link WebMvcConfigurer} para personalizar la configuración de Spring MVC
 * siguiendo los principios SOLID:
 * </p>
 * 
 * <ul>
 * <li><b>S (Single Responsibility):</b> Solo se encarga de la configuración web</li>
 * <li><b>O (Open/Closed):</b> Extendible mediante beans adicionales sin modificar esta clase</li>
 * <li><b>D (Dependency Inversion):</b> Depende de abstracciones (interfaces) de Spring</li>
 * </ul>
 * 
 * @author Sistema de Gestión de Empleados
 * @version 1.0
 * @since 2025
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura los manejadores de recursos estáticos (CSS, JS, imágenes).
     * 
     * <p>
     * IMPORTANTE: Usamos rutas específicas (/css/**, /js/**, /images/**) en lugar de /** para
     * evitar conflictos con las rutas del controlador.
     * </p>
     * 
     * @param registry el registro de recursos
     */
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Configuración específica para CSS
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/")
                .setCachePeriod(getSecondsForDays(30));

        // Configuración específica para JavaScript
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/")
                .setCachePeriod(getSecondsForDays(30));

        // Configuración específica para imágenes
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/")
                .setCachePeriod(getSecondsForDays(365));

        // Webjars (librerías frontend como Bootstrap, jQuery via Maven)
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCachePeriod(getSecondsForDays(365));
    }

    /**
     * Configura los controladores de vista simples (sin lógica en controlador).
     * 
     * @param registry el registro de controladores de vista
     */
    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        // Redirección de la raíz a la lista de empleados
        registry.addRedirectViewController("/", "/employees");
    }

    /**
     * Configura CORS (Cross-Origin Resource Sharing) para APIs REST.
     * 
     * <p>
     * Útil si en el futuro se agrega un frontend separado (React, Angular, Vue).
     * </p>
     * 
     * @param registry el registro de configuración CORS
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*")
                .allowCredentials(true).maxAge(3600);
    }

    /**
     * Configura interceptores HTTP.
     * 
     * <p>
     * Los interceptores permiten ejecutar lógica antes y después de las peticiones. Útil para
     * logging, seguridad, cambio de idioma, etc.
     * </p>
     * 
     * @param registry el registro de interceptores
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // Interceptor para cambio de idioma (internacionalización)
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * Bean para cambiar el idioma de la aplicación mediante parámetro 'lang'.
     * 
     * <p>
     * Ejemplo de uso: {@code /employees?lang=es} o {@code /employees?lang=en}
     * </p>
     * 
     * @return el interceptor configurado
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        var interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    /**
     * Bean para resolver y almacenar el locale en la sesión HTTP.
     * 
     * <p>
     * Aplica <b>Inmutabilidad</b> al usar configuración declarativa.
     * </p>
     * 
     * @return el resolver de locale configurado
     */
    @Bean
    public SessionLocaleResolver localeResolver() {
        var localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.forLanguageTag("es"));
        return localeResolver;
    }

    /**
     * Calcula segundos para un número de días (usado en cache de recursos).
     * 
     * <p>
     * Este método es una <b>Función Pura</b>: sin efectos secundarios, mismo input = mismo output.
     * </p>
     * 
     * @param days número de días
     * @return segundos equivalentes
     */
    private int getSecondsForDays(int days) {
        return Math.toIntExact(TimeUnit.DAYS.toSeconds(days));
    }

    /**
     * Configura el formato de las rutas (opcional).
     * 
     * <p>
     * Deshabilitamos el sufijo automático para mayor claridad en URLs.
     * </p>
     * 
     * @param configurer el configurador de rutas
     */
    @Override
    public void configurePathMatch(@NonNull PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(true);
    }

    /**
     * Configuración de Content Negotiation (negociación de contenido).
     * 
     * <p>
     * Define cómo Spring decide qué formato devolver (HTML, JSON, XML).
     * </p>
     * 
     * @param configurer el configurador de contenido
     */
    @Override
    public void configureContentNegotiation(@NonNull ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false).favorParameter(true).parameterName("format")
                .ignoreAcceptHeader(false)
                .defaultContentType(org.springframework.http.MediaType.TEXT_HTML);
    }
}
