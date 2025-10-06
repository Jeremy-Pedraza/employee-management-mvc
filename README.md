<p align="center">
  <img src="https://upload.wikimedia.org/wikipedia/en/thumb/3/30/Java_programming_language_logo.svg/121px-Java_programming_language_logo.svg.png" alt="CoopRKC Logo" width="140" align="center">
</p>

<blockquote>
  <p><strong>S25 - Evidencia de Aprendizaje · Unidad 2: Programación Funcional en Java</strong><br>
  <strong>Actividad 1, 2 y 3 — Grupo 37</strong><br>
  Estiven Andrés Trujillo Montiel · Jeremy Iván Pedraza Hernández · Hahiler Esteban Guevara Estrada<br>
  <strong>Docente:</strong> Ramiro Antonio Giraldo Escobar<br>
  <strong>Curso:</strong> Programación Orientada a Objetos II — PREICA2502B010090<br>
  <strong>Programa:</strong> Ingeniería de Software y Datos — Facultad de Ingeniería y Ciencias Agropecuarias<br>
  <strong>Institución Universitaria Digital de Antioquia</strong> — <strong>2025</strong></p>
</blockquote>

<!-- Recursos: Videos Actividades 1, 2, 3 y Repositorios 2, 3 -->

<div align="center">
  <h3>🎓 Recursos de las Actividades</h3>
  <p>
    <a href="https://youtu.be/ZowapBpChS4" target="_blank" rel="noopener noreferrer">🎬 Video Actividad 1</a> ·
    <a href="https://youtu.be/leErN763Poo" target="_blank" rel="noopener noreferrer">🎬 Video Actividad 2</a> ·
    <a href="#" target="_blank" rel="noopener noreferrer">🎬 Video Actividad 3</a>
  </p>
  <p>
    <a href="https://github.com/Jeremy-Pedraza/proyecto-iudigital-poo.git" target="_blank" rel="noopener noreferrer">📦 Repositorio Actividad 2</a> ·
    <a href="https://github.com/Jeremy-Pedraza/employee-management-mvc.git" target="_blank" rel="noopener noreferrer">📦 Repositorio Actividad 3</a>
  </p>
  <h3>🎓 Recursos Google Drive Drive</h3>
    <a href="https://drive.google.com/drive/folders/16Y7ih-qP-NlakHIZoYbtVwSaUwDbFcq1?usp=sharing" target="_blank" rel="noopener noreferrer">📦 Drive Actividad 2</a> ·
    <a href="https://drive.google.com/drive/folders/1tFW_f6ANzBWTz5khA_ihJronMgdjwUu-?usp=sharing" target="_blank" rel="noopener noreferrer">📦 Drive Actividad 2</a> ·
    <a href="https://drive.google.com/drive/folders/1wW0R4_TZ5i9u2j_lrUyXrCgbqHGwa3TQ?usp=sharing" target="_blank" rel="noopener noreferrer">📦 Drive Actividad 3</a>
</div>

<hr/>

# 🏢 Employee Management System

Sistema de gestión de empleados desarrollado con **Spring Boot MVC**, aplicando principios **SOLID** y combinando **Programación Orientada a Objetos (POO)** con **Programación Funcional (PF)**.

## 📋 Descripción

Aplicación web que permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre una base de datos de empleados, implementando las mejores prácticas de desarrollo Java y arquitectura de software.

## 🛠️ Tecnologías Utilizadas

- **Java 17** - Lenguaje de programación
- **Spring Boot 3.2.0** - Framework principal
- **Spring MVC** - Patrón Modelo-Vista-Controlador
- **Spring Data JPA** - Persistencia de datos
- **Hibernate** - ORM (Object-Relational Mapping)
- **Thymeleaf** - Motor de plantillas
- **H2 Database** - Base de datos en memoria
- **Maven** - Gestión de dependencias
- **Lombok** - Reducción de código boilerplate
- **MapStruct** - Mapeo de objetos
- **JUnit 5** - Testing
- **Mockito** - Mocking para pruebas

## 🏗️ Arquitectura

### Principios SOLID Aplicados

- **S** - Single Responsibility: Cada clase tiene una única responsabilidad
- **O** - Open/Closed: Extensible sin modificación
- **L** - Liskov Substitution: Interfaces y contratos bien definidos
- **I** - Interface Segregation: Interfaces específicas y cohesivas
- **D** - Dependency Inversion: Dependencias de abstracciones

### Estructura del Proyecto

```
src/main/java/com/mvc/demo/
├── config/              # Configuraciones
├── model/               # Entidades JPA
├── repository/          # Acceso a datos
├── service/             # Lógica de negocio
├── web/controller/      # Controladores MVC
├── dto/                 # Data Transfer Objects
├── mapper/              # Conversión DTO <-> Entity
├── validation/          # Validaciones personalizadas
└── exception/           # Manejo de excepciones

src/main/resources/
├── templates/           # Vistas Thymeleaf
├── static/              # CSS, JS, imágenes
├── application.properties
├── schema.sql
└── data.sql
```

## 🚀 Instalación y Configuración

### Prerequisitos

- JDK 17 o superior
- Maven 3.6+
- IDE (VS Code, IntelliJ IDEA, Eclipse, NetBeans)

### Pasos de Instalación

1. **Clonar el repositorio**

```bash
git clone <url-del-repositorio>
cd employee-management-mvc
```

2. **Compilar el proyecto**

```bash
mvn clean install
```

3. **Ejecutar la aplicación**

```bash
mvn spring-boot:run
```

O ejecutar el JAR generado:

```bash
java -jar target/employee-management-mvc.jar
```

4. **Acceder a la aplicación**

- Aplicación: http://localhost:8080
- Consola H2: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:employeedb`
  - Usuario: `sa`
  - Password: (dejar en blanco)

## 📚 Endpoints Principales

| Método | Endpoint          | Descripción                    |
| ------ | ----------------- | ------------------------------ |
| GET    | `/`               | Lista todos los empleados      |
| GET    | `/addEmployee`    | Formulario para nuevo empleado |
| GET    | `/edit/{id}`      | Formulario de edición          |
| POST   | `/createEmployee` | Crear/actualizar empleado      |
| GET    | `/delete/{id}`    | Eliminar empleado              |

## 🧪 Ejecutar Tests

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar tests con reporte de cobertura
mvn test jacoco:report
```

## 🎯 Características Implementadas

### POO (Programación Orientada a Objetos)

- ✅ Encapsulamiento con clases y métodos privados
- ✅ Herencia e interfaces bien definidas
- ✅ Polimorfismo en servicios
- ✅ Abstracción de capas

### Programación Funcional

- ✅ Uso de Streams API
- ✅ Expresiones Lambda
- ✅ Optional para manejo de nulos
- ✅ Interfaces funcionales (Function, Predicate, etc.)
- ✅ Inmutabilidad en DTOs
- ✅ Funciones puras sin efectos secundarios

### Patrones de Diseño

- ✅ Repository Pattern (acceso a datos)
- ✅ Service Layer Pattern (lógica de negocio)
- ✅ DTO Pattern (transferencia de datos)
- ✅ Mapper Pattern (conversión de objetos)
- ✅ Dependency Injection (Spring)

## 📝 Buenas Prácticas Implementadas

- ✅ Código limpio y auto-documentado
- ✅ Nombres descriptivos de variables y métodos
- ✅ Separación de responsabilidades
- ✅ Validación de datos
- ✅ Manejo centralizado de excepciones
- ✅ Logging apropiado
- ✅ Tests unitarios
- ✅ Documentación JavaDoc

## 🤝 Contribución

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo LICENSE para más detalles.

## 👥 Autor

Desarrollado como proyecto educativo siguiendo principios SOLID y mejores prácticas de Java/Spring Boot.

## 📞 Contacto

Para preguntas o sugerencias, por favor abra un issue en el repositorio.

---

**⭐ Si te ha gustado este proyecto, dale una estrella!**
