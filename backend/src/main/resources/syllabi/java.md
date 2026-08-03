# 🎓 Sílabo Oficial — Java Backend Developer
### De cero a Junior competitivo en 4 meses
**Alumno:** Harold  
**Objetivo:** Empleo remoto como Java Backend Developer  
**Duración:** 16 semanas · 2-3 horas/día · ~5 días/semana  
**Nivel de entrada:** Básico | **Nivel de salida:** Junior competitivo  

---

## 📌 Resumen Ejecutivo

| Mes | Enfoque | Proyectos |
|-----|---------|-----------|
| Mes 1 | Fundamentos Java + POO + Bases | 2 mini proyectos |
| Mes 2 | Java profesional + Spring Boot básico | 2 proyectos medianos |
| Mes 3 | Spring Boot avanzado + Seguridad + Testing | 2 proyectos empresariales |
| Mes 4 | Microservicios + Docker + Portafolio + Empleo | 2 proyectos finales |

---

## 🗓️ MES 1 — Base Sólida (Semanas 1–4)
> *"Sin base, el rascacielos no existe."*

---

### 📅 SEMANA 1 — El ecosistema y Java desde cero

#### Módulo 0 · ¿Qué hace un Java Backend Developer?
| Día | Tema | Tiempo estimado |
|-----|------|----------------|
| Lunes | ¿Qué es un Backend? ¿Qué hace un Java Dev? | 2h |
| Martes | Cómo trabaja con Frontend, DevOps, QA, PM | 2h |
| Miércoles | ¿Cómo es un día real de trabajo en empresa? | 2h |
| Jueves | Ecosistema Java: JDK, JRE, JVM, bytecode | 2.5h |
| Viernes | Instalación y configuración: JDK 21, IntelliJ IDEA, Git | 2h |

**🎯 Objetivo de la semana:** Entender el ecosistema completo antes de codear.

---

#### Módulo 1 · Fundamentos de Programación en Java
| Día | Tema | Tiempo estimado |
|-----|------|----------------|
| Sábado | Tipos de datos, variables, constantes, `final` | 2.5h |
| Domingo | Operadores, expresiones, entrada/salida (`Scanner`) | 2h |

---

### 📅 SEMANA 2 — Java Fundamentos

#### Módulo 1 (continuación) + Módulo 2
| Día | Tema | Tiempo estimado |
|-----|------|----------------|
| Lunes | Condicionales: `if`, `else`, `switch` | 2h |
| Martes | Bucles: `for`, `while`, `do-while`, `for-each` | 2.5h |
| Miércoles | Métodos: parámetros, retorno, sobrecarga | 2.5h |
| Jueves | Arrays unidimensionales y bidimensionales | 2h |
| Viernes | Recursividad: qué es, cuándo usarla, factorial, Fibonacci | 2.5h |
| Sábado | **JVM profundo**: Stack, Heap, Garbage Collector, bytecode | 3h |
| Domingo | Memoria en Java: cómo vive un objeto, ciclo de vida | 2h |

**🎯 Objetivo de la semana:** Dominar los fundamentos y entender cómo Java ejecuta código.

---

### 📅 SEMANA 3 — Programación Orientada a Objetos (POO)

#### Módulo 3 — POO Completo
| Día | Tema | Tiempo estimado |
|-----|------|----------------|
| Lunes | Clases y objetos: qué son, cómo se crean, `new` | 2.5h |
| Martes | Constructores, `this`, sobrecarga de constructores | 2h |
| Miércoles | Encapsulamiento: `private`, getters, setters, por qué importa | 2.5h |
| Jueves | Herencia: `extends`, `super`, jerarquías | 2.5h |
| Viernes | Polimorfismo: sobreescritura, `@Override`, casteo | 2.5h |
| Sábado | Abstracción: clases abstractas vs interfaces | 3h |
| Domingo | Composición vs Herencia: cuándo usar cada una | 2h |

**🎯 Objetivo de la semana:** Pensar en objetos. Modelar el mundo real en código.

---

### 📅 SEMANA 4 — Principios de Diseño + Colecciones

#### Módulo 3 (SOLID) + Módulo 4
| Día | Tema | Tiempo estimado |
|-----|------|----------------|
| Lunes | Principios SOLID: S y O (Single Responsibility, Open/Closed) | 2.5h |
| Martes | Principios SOLID: L, I, D (Liskov, Interface Segregation, DI) | 2.5h |
| Miércoles | DRY, KISS, YAGNI — principios que todo dev profesional conoce | 2h |
| Jueves | Colecciones: `List`, `ArrayList`, `LinkedList` | 2.5h |
| Viernes | Colecciones: `Set`, `HashSet`, `TreeSet`, `LinkedHashSet` | 2.5h |
| Sábado | Colecciones: `Map`, `HashMap`, `TreeMap`, `LinkedHashMap` | 2.5h |
| Domingo | `Queue`, `Deque`, `PriorityQueue`, `Comparable`, `Comparator` | 2h |

**🎯 Objetivo de la semana:** Saber elegir la estructura de datos correcta para cada problema.

---

### 🏗️ MINI PROYECTO 1 — Sistema de Gestión de Inventario (consola)
> **Tecnologías:** Java puro, colecciones, POO, archivos  
> **Descripción:** Sistema CRUD de productos con categorías, búsqueda, ordenamiento y persistencia en archivo de texto.  
> **Git + GitHub requerido.**

---

## 🗓️ MES 2 — Java Profesional + Spring Básico (Semanas 5–8)

---

### 📅 SEMANA 5 — Excepciones, Logging y Archivos

#### Módulo 5 + Módulo 8
| Día | Tema | Tiempo estimado |
|-----|------|----------------|
| Lunes | Excepciones: checked vs unchecked, jerarquía | 2.5h |
| Martes | `try-catch-finally`, `throw`, `throws`, `try-with-resources` | 2.5h |
| Miércoles | Crear excepciones personalizadas | 2h |
| Jueves | Logging profesional con SLF4J + Logback | 2.5h |
| Viernes | IO y NIO: leer/escribir archivos, `Path`, `Files` | 2.5h |
| Sábado | JSON con Jackson: serialización y deserialización | 2.5h |
| Domingo | XML básico + repaso semanal | 2h |

---

### 📅 SEMANA 6 — Programación Funcional en Java

#### Módulo 6 — Lambdas, Streams, Optional
| Día | Tema | Tiempo estimado |
|-----|------|----------------|
| Lunes | Programación funcional: qué es y por qué importa | 2h |
| Martes | Lambda expressions: sintaxis, usos, functional interfaces | 2.5h |
| Miércoles | Streams API: `filter`, `map`, `reduce`, `collect` | 3h |
| Jueves | Streams avanzados: `flatMap`, `distinct`, `sorted`, `limit` | 2.5h |
| Viernes | `Optional`: el fin de NullPointerException | 2.5h |
| Sábado | Method References: `::`, cuatro tipos | 2h |
| Domingo | `Collectors`: `toList`, `groupingBy`, `joining`, `counting` | 2h |

**🎯 Objetivo:** Escribir código Java moderno, declarativo y limpio.

---

### 📅 SEMANA 7 — Bases de Datos con SQL

#### Módulo 9 — SQL, PostgreSQL, JDBC
| Día | Tema | Tiempo estimado |
|-----|------|----------------|
| Lunes | ¿Qué es una base de datos? Relacional vs NoSQL | 2h |
| Martes | DDL: `CREATE TABLE`, tipos de datos, `PRIMARY KEY`, `FOREIGN KEY` | 2.5h |
| Miércoles | DML: `INSERT`, `UPDATE`, `DELETE`, `SELECT` | 2.5h |
| Jueves | `JOIN`: `INNER`, `LEFT`, `RIGHT`, `FULL` | 3h |
| Viernes | Funciones de agregación, `GROUP BY`, `HAVING`, subconsultas | 2.5h |
| Sábado | Índices, transacciones, ACID, optimización básica | 2.5h |
| Domingo | JDBC: conectar Java con PostgreSQL sin frameworks | 2h |

---

### 📅 SEMANA 8 — Spring Framework + Spring Boot

#### Módulo 11 + Módulo 12 (inicio)
| Día | Tema | Tiempo estimado |
|-----|------|----------------|
| Lunes | ¿Qué es Spring? Historia, IoC, Dependency Injection | 2.5h |
| Martes | Spring Beans, `@Component`, `@Service`, `@Repository` | 2.5h |
| Miércoles | Spring Boot: AutoConfiguration, `application.properties`, profiles | 2.5h |
| Jueves | Primera REST API: `@RestController`, `@GetMapping`, `@PostMapping` | 3h |
| Viernes | `@RequestBody`, `@PathVariable`, `@RequestParam`, `ResponseEntity` | 2.5h |
| Sábado | DTOs: qué son, por qué usarlos, MapStruct | 2.5h |
| Domingo | Validaciones: `@Valid`, `@NotNull`, `@Size`, Bean Validation | 2h |

---

### 🏗️ MINI PROYECTO 2 — API REST de Gestión de Tareas (To-Do App)
> **Tecnologías:** Spring Boot, PostgreSQL, DTOs, validaciones  
> **Descripción:** CRUD completo de tareas con usuarios, categorías, estados y paginación.  
> **Git + GitHub + Postman collection incluidos.**

---

## 🗓️ MES 3 — Spring Avanzado + Seguridad + Testing (Semanas 9–12)

---

### 📅 SEMANA 9 — JPA + Hibernate

#### Módulo 10 — Persistencia Profesional
| Día | Tema | Tiempo estimado |
|-----|------|----------------|
| Lunes | ORM: qué es, por qué existe, Hibernate vs JDBC | 2.5h |
| Martes | JPA: `@Entity`, `@Table`, `@Id`, `@GeneratedValue` | 2.5h |
| Miércoles | Relaciones: `@OneToOne`, `@OneToMany`, `@ManyToMany` | 3h |
| Jueves | Lazy vs Eager Loading, N+1 Problem, cómo resolverlo | 3h |
| Viernes | `CascadeType`, `FetchType`, `orphanRemoval` | 2.5h |
| Sábado | Spring Data JPA: `JpaRepository`, queries personalizadas | 2.5h |
| Domingo | JPQL, `@Query`, `@NamedQuery`, Criteria API básico | 2h |

---

### 📅 SEMANA 10 — Spring Boot Avanzado

#### Módulo 12 (avanzado)
| Día | Tema | Tiempo estimado |
|-----|------|----------------|
| Lunes | Manejo global de excepciones: `@ControllerAdvice`, `@ExceptionHandler` | 2.5h |
| Martes | Paginación y ordenamiento: `Pageable`, `Page`, `Sort` | 2.5h |
| Miércoles | Filtros y especificaciones JPA para búsquedas dinámicas | 2.5h |
| Jueves | Interceptors y filtros HTTP | 2h |
| Viernes | Auditoría: `@CreatedDate`, `@LastModifiedDate` | 2h |
| Sábado | Documentación con Swagger / OpenAPI 3 | 2.5h |
| Domingo | `application.yml` avanzado, configuración por entornos | 2h |

---

### 📅 SEMANA 11 — Spring Security + JWT

#### Módulo 13 — Seguridad
| Día | Tema | Tiempo estimado |
|-----|------|----------------|
| Lunes | ¿Qué es la seguridad en APIs? Autenticación vs Autorización | 2h |
| Martes | Spring Security: arquitectura, `SecurityFilterChain` | 3h |
| Miércoles | JWT: estructura, firma, validación, `access token`, `refresh token` | 3h |
| Jueves | Implementar login + registro con JWT completo | 3h |
| Viernes | Roles y permisos: `@PreAuthorize`, `@RolesAllowed` | 2.5h |
| Sábado | CORS, CSRF: qué son y cómo configurarlos | 2h |
| Domingo | Hashing de contraseñas: BCrypt, por qué nunca guardar en texto plano | 2h |

---

### 📅 SEMANA 12 — Testing Profesional

#### Módulo 14 — Testing
| Día | Tema | Tiempo estimado |
|-----|------|----------------|
| Lunes | ¿Por qué hacer pruebas? Pirámide de testing | 2h |
| Martes | JUnit 5: `@Test`, `@BeforeEach`, `@AfterEach`, assertions | 2.5h |
| Miércoles | Mockito: `@Mock`, `@InjectMocks`, `when().thenReturn()` | 2.5h |
| Jueves | Testing de servicios y repositorios | 2.5h |
| Viernes | Integration Tests: `@SpringBootTest`, `@WebMvcTest` | 3h |
| Sábado | Testcontainers: PostgreSQL real en tests | 2.5h |
| Domingo | TDD: desarrollo guiado por tests, cobertura de código | 2h |

---

### 🏗️ PROYECTO 3 — Sistema de E-commerce (Backend completo)
> **Tecnologías:** Spring Boot, Spring Security, JWT, JPA, PostgreSQL, Swagger  
> **Descripción:** API completa con usuarios, productos, categorías, carrito, pedidos, autenticación JWT, roles (admin/cliente), paginación, documentación.  
> **Incluye:** Tests unitarios, tests de integración, README profesional, Git/GitHub.

---

### 🏗️ PROYECTO 4 — Sistema de Autenticación y Autorización Empresarial
> **Tecnologías:** Spring Security, JWT, Refresh Tokens, OAuth2 básico  
> **Descripción:** Microservicio de auth con registro, login, logout, refresh token, roles dinámicos y auditoría.

---

## 🗓️ MES 4 — Nivel Empresa + Portafolio + Empleo (Semanas 13–16)

---

### 📅 SEMANA 13 — Concurrencia + Redis + Performance

#### Módulo 7 + Módulo 18
| Día | Tema | Tiempo estimado |
|-----|------|----------------|
| Lunes | Threads, concurrencia, race conditions, `synchronized` | 2.5h |
| Martes | `ExecutorService`, `CompletableFuture`, programación asíncrona | 2.5h |
| Miércoles | Virtual Threads (Java 21 - Project Loom) | 2h |
| Jueves | Caché con Redis: qué es, cuándo usarlo, Spring Cache | 3h |
| Viernes | Optimización de APIs: lazy loading, N+1, índices DB | 2.5h |
| Sábado | JVM Tuning básico: GC, profiling con VisualVM | 2h |
| Domingo | Repaso y ejercicios de performance | 2h |

---

### 📅 SEMANA 14 — Microservicios + Mensajería

#### Módulo 15 — Microservicios
| Día | Tema | Tiempo estimado |
|-----|------|----------------|
| Lunes | ¿Qué son los microservicios? Monolito vs Microservicios | 2.5h |
| Martes | Spring Cloud: Config Server, Service Discovery (Eureka) | 2.5h |
| Miércoles | API Gateway con Spring Cloud Gateway | 2.5h |
| Jueves | OpenFeign: comunicación entre servicios | 2h |
| Viernes | Circuit Breaker: Resilience4j | 2h |
| Sábado | RabbitMQ: mensajería asíncrona entre servicios | 2.5h |
| Domingo | Apache Kafka: eventos, topics, producers, consumers | 3h |

---

### 📅 SEMANA 15 — Docker + Despliegue + Observabilidad

#### Módulo 16 — DevOps básico para Backend
| Día | Tema | Tiempo estimado |
|-----|------|----------------|
| Lunes | Docker: imágenes, contenedores, `Dockerfile` | 2.5h |
| Martes | Docker Compose: orquestar app + DB + Redis | 2.5h |
| Miércoles | Desplegar Spring Boot en Docker | 2h |
| Jueves | Spring Boot Actuator: health, metrics, info | 2h |
| Viernes | Prometheus + Grafana: monitoreo visual | 2.5h |
| Sábado | GitHub Actions: CI/CD básico para tu proyecto | 2.5h |
| Domingo | Intro a Kubernetes: pods, deployments, services (conceptual) | 2h |

---

### 📅 SEMANA 16 — Arquitectura + Portafolio + Entrevistas

#### Módulo 17 + Módulo 20
| Día | Tema | Tiempo estimado |
|-----|------|----------------|
| Lunes | Arquitectura en capas vs Hexagonal vs Clean Architecture | 3h |
| Martes | DDD básico: entidades, value objects, aggregates | 2.5h |
| Miércoles | Patrones de diseño GoF: Singleton, Factory, Builder, Strategy, Observer | 3h |
| Jueves | CV técnico para Java Developer + LinkedIn optimizado | 2h |
| Viernes | GitHub Profile + README de proyectos nivel empresa | 2h |
| Sábado | Entrevistas técnicas: preguntas frecuentes, cómo responder | 2.5h |
| Domingo | Mock Interview: simulacro completo + corrección | 2.5h |

---

### 🏗️ PROYECTO 5 — Sistema Bancario Simplificado
> **Tecnologías:** Spring Boot, JPA, PostgreSQL, JWT, Redis, Docker  
> **Descripción:** Cuentas, transferencias, historial, saldo, autenticación robusta, caché y despliegue con Docker Compose.

---

### 🏗️ PROYECTO FINAL — Plataforma de Gestión Empresarial (Proyecto Integrador)
> **Tecnologías:** Spring Boot, Microservicios, JPA, PostgreSQL, Redis, Kafka, JWT, Docker, GitHub Actions  
> **Descripción:** Sistema completo con múltiples módulos (usuarios, inventario, pedidos, notificaciones), Clean Architecture, documentación OpenAPI, tests, CI/CD y despliegue.  
> **Este proyecto va al centro de tu portafolio.**

---

## 📚 Recursos Obligatorios

### Libros
| Libro | Cuándo leerlo |
|-------|--------------|
| *Effective Java* — Joshua Bloch | Desde mes 2 |
| *Spring in Action* — Craig Walls | Mes 2–3 |
| *Java Concurrency in Practice* — Goetz | Mes 3–4 |
| *Clean Code* — Robert C. Martin | Desde mes 1 |
| *Designing Data-Intensive Applications* — Kleppmann | Mes 4 |

### Documentación oficial (siempre vigente)
- [docs.oracle.com/en/java](https://docs.oracle.com/en/java/)
- [spring.io/docs](https://spring.io/docs)
- [hibernate.org/documentation](https://hibernate.org/documentation/)

---

## 🏆 Los 20 Proyectos del Portafolio

| # | Proyecto | Módulos que usa | Mes |
|---|---------|----------------|-----|
| 1 | Sistema de inventario (consola) | POO, colecciones | 1 |
| 2 | To-Do API REST básica | Spring Boot, PostgreSQL | 2 |
| 3 | E-commerce backend completo | Spring, JPA, JWT, Swagger | 3 |
| 4 | Sistema de autenticación empresarial | Spring Security, OAuth2 | 3 |
| 5 | Sistema bancario simplificado | Redis, Docker, seguridad | 4 |
| 6 | Proyecto final integrador | Todo | 4 |
| 7 | Sistema hospitalario | JPA avanzado, roles | Bonus |
| 8 | Plataforma de reservas de hotel | Concurrencia, Redis | Bonus |
| 9 | Sistema de pedidos para restaurante | Kafka, microservicios | Bonus |
| 10 | Plataforma de cursos online | S3, CDN, archivos | Bonus |
| 11 | Acortador de URLs | Redis, estadísticas | Bonus |
| 12 | Red social simplificada | Grafos, relaciones complejas | Bonus |
| 13 | Chat en tiempo real | WebSockets | Bonus |
| 14 | Sistema de notificaciones | Kafka, email, push | Bonus |
| 15 | API para aplicación móvil | REST avanzado, JWT | Bonus |
| 16 | Plataforma de pagos simulada | Transacciones, idempotencia | Bonus |
| 17 | Marketplace con microservicios | Spring Cloud completo | Bonus |
| 18 | Sistema de gestión de tareas avanzado | Equipos, permisos | Bonus |
| 19 | Backend con Clean Architecture + DDD | Arquitectura avanzada | Bonus |
| 20 | Proyecto desplegado en AWS/GCP | Cloud, K8s, CI/CD | Bonus |

---

## ⏱️ Estimación de Carga Horaria Total

| Componente | Horas |
|------------|-------|
| Clases teóricas | ~180h |
| Proyectos | ~120h |
| Ejercicios y práctica | ~80h |
| Exámenes y revisiones | ~20h |
| **TOTAL** | **~400 horas** |

> A 2.5 horas/día × 5 días/semana × 16 semanas = **~200 horas mínimas**  
> Recomendado: incluir fines de semana parcialmente para proyectos.

---

## 📋 Reglas del Programa

1. ✅ No avanzamos si no entendiste el tema anterior
2. ✅ Cada módulo tiene su examen antes de continuar
3. ✅ Todos los proyectos van a GitHub con README profesional
4. ✅ Código revisado y corregido antes de pasar al siguiente
5. ✅ Preguntas de entrevista al final de cada módulo
6. ✅ Las dudas se hacen antes de continuar, no después
7. ✅ Java 21 LTS es nuestra versión base (con features modernas)

---

*Programa diseñado específicamente para Harold — Java Backend Developer Track — 2025*
