package app.studyhub.config;

import app.studyhub.domain.User;
import app.studyhub.domain.Course;
import app.studyhub.domain.Lesson;
import app.studyhub.domain.Event;
import app.studyhub.domain.Note;
import app.studyhub.domain.CourseStatus;
import app.studyhub.infrastructure.UserRepository;
import app.studyhub.infrastructure.CourseRepository;
import app.studyhub.infrastructure.LessonRepository;
import app.studyhub.infrastructure.EventRepository;
import app.studyhub.infrastructure.NoteRepository;
import app.studyhub.domain.ScheduleItem;
import app.studyhub.infrastructure.ScheduleItemRepository;
import app.studyhub.application.ScheduleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Configuration
class DevelopmentSeed {
    @Bean
    CommandLineRunner seed(
        UserRepository users,
        CourseRepository courses,
        LessonRepository lessons,
        EventRepository events,
        NoteRepository noteRepository,
        ScheduleItemRepository scheduleItemRepository,
        ScheduleService scheduleService,
        PasswordEncoder encoder
    ) {
        return a -> {
            // 1. Seed user
            User user = users.findByEmail("harold@studyhub.local").orElseGet(() -> {
                User u = new User();
                u.setEmail("harold@studyhub.local");
                u.setName("Harold");
                u.setPasswordHash(encoder.encode("ChangeMe123!"));
                return users.save(u);
            });

            // 2. Clean up unwanted courses
            courses.findByUserEmail(user.getEmail()).stream()
                .filter(c -> !c.getTitle().equalsIgnoreCase("Java") 
                          && !c.getTitle().equalsIgnoreCase("Inglés") 
                          && !c.getTitle().equalsIgnoreCase("Data Analyst")
                          && !c.getTitle().equalsIgnoreCase("Javascript")
                          && !c.getTitle().equalsIgnoreCase("Data Engineer"))
                .forEach(c -> {
                    System.out.println("Eliminando curso no deseado: " + c.getTitle());
                    List<Lesson> cLessons = lessons.findByCourseIdOrderByCreatedAtAsc(c.getId());
                    lessons.deleteAll(cLessons);
                    courses.delete(c);
                });

            // 3. Seed active courses
            seedJava(user, courses, lessons);
            seedEnglish(user, courses, lessons);
            seedDataEngineer(user, courses, lessons);
            seedDataAnalyst(user, courses, lessons);
            seedJavascript(user, courses, lessons);

            // 4. Seed Recovered Notes (from lost H2 memory state)
            seedRecoveredNotes(user, courses, lessons, noteRepository);

            // 5. Seed Weekly Schedule template if empty
            List<ScheduleItem> currentSchedule = scheduleItemRepository.findByUserEmail(user.getEmail());
            if (currentSchedule.isEmpty()) {
                System.out.println("Sembrando horario semanal por defecto...");
                String[] days = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
                for (String day : days) {
                    // Data Engineer (08:00 - 11:00)
                    ScheduleItem de = new ScheduleItem();
                    de.setDayOfWeek(day);
                    de.setStartTime("08:00");
                    de.setEndTime("11:00");
                    de.setCourseTitle("Data Engineer");
                    de.setColor("#ffa502");
                    de.setUser(user);
                    scheduleItemRepository.save(de);

                    // Java Backend (11:30 - 14:30)
                    ScheduleItem java = new ScheduleItem();
                    java.setDayOfWeek(day);
                    java.setStartTime("11:30");
                    java.setEndTime("14:30");
                    java.setCourseTitle("Java");
                    java.setColor("#7257e8");
                    java.setUser(user);
                    scheduleItemRepository.save(java);

                    // Data Analyst (15:00 - 17:00)
                    ScheduleItem da = new ScheduleItem();
                    da.setDayOfWeek(day);
                    da.setStartTime("15:00");
                    da.setEndTime("17:00");
                    da.setCourseTitle("Data Analyst");
                    da.setColor("#3f80ea");
                    da.setUser(user);
                    scheduleItemRepository.save(da);

                    // Javascript (17:00 - 18:30)
                    ScheduleItem js = new ScheduleItem();
                    js.setDayOfWeek(day);
                    js.setStartTime("17:00");
                    js.setEndTime("18:30");
                    js.setCourseTitle("Javascript");
                    js.setColor("#f7df1e");
                    js.setUser(user);
                    scheduleItemRepository.save(js);

                    // Inglés (18:30 - 20:00)
                    ScheduleItem eng = new ScheduleItem();
                    eng.setDayOfWeek(day);
                    eng.setStartTime("18:30");
                    eng.setEndTime("20:00");
                    eng.setCourseTitle("Inglés");
                    eng.setColor("#ff4757");
                    eng.setUser(user);
                    scheduleItemRepository.save(eng);
                }
            }

            // Sync schedule to calendar events
            scheduleService.syncCalendarEvents(user.getEmail());
        };
    }


    private void createEvent(User user, EventRepository eventRepository, String title, String desc, LocalDateTime dt, String color) {
        Event e = new Event();
        e.setTitle(title);
        e.setDescription(desc);
        e.setDateTime(dt);
        e.setColor(color);
        e.setUser(user);
        eventRepository.save(e);
    }

    private void seedJava(User user, CourseRepository courses, LessonRepository lessons) {
        Course course = courses.findByUserEmail(user.getEmail()).stream()
            .filter(c -> c.getCode().equalsIgnoreCase("CJ-300") || c.getTitle().equalsIgnoreCase("Java"))
            .findFirst()
            .orElseGet(() -> {
                Course c = new Course();
                c.setTitle("Java");
                c.setCode("CJ-300");
                c.setProfessor("Antigravity");
                c.setUniversity("Universidad");
                c.setPlatform("Local");
                c.setColor("#7257e8");
                c.setIcon("✦");
                c.setUser(user);
                c.setStatus(CourseStatus.NOT_STARTED);
                return courses.save(c);
            });

        try (InputStream is = getClass().getResourceAsStream("/syllabi/java.md")) {
            if (is != null) {
                System.out.println("Cargando sílabo de Java Backend desde recursos...");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    String line;
                    String currentWeek = "Semana 1";
                    int count = 0;
                    List<Lesson> existingLessons = lessons.findByCourseIdOrderByCreatedAtAsc(course.getId());
                    LocalDateTime baseDateTime = LocalDateTime.of(2026, 7, 27, 8, 0);
                    int index = 0;

                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.startsWith("### 📅")) {
                            int idx = line.toUpperCase().indexOf("SEMANA");
                            if (idx != -1) {
                                String rest = line.substring(idx);
                                String[] parts = rest.split("\\s+");
                                if (parts.length >= 2) {
                                    currentWeek = "Semana " + parts[1];
                                }
                            }
                        }
                        if (line.startsWith("|") && line.endsWith("|")) {
                            String[] parts = line.split("\\|");
                            if (parts.length >= 3) {
                                String day = parts[1].trim();
                                String topic = parts[2].trim();
                                
                                if (!day.equalsIgnoreCase("Día") && 
                                    !day.startsWith("---") && 
                                    !day.isEmpty() && 
                                    !topic.isEmpty() && 
                                    !topic.startsWith("---")) {
                                    
                                    String lessonTitle = currentWeek + " · " + day + ": " + topic;
                                    if (saveOrUpdateLesson(lessons, course, lessonTitle, baseDateTime.plusMinutes(index), existingLessons)) {
                                        count++;
                                    }
                                    index++;
                                }
                            }
                        }
                    }
                    System.out.println("Se agregaron " + count + " clases nuevas al curso de Java.");
                }
            } else {
                System.err.println("No se encontró el archivo de sílabo de Java en los recursos.");
            }
        } catch (Exception e) {
            System.err.println("Error al leer el archivo de sílabo de Java: " + e.getMessage());
        }
    }

    private void seedEnglish(User user, CourseRepository courses, LessonRepository lessons) {
        Course course = courses.findByUserEmail(user.getEmail()).stream()
            .filter(c -> c.getCode().equalsIgnoreCase("ENG-101") || c.getTitle().equalsIgnoreCase("Inglés"))
            .findFirst()
            .orElseGet(() -> {
                Course c = new Course();
                c.setTitle("Inglés");
                c.setCode("ENG-101");
                c.setProfessor("Antigravity English");
                c.setUniversity("Global");
                c.setPlatform("Local");
                c.setColor("#ff4757");
                c.setIcon("🇬🇧");
                c.setUser(user);
                c.setStatus(CourseStatus.NOT_STARTED);
                return courses.save(c);
            });

        try (InputStream is = getClass().getResourceAsStream("/syllabi/english.md")) {
            if (is != null) {
                System.out.println("Cargando sílabo de Inglés desde recursos...");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    String line;
                    String currentWeek = "Semana 1";
                    int count = 0;
                    List<Lesson> existingLessons = lessons.findByCourseIdOrderByCreatedAtAsc(course.getId());
                    LocalDateTime baseDateTime = LocalDateTime.of(2026, 7, 27, 8, 0);
                    int index = 0;

                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.toUpperCase().startsWith("### SEMANA")) {
                            String clean = line.replace("#", "").trim();
                            int dashIdx = clean.indexOf("—");
                            if (dashIdx != -1) {
                                currentWeek = clean.substring(0, dashIdx).trim();
                            } else {
                                currentWeek = clean;
                            }
                        }
                        if (line.startsWith("|") && line.endsWith("|")) {
                            String[] parts = line.split("\\|");
                            if (parts.length >= 4) {
                                String day = parts[1].trim().replace("**", "");
                                String topic = parts[2].trim().replace("✅", "").replace("❌", "").trim();
                                
                                if (day.toLowerCase().contains("clase") && 
                                    !day.equalsIgnoreCase("Clase") && 
                                    !day.startsWith("---") && 
                                    !topic.isEmpty() && 
                                    !topic.startsWith("---")) {
                                    
                                    String lessonTitle = currentWeek + " · " + day + ": " + topic;
                                    if (saveOrUpdateLesson(lessons, course, lessonTitle, baseDateTime.plusMinutes(index), existingLessons)) {
                                        count++;
                                    }
                                    index++;
                                }
                            }
                        }
                    }
                    System.out.println("Se agregaron " + count + " clases nuevas al curso de Inglés.");
                }
            } else {
                System.err.println("No se encontró el archivo de sílabo de Inglés en los recursos.");
            }
        } catch (Exception e) {
            System.err.println("Error al leer el archivo de sílabo de Inglés: " + e.getMessage());
        }
    }

    private void seedDataEngineer(User user, CourseRepository courses, LessonRepository lessons) {
        Course course = courses.findByUserEmail(user.getEmail()).stream()
            .filter(c -> c.getCode().equalsIgnoreCase("DE-301") || c.getTitle().equalsIgnoreCase("Data Engineer"))
            .findFirst()
            .orElseGet(() -> {
                Course c = new Course();
                c.setTitle("Data Engineer");
                c.setCode("DE-301");
                c.setProfessor("Antigravity DE");
                c.setUniversity("StudyHub Academy");
                c.setPlatform("Local");
                c.setColor("#ffa502");
                c.setIcon("🛠️");
                c.setUser(user);
                c.setStatus(CourseStatus.NOT_STARTED);
                return courses.save(c);
            });

        try (InputStream is = getClass().getResourceAsStream("/syllabi/data_engineering.md")) {
            if (is != null) {
                System.out.println("Cargando sílabo de Data Engineer desde recursos...");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    String line;
                    String currentModule = "Módulo 0";
                    String currentWeek = "";
                    int count = 0;
                    List<Lesson> existingLessons = lessons.findByCourseIdOrderByCreatedAtAsc(course.getId());
                    LocalDateTime baseDateTime = LocalDateTime.of(2026, 7, 27, 8, 0);
                    int index = 0;

                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.startsWith("## MÓDULO") || line.startsWith("## Módulo")) {
                            String clean = line.replace("#", "").trim();
                            int dashIdx = clean.indexOf("—");
                            if (dashIdx != -1) {
                                currentModule = clean.substring(0, dashIdx).trim();
                            } else {
                                currentModule = clean;
                            }
                            currentWeek = "";
                        }
                        if (line.startsWith("### ")) {
                            String sub = line.substring(4).trim();
                            if (sub.toUpperCase().startsWith("SEMANA") || sub.toUpperCase().startsWith("TEMAS")) {
                                int dashIdx = sub.indexOf("—");
                                if (dashIdx != -1) {
                                    currentWeek = sub.substring(0, dashIdx).trim();
                                } else {
                                    currentWeek = sub;
                                }
                            }
                        }
                        if (currentModule.equalsIgnoreCase("Módulo 0") && line.startsWith("- ")) {
                            String topic = line.substring(2).trim();
                            if (!topic.isEmpty()) {
                                String lessonTitle = "Módulo 0 · " + topic;
                                if (saveOrUpdateLesson(lessons, course, lessonTitle, baseDateTime.plusMinutes(index), existingLessons)) {
                                    count++;
                                }
                                index++;
                            }
                        }
                        if (line.startsWith("|") && line.endsWith("|")) {
                            String[] parts = line.split("\\|");
                            if (parts.length >= 3) {
                                String col1 = parts[1].trim();
                                String col2 = parts[2].trim();
                                if (!col1.equalsIgnoreCase("Tema") && 
                                    !col1.startsWith("---") && 
                                    !col1.isEmpty() && 
                                    !col2.isEmpty() && 
                                    !col2.startsWith("---")) {
                                    
                                    String weekPrefix = currentWeek.isEmpty() ? "" : currentWeek + " · ";
                                    String lessonTitle = currentModule + " · " + weekPrefix + col1 + ": " + col2;
                                    if (lessonTitle.length() > 200) {
                                        lessonTitle = lessonTitle.substring(0, 197) + "...";
                                    }
                                    if (saveOrUpdateLesson(lessons, course, lessonTitle, baseDateTime.plusMinutes(index), existingLessons)) {
                                        count++;
                                    }
                                    index++;
                                }
                            }
                        }
                    }
                    System.out.println("Se agregaron " + count + " clases nuevas al curso de Data Engineer.");
                }
            } else {
                System.err.println("No se encontró el archivo de sílabo de Data Engineer en los recursos.");
            }
        } catch (Exception e) {
            System.err.println("Error al leer el archivo de sílabo de Data Engineer: " + e.getMessage());
        }
    }

    private Note getOrCreateSeededNote(NoteRepository noteRepository, Lesson lesson, Course course, User user) {
        List<Note> notes = noteRepository.findByLessonIdAndUserEmail(lesson.getId(), user.getEmail());
        if (!notes.isEmpty()) {
            return notes.get(0);
        }
        Note n = new Note();
        n.setLessonId(lesson.getId());
        n.setCourseId(course.getId());
        n.setUser(user);
        return n;
    }

    private void seedRecoveredNotes(User user, CourseRepository courses, LessonRepository lessons, NoteRepository noteRepository) {
        System.out.println("Recuperando y sembrando apuntes previos del usuario...");
        Course javaCourse = courses.findByUserEmail(user.getEmail()).stream()
            .filter(c -> c.getTitle().equalsIgnoreCase("Java"))
            .findFirst()
            .orElse(null);
        if (javaCourse == null) return;
        List<Lesson> javaLessons = lessons.findByCourseIdOrderByCreatedAtAsc(javaCourse.getId());

        // 1. Lunes Note
        javaLessons.stream()
            .filter(l -> l.getTitle().contains("¿Qué es un Backend?"))
            .findFirst()
            .ifPresent(lesson -> {
                Note note = getOrCreateSeededNote(noteRepository, lesson, javaCourse, user);
                if (note.getBody() == null || note.getBody().trim().isEmpty()) {
                    note.setTitle("Apuntes: Semana 1 · Lunes: ¿Qué es un Backend? ¿Qué hace un Java Dev?");
                    note.setBody(
                        "# ☕ ¿Qué es el Backend?\n\n" +
                        "Cuando usas una aplicación móvil o web, ves la interfaz (Frontend). Pero detrás hay un servidor (Backend) procesando tus acciones.\n\n" +
                        "Un **Java Backend Developer** diseña, construye y mantiene la parte del servidor de una aplicación usando **Java** como lenguaje principal.\n\n" +
                        "## Sus responsabilidades concretas son:\n\n" +
                        "| Responsabilidad | Qué significa en la práctica |\n" +
                        "|---|---|\n" +
                        "| **Diseñar APIs REST** | Crear las \"puertas\" por donde el Frontend pide y recibe datos. |\n" +
                        "| **Modelar bases de datos** | Diseñar cómo se guardan los datos (tablas, relaciones). |\n" +
                        "| **Escribir lógica de negocio** | Implementar las reglas del sistema. |\n" +
                        "| **Garantizar seguridad** | Que solo usuarios autorizados accedan a la información. |"
                    );
                    note.setScratchpad(
                        "En Java, una operación entre dos int siempre produce un int. Para obtener decimales, al menos uno de los operandos debe ser double o debes hacer un cast.\n\n" +
                        "Imagina un ODÓMETRO de auto (el contador de kilómetros).\n" +
                        "El odómetro más básico tiene 3 dígitos: va de 000 a 999.\n" +
                        "¿Qué pasa cuando llega a 999 y avanzas 1 km más?\n" +
                        "→ No puede mostrar 1000 porque solo tiene 3 dígitos.\n" +
                        "→ Da la vuelta y muestra: 000\n" +
                        "Eso es exactamente el OVERFLOW en Java."
                    );
                    note.setLastModified(LocalDateTime.now());
                    noteRepository.save(note);
                    System.out.println("Apunte de Lunes recuperado y sembrado.");
                }
            });

        // 2. Martes Note
        javaLessons.stream()
            .filter(l -> l.getTitle().contains("Cómo trabaja con Frontend"))
            .findFirst()
            .ifPresent(lesson -> {
                Note note = getOrCreateSeededNote(noteRepository, lesson, javaCourse, user);
                if (note.getBody() == null || note.getBody().trim().isEmpty()) {
                    note.setTitle("Apuntes: Semana 1 · Martes: Cómo trabaja con Frontend, DevOps, QA, PM");
                    note.setBody(
                        "## 🤝 Con el equipo de Frontend\n\n" +
                        "El Frontend y el Backend se comunican constantemente para definir el contrato de API. El Frontend necesita saber qué campos va a recibir y en qué formato.\n\n" +
                        "### Colaboración con otros roles:\n" +
                        "* **DevOps**: Configura los pipelines de integración y despliegue continuo (CI/CD) para que nuestro código compile y se publique automáticamente.\n" +
                        "* **QA (Quality Assurance)**: Prueba nuestras APIs para reportar fallos y validar que la lógica de negocio funcione.\n" +
                        "* **PM (Product Manager)**: Define los requisitos de negocio y prioriza las tareas en el tablero."
                    );
                    note.setScratchpad("");
                    note.setLastModified(LocalDateTime.now());
                    noteRepository.save(note);
                    System.out.println("Apunte de Martes recuperado y sembrado.");
                }
            });

        // 3. Miércoles Note
        javaLessons.stream()
            .filter(l -> l.getTitle().contains("¿Cómo es un día real"))
            .findFirst()
            .ifPresent(lesson -> {
                Note note = getOrCreateSeededNote(noteRepository, lesson, javaCourse, user);
                if (note.getBody() == null || note.getBody().trim().isEmpty()) {
                    note.setTitle("Apuntes: Semana 1 · Miércoles: ¿Cómo es un día real de trabajo en empresa?");
                    note.setBody(
                        "## ¿Cómo es un día real de trabajo?\n\n" +
                        "### 💻 Oficina y rutina del Developer\n" +
                        "* **09:30 — Daily Standup**: Reunión corta (15 min) para contar en qué trabajaste ayer, qué harás hoy y si tienes bloqueos.\n" +
                        "* **10:00 — Focus Time**: Escribir código, diseñar endpoints, resolver bugs y documentar.\n" +
                        "* **14:00 — Code Review**: Revisar el código de tus compañeros y dar feedback constructivo.\n" +
                        "* **16:00 — Reunión técnica**: Coordinar contratos de API o arquitectura del sistema."
                    );
                    note.setScratchpad("");
                    note.setLastModified(LocalDateTime.now());
                    noteRepository.save(note);
                    System.out.println("Apunte de Miércoles recuperado y sembrado.");
                }
            });

        // 4. Jueves Note
        javaLessons.stream()
            .filter(l -> l.getTitle().contains("Ecosistema Java"))
            .findFirst()
            .ifPresent(lesson -> {
                Note note = getOrCreateSeededNote(noteRepository, lesson, javaCourse, user);
                if (note.getBody() == null || note.getBody().trim().isEmpty()) {
                    note.setTitle("Apuntes: Semana 1 · Jueves: Ecosistema Java: JDK, JRE, JVM, bytecode");
                    note.setBody(
                        "## El stack tecnológico que dominarás\n\n" +
                        "* **JDK (Java Development Kit)**: Contiene todo lo necesario para desarrollar con Java (compilador `javac`, debugger, APIs).\n" +
                        "* **JRE (Java Runtime Environment)**: Contiene lo necesario para ejecutar aplicaciones (JVM y librerías estándar).\n" +
                        "* **JVM (Java Virtual Machine)**: La máquina virtual que ejecuta el **bytecode** generado por el compilador, permitiendo la portabilidad (\"Escribe una vez, ejecuta en cualquier lado\")."
                    );
                    note.setScratchpad("");
                    note.setLastModified(LocalDateTime.now());
                    noteRepository.save(note);
                    System.out.println("Apunte de Jueves recuperado y sembrado.");
                }
            });

        // 5. Viernes Note
        javaLessons.stream()
            .filter(l -> l.getTitle().contains("Instalación y configuración") || l.getTitle().contains("Instalacion y configuracion"))
            .findFirst()
            .ifPresent(lesson -> {
                Note note = getOrCreateSeededNote(noteRepository, lesson, javaCourse, user);
                if (note.getBody() == null || note.getBody().trim().isEmpty()) {
                    note.setTitle("Apuntes: Semana 1 · Viernes: Buenas Practicas y errores comunes");
                    note.setBody(
                        "## Buenas prácticas del rol\n\n" +
                        "* **Comunica antes de codear** – Si algo no está claro, pregunta al PM antes de invertir horas en algo incorrecto.\n" +
                        "* **Documenta tus APIs** – Swagger es tu mejor amigo. Un Frontend que no puede entender tu API es tiempo perdido.\n" +
                        "* **Escribe tests desde el inicio** – No \"al final cuando haya tiempo\". Nunca hay tiempo después.\n" +
                        "* **Código limpio sobre código rápido** – El código se lee más veces de las que se escribe.\n" +
                        "* **Nunca hagas deploy un viernes** – Regla no escrita de la industria 😂\n\n" +
                        "## Errores comunes del Junior\n\n" +
                        "| Error | Consecuencia | Cómo evitarlo |\n" +
                        "| :--- | :--- | :--- |\n" +
                        "| **Empezar a codear sin entender el requisito** | Rehacer todo el trabajo | Siempre pide claridad primero |\n" +
                        "| **No escribir tests** | Bugs en producción constantes | TDD o tests después, pero siempre |"
                    );
                    note.setScratchpad("");
                    note.setLastModified(LocalDateTime.now());
                    noteRepository.save(note);
                    System.out.println("Apunte de Viernes recuperado y sembrado.");
                }
            });
    }

    private void seedDataAnalyst(User user, CourseRepository courses, LessonRepository lessons) {
        Course course = courses.findByUserEmail(user.getEmail()).stream()
            .filter(c -> c.getCode().equalsIgnoreCase("DA-201") || c.getTitle().equalsIgnoreCase("Data Analyst"))
            .findFirst()
            .orElseGet(() -> {
                Course c = new Course();
                c.setTitle("Data Analyst");
                c.setCode("DA-201");
                c.setProfessor("Antigravity DA");
                c.setUniversity("StudyHub Academy");
                c.setPlatform("Local");
                c.setColor("#3f80ea");
                c.setIcon("📊");
                c.setUser(user);
                c.setStatus(CourseStatus.NOT_STARTED);
                return courses.save(c);
            });

        try (InputStream is = getClass().getResourceAsStream("/syllabi/data_analyst.md")) {
            if (is != null) {
                System.out.println("Cargando sílabo de Data Analyst desde recursos...");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    String line;
                    String currentModule = "Módulo 1";
                    String currentWeek = "Semana 1";
                    int count = 0;
                    List<Lesson> existingLessons = lessons.findByCourseIdOrderByCreatedAtAsc(course.getId());
                    LocalDateTime baseDateTime = LocalDateTime.of(2026, 7, 27, 8, 0);
                    int index = 0;

                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.startsWith("# ") && (line.contains("MÓDULO") || line.contains("Módulo"))) {
                            String clean = line.replace("#", "").replaceAll("[🟦🟩🟨🟧🟥🟪🔵🏆🟫]", "").trim();
                            int colonIdx = clean.indexOf(":");
                            if (colonIdx != -1) {
                                currentModule = clean.substring(0, colonIdx).trim();
                            } else {
                                currentModule = clean;
                            }
                        }
                        if (line.startsWith("### ")) {
                            currentWeek = line.substring(4).trim();
                        }
                        if (line.startsWith("|") && line.endsWith("|")) {
                            String[] parts = line.split("\\|");
                            if (parts.length >= 6) {
                                String sesion = parts[1].trim();
                                String tema = parts[2].trim();
                                if (!sesion.equalsIgnoreCase("Sesión") && !sesion.startsWith("---") && !sesion.isEmpty() && !tema.isEmpty()) {
                                    String lessonTitle = currentModule + " · " + currentWeek + " · Sesión " + sesion + ": " + tema;
                                    if (lessonTitle.length() > 200) {
                                        lessonTitle = lessonTitle.substring(0, 197) + "...";
                                    }
                                    if (saveOrUpdateLesson(lessons, course, lessonTitle, baseDateTime.plusMinutes(index), existingLessons)) {
                                        count++;
                                    }
                                    index++;
                                }
                            }
                        }
                    }
                    System.out.println("Se agregaron " + count + " clases nuevas al curso de Data Analyst.");
                }
            } else {
                System.err.println("No se encontró el archivo de sílabo de Data Analyst en los recursos.");
            }
        } catch (Exception e) {
            System.err.println("Error al leer el archivo de sílabo de Data Analyst: " + e.getMessage());
        }
    }

    private void seedJavascript(User user, CourseRepository courses, LessonRepository lessons) {
        Course course = courses.findByUserEmail(user.getEmail()).stream()
            .filter(c -> c.getCode().equalsIgnoreCase("JS-101") || c.getTitle().equalsIgnoreCase("Javascript"))
            .findFirst()
            .orElseGet(() -> {
                Course c = new Course();
                c.setTitle("Javascript");
                c.setCode("JS-101");
                c.setProfessor("Antigravity JS");
                c.setUniversity("StudyHub Academy");
                c.setPlatform("Local");
                c.setColor("#f7df1e");
                c.setIcon("💛");
                c.setUser(user);
                c.setStatus(CourseStatus.NOT_STARTED);
                return courses.save(c);
            });

        try (InputStream is = getClass().getResourceAsStream("/syllabi/javascript.md")) {
            if (is != null) {
                System.out.println("Cargando sílabo de Javascript desde recursos...");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    String line;
                    String currentWeek = "Semana 1";
                    int count = 0;
                    List<Lesson> existingLessons = lessons.findByCourseIdOrderByCreatedAtAsc(course.getId());
                    LocalDateTime baseDateTime = LocalDateTime.of(2026, 7, 27, 8, 0);
                    int index = 0;

                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.startsWith("*") && line.contains("**Semana")) {
                            int startIdx = line.indexOf("**");
                            int endIdx = line.lastIndexOf("**");
                            if (startIdx != -1 && endIdx != -1 && endIdx > startIdx) {
                                String cleanWeek = line.substring(startIdx + 2, endIdx).trim();
                                int colonIdx = cleanWeek.indexOf(":");
                                if (colonIdx != -1) {
                                    currentWeek = cleanWeek.substring(0, colonIdx).trim();
                                } else {
                                    currentWeek = cleanWeek;
                                }
                            }
                        }
                        if (line.startsWith("*") && line.contains("*") && line.contains(":") && !line.contains("**Semana")) {
                            String clean = line.replace("*", "").trim();
                            if (clean.startsWith("-")) {
                                clean = clean.substring(1).trim();
                            }
                            
                            int colonIdx = clean.indexOf(":");
                            if (colonIdx != -1) {
                                String dayPart = clean.substring(0, colonIdx).trim();
                                String topicPart = clean.substring(colonIdx + 1).trim();
                                
                                if (!dayPart.isEmpty() && !topicPart.isEmpty()) {
                                    String lessonTitle = currentWeek + " · " + dayPart + ": " + topicPart;
                                    if (lessonTitle.length() > 200) {
                                        lessonTitle = lessonTitle.substring(0, 197) + "...";
                                    }
                                    if (saveOrUpdateLesson(lessons, course, lessonTitle, baseDateTime.plusMinutes(index), existingLessons)) {
                                        count++;
                                    }
                                    index++;
                                }
                            }
                        }
                    }
                    System.out.println("Se agregaron " + count + " clases nuevas al curso de Javascript.");
                }
            } else {
                System.err.println("No se encontró el archivo de sílabo de Javascript en los recursos.");
            }
        } catch (Exception e) {
            System.err.println("Error al leer el archivo de sílabo de Javascript: " + e.getMessage());
        }
    }

    private boolean saveOrUpdateLesson(LessonRepository repository, Course course, String title, LocalDateTime createdAt, List<Lesson> existing) {
        Lesson l = existing.stream()
            .filter(exist -> exist.getTitle().equalsIgnoreCase(title))
            .findFirst()
            .orElse(null);
        boolean isNew = false;
        if (l == null) {
            l = new Lesson();
            l.setTitle(title);
            l.setCompleted(false);
            l.setCourse(course);
            isNew = true;
        }
        l.setCreatedAt(createdAt);
        repository.save(l);
        return isNew;
    }
}
