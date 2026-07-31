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
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

            // 2. Clean up Data Engineer if present
            courses.findByUserEmail(user.getEmail()).stream()
                .filter(c -> c.getCode().equalsIgnoreCase("DE-301") || c.getTitle().equalsIgnoreCase("Data Engineer"))
                .findFirst()
                .ifPresent(c -> {
                    System.out.println("Eliminando curso y lecciones de Data Engineer según solicitud del usuario...");
                    List<Lesson> deLessons = lessons.findByCourseId(c.getId());
                    lessons.deleteAll(deLessons);
                    courses.delete(c);
                    System.out.println("Curso de Data Engineer eliminado correctamente.");
                });

            // 3. Seed Java course (original)
            seedJava(user, courses, lessons);

            // 4. Seed other active courses
            seedEnglish(user, courses, lessons);
            seedDataScience(user, courses, lessons);
            seedDataAnalyst(user, courses, lessons);
            seedCloudDevOps(user, courses, lessons);

            // 4.5. Seed Recovered Notes (from lost H2 memory state)
            seedRecoveredNotes(user, courses, lessons, noteRepository);

            // 5. Seed Schedule (Events)
            seedSchedule(user, events);
        };
    }

    private void seedSchedule(User user, EventRepository eventRepository) {
        // Clear previous study events to apply new schedule distribution
        System.out.println("Actualizando horario de estudio en el calendario...");
        List<Event> existingStudyEvents = eventRepository.findByUserEmailOrderByDateTimeAsc(user.getEmail()).stream()
            .filter(e -> e.getTitle().startsWith("Estudio:"))
            .toList();
        eventRepository.deleteAll(existingStudyEvents);

        LocalDate startDate = LocalDate.of(2026, 7, 27); // Lunes, 27 de Julio 2026
        int count = 0;
        for (int w = 0; w < 4; w++) {
            for (int d = 0; d < 5; d++) { // Lunes a Viernes
                LocalDate date = startDate.plusWeeks(w).plusDays(d);

                // Data Science (Lunes a Viernes 08:30 - 10:30)
                createEvent(user, eventRepository, "Estudio: Data Science", 
                    "Revisar temas de scikit-learn, álgebra lineal, PyTorch o EDA.", 
                    date.atTime(8, 30), "#2ed573");
                count++;

                // Data Analyst (Lunes a Viernes 14:00 - 16:00)
                createEvent(user, eventRepository, "Estudio: Data Analyst", 
                    "Práctica de Excel avanzado, consultas SQL complejas o dashboards interactivos.", 
                    date.atTime(14, 0), "#ffa502");
                count++;

                // Inglés (Lunes a Viernes 18:30 - 20:00)
                createEvent(user, eventRepository, "Estudio: Inglés", 
                    "Inmersión en conversación, ampliación de vocabulario y listening activo.", 
                    date.atTime(18, 30), "#ff4757");
                count++;

                if (d == 0 || d == 2 || d == 4) { // Lunes, Miércoles, Viernes
                    // Java Backend (10:45 - 12:45)
                    createEvent(user, eventRepository, "Estudio: Java Backend", 
                        "Construcción de microservicios con Spring Boot, H2 y seguridad REST.", 
                        date.atTime(10, 45), "#7257e8");
                    count++;

                    // Cloud & DevOps (16:15 - 18:15)
                    createEvent(user, eventRepository, "Estudio: Cloud & DevOps", 
                        "Configuración de contenedores Docker, despliegue en Kubernetes y pipelines de CI/CD.", 
                        date.atTime(16, 15), "#3742fa");
                    count++;
                } else if (d == 1) { // Martes
                    // Data Science (10:45 - 12:45) -> Expandido para priorizarlo
                    createEvent(user, eventRepository, "Estudio: Data Science", 
                        "Profundizar en matemáticas para ML y análisis de algoritmos avanzados.", 
                        date.atTime(10, 45), "#2ed573");
                    count++;

                    // Cloud & DevOps (16:15 - 18:15) -> Expandido
                    createEvent(user, eventRepository, "Estudio: Cloud & DevOps", 
                        "Automatización de infraestructura con Terraform, Ansible y control cloud.", 
                        date.atTime(16, 15), "#3742fa");
                    count++;
                } else if (d == 3) { // Jueves
                    // Java Backend (10:45 - 12:45) -> Expandido
                    createEvent(user, eventRepository, "Estudio: Java Backend", 
                        "Especialización en patrones de diseño arquitectónicos y testing unitario.", 
                        date.atTime(10, 45), "#7257e8");
                    count++;

                    // Data Science (16:15 - 18:15) -> Expandido para priorizarlo
                    createEvent(user, eventRepository, "Estudio: Data Science", 
                        "Práctica e implementación de Deep Learning y modelos de lenguaje (LLMs).", 
                        date.atTime(16, 15), "#2ed573");
                    count++;
                }
            }
        }
        System.out.println("Se registraron " + count + " bloques de estudio actualizados en el calendario.");
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

        File file = new File("C:/Users/Harold/Downloads/silabo_java_backend.md");
        if (file.exists()) {
            System.out.println("Cargando sílabo de Java Backend desde: " + file.getAbsolutePath());
            try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                String line;
                String currentWeek = "Semana 1";
                int count = 0;
                List<Lesson> existingLessons = lessons.findByCourseId(course.getId());

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
                                boolean exists = existingLessons.stream()
                                    .anyMatch(l -> l.getTitle().equalsIgnoreCase(lessonTitle));
                                
                                if (!exists) {
                                    Lesson l = new Lesson();
                                    l.setTitle(lessonTitle);
                                    l.setCompleted(false);
                                    l.setCourse(course);
                                    lessons.save(l);
                                    count++;
                                }
                            }
                        }
                    }
                }
                System.out.println("Se agregaron " + count + " clases nuevas al curso de Java.");
            } catch (Exception e) {
                System.err.println("Error al leer el archivo de sílabo de Java: " + e.getMessage());
            }
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

        File file = new File("C:/Users/Harold/.gemini/antigravity/brain/1b3b9d84-70b5-4b9c-bf53-5757a211f499/syllabus_ingles_2_meses.md");
        if (file.exists()) {
            System.out.println("Cargando sílabo de Inglés desde: " + file.getAbsolutePath());
            try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                String line;
                String currentWeek = "Semana 1";
                int count = 0;
                List<Lesson> existingLessons = lessons.findByCourseId(course.getId());

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
                                boolean exists = existingLessons.stream()
                                    .anyMatch(l -> l.getTitle().equalsIgnoreCase(lessonTitle));
                                
                                if (!exists) {
                                    Lesson l = new Lesson();
                                    l.setTitle(lessonTitle);
                                    l.setCompleted(false);
                                    l.setCourse(course);
                                    lessons.save(l);
                                    count++;
                                }
                            }
                        }
                    }
                }
                System.out.println("Se agregaron " + count + " clases nuevas al curso de Inglés.");
            } catch (Exception e) {
                System.err.println("Error al leer el archivo de sílabo de Inglés: " + e.getMessage());
            }
        }
    }

    private void seedDataScience(User user, CourseRepository courses, LessonRepository lessons) {
        Course course = courses.findByUserEmail(user.getEmail()).stream()
            .filter(c -> c.getCode().equalsIgnoreCase("DS-401") || c.getTitle().equalsIgnoreCase("Data Science"))
            .findFirst()
            .orElseGet(() -> {
                Course c = new Course();
                c.setTitle("Data Science");
                c.setCode("DS-401");
                c.setProfessor("Antigravity DS");
                c.setUniversity("StudyHub Academy");
                c.setPlatform("Local");
                c.setColor("#2ed573");
                c.setIcon("🧪");
                c.setUser(user);
                c.setStatus(CourseStatus.NOT_STARTED);
                return courses.save(c);
            });

        File file = new File("C:/Users/Harold/.gemini/antigravity/brain/0f6ff60c-8e1c-45ce-8330-bb3e890cf4ef/silabo_data_science.md");
        if (file.exists()) {
            System.out.println("Cargando sílabo de Data Science desde: " + file.getAbsolutePath());
            try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                String line;
                String currentModule = "Módulo 0";
                int count = 0;
                List<Lesson> existingLessons = lessons.findByCourseId(course.getId());

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("## 📌 MÓDULO") || line.startsWith("## MÓDULO")) {
                        String clean = line.replace("#", "").replace("📌", "").trim();
                        int dashIdx = clean.indexOf("—");
                        if (dashIdx != -1) {
                            currentModule = clean.substring(0, dashIdx).trim();
                        } else {
                            currentModule = clean;
                        }
                    }
                    if (line.startsWith("|") && line.endsWith("|")) {
                        String[] parts = line.split("\\|");
                        if (parts.length >= 4) {
                            String classNum = parts[1].trim();
                            String topic = parts[2].trim().replace("⚡", "").trim();
                            
                            if (!classNum.equalsIgnoreCase("#") && 
                                !classNum.startsWith("---") && 
                                !classNum.isEmpty() &&
                                !topic.isEmpty() && 
                                !topic.startsWith("---") &&
                                (classNum.contains(".") || classNum.equalsIgnoreCase("📁"))) {
                                
                                String prefix = classNum.equalsIgnoreCase("📁") ? "Proyecto" : "Clase " + classNum;
                                String lessonTitle = currentModule + " · " + prefix + ": " + topic;
                                boolean exists = existingLessons.stream()
                                    .anyMatch(l -> l.getTitle().equalsIgnoreCase(lessonTitle));
                                
                                if (!exists) {
                                    Lesson l = new Lesson();
                                    l.setTitle(lessonTitle);
                                    l.setCompleted(false);
                                    l.setCourse(course);
                                    lessons.save(l);
                                    count++;
                                }
                            }
                        }
                    }
                }
                System.out.println("Se agregaron " + count + " clases nuevas al curso de Data Science.");
            } catch (Exception e) {
                System.err.println("Error al leer el archivo de sílabo de Data Science: " + e.getMessage());
            }
        }
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
                c.setColor("#ffa502");
                c.setIcon("📊");
                c.setUser(user);
                c.setStatus(CourseStatus.NOT_STARTED);
                return courses.save(c);
            });

        File file = new File("C:/Users/Harold/.gemini/antigravity/brain/02bc3140-0dc9-4d5b-9b45-0ce57d95ac02/silabo_dosificado.md");
        if (file.exists()) {
            System.out.println("Cargando sílabo de Data Analyst desde: " + file.getAbsolutePath());
            try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                String line;
                String currentModule = "Módulo 1";
                int count = 0;
                List<Lesson> existingLessons = lessons.findByCourseId(course.getId());

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
                    if (line.startsWith("|") && line.endsWith("|")) {
                        String[] parts = line.split("\\|");
                        if (parts.length >= 6) {
                            String sessionNum = parts[1].trim();
                            String topic = parts[2].trim();
                            
                            if (!sessionNum.equalsIgnoreCase("Sesión") && 
                                !sessionNum.startsWith("---") && 
                                !sessionNum.isEmpty() &&
                                !topic.isEmpty() && 
                                !topic.startsWith("---")) {
                                
                                try {
                                    Integer.parseInt(sessionNum);
                                    String lessonTitle = currentModule + " · Sesión " + sessionNum + ": " + topic;
                                    boolean exists = existingLessons.stream()
                                        .anyMatch(l -> l.getTitle().equalsIgnoreCase(lessonTitle));
                                    
                                    if (!exists) {
                                        Lesson l = new Lesson();
                                        l.setTitle(lessonTitle);
                                        l.setCompleted(false);
                                        l.setCourse(course);
                                        lessons.save(l);
                                        count++;
                                    }
                                } catch (NumberFormatException ignored) {}
                            }
                        }
                    }
                }
                System.out.println("Se agregaron " + count + " clases nuevas al curso de Data Analyst.");
            } catch (Exception e) {
                System.err.println("Error al leer el archivo de sílabo de Data Analyst: " + e.getMessage());
            }
        }
    }

    private void seedCloudDevOps(User user, CourseRepository courses, LessonRepository lessons) {
        Course course = courses.findByUserEmail(user.getEmail()).stream()
            .filter(c -> c.getCode().equalsIgnoreCase("CD-501") || c.getTitle().equalsIgnoreCase("Cloud & DevOps"))
            .findFirst()
            .orElseGet(() -> {
                Course c = new Course();
                c.setTitle("Cloud & DevOps");
                c.setCode("CD-501");
                c.setProfessor("Antigravity Cloud");
                c.setUniversity("StudyHub Academy");
                c.setPlatform("Local");
                c.setColor("#3742fa");
                c.setIcon("☁️");
                c.setUser(user);
                c.setStatus(CourseStatus.NOT_STARTED);
                return courses.save(c);
            });

        File file = new File("C:/Users/Harold/.gemini/antigravity/brain/eabc4e13-2cc9-44bc-99eb-d0617acb9478/silabo_cloud_devops.md");
        if (file.exists()) {
            System.out.println("Cargando sílabo de Cloud & DevOps desde: " + file.getAbsolutePath());
            try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                String line;
                String currentPhase = "Fase 0";
                int count = 0;
                List<Lesson> existingLessons = lessons.findByCourseId(course.getId());

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("## 📍 FASE") || line.startsWith("## FASE")) {
                        String clean = line.replace("#", "").replace("📍", "").trim();
                        int dashIdx = clean.indexOf("—");
                        if (dashIdx != -1) {
                            currentPhase = clean.substring(0, dashIdx).trim();
                        } else {
                            currentPhase = clean;
                        }
                    }
                    if (line.startsWith("|") && line.endsWith("|")) {
                        String[] parts = line.split("\\|");
                        if (parts.length >= 3) {
                            String week = parts[1].trim().replace("**", "");
                            
                            if ((week.startsWith("S") || week.startsWith("P") || week.matches("\\d+")) && 
                                !week.equalsIgnoreCase("Semana") && 
                                !week.equalsIgnoreCase("#") && 
                                !week.startsWith("---") && 
                                !week.isEmpty()) {
                                
                                String topic = "";
                                if (parts.length >= 5) {
                                    String module = parts[2].trim().replace("**", "");
                                    String temas = parts[3].trim();
                                    topic = module + ": " + temas;
                                } else {
                                    topic = parts[2].trim();
                                }
                                
                                if (!topic.isEmpty() && !topic.startsWith("---")) {
                                    String lessonTitle = currentPhase + " · " + week + " · " + topic;
                                    
                                    if (lessonTitle.length() > 250) {
                                        lessonTitle = lessonTitle.substring(0, 247) + "...";
                                    }
                                    
                                    String finalTitle = lessonTitle;
                                    boolean exists = existingLessons.stream()
                                        .anyMatch(l -> l.getTitle().equalsIgnoreCase(finalTitle));
                                    
                                    if (!exists) {
                                        Lesson l = new Lesson();
                                        l.setTitle(lessonTitle);
                                        l.setCompleted(false);
                                        l.setCourse(course);
                                        lessons.save(l);
                                        count++;
                                    }
                                }
                            }
                        }
                    }
                }
                System.out.println("Se agregaron " + count + " clases nuevas al curso de Cloud & DevOps.");
            } catch (Exception e) {
                System.err.println("Error al leer el archivo de sílabo de Cloud & DevOps: " + e.getMessage());
            }
        }
    }

    private void seedRecoveredNotes(User user, CourseRepository courses, LessonRepository lessons, NoteRepository noteRepository) {
        System.out.println("Recuperando y sembrando apuntes previos del usuario...");
        
        // Find Java course
        Course javaCourse = courses.findByUserEmail(user.getEmail()).stream()
            .filter(c -> c.getTitle().equalsIgnoreCase("Java"))
            .findFirst()
            .orElse(null);
            
        if (javaCourse == null) return;
        
        List<Lesson> javaLessons = lessons.findByCourseId(javaCourse.getId());
        
        // 1. Lunes Note
        javaLessons.stream()
            .filter(l -> l.getTitle().contains("¿Qué es un Backend?"))
            .findFirst()
            .ifPresent(lesson -> {
                Note note = noteRepository.findByLessonIdAndUserEmail(lesson.getId(), user.getEmail())
                    .orElseGet(() -> {
                        Note n = new Note();
                        n.setLessonId(lesson.getId());
                        n.setCourseId(javaCourse.getId());
                        n.setUser(user);
                        return n;
                    });
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
                Note note = noteRepository.findByLessonIdAndUserEmail(lesson.getId(), user.getEmail())
                    .orElseGet(() -> {
                        Note n = new Note();
                        n.setLessonId(lesson.getId());
                        n.setCourseId(javaCourse.getId());
                        n.setUser(user);
                        return n;
                    });
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
                Note note = noteRepository.findByLessonIdAndUserEmail(lesson.getId(), user.getEmail())
                    .orElseGet(() -> {
                        Note n = new Note();
                        n.setLessonId(lesson.getId());
                        n.setCourseId(javaCourse.getId());
                        n.setUser(user);
                        return n;
                    });
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
                Note note = noteRepository.findByLessonIdAndUserEmail(lesson.getId(), user.getEmail())
                    .orElseGet(() -> {
                        Note n = new Note();
                        n.setLessonId(lesson.getId());
                        n.setCourseId(javaCourse.getId());
                        n.setUser(user);
                        return n;
                    });
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
                Note note = noteRepository.findByLessonIdAndUserEmail(lesson.getId(), user.getEmail())
                    .orElseGet(() -> {
                        Note n = new Note();
                        n.setLessonId(lesson.getId());
                        n.setCourseId(javaCourse.getId());
                        n.setUser(user);
                        return n;
                    });
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
}
