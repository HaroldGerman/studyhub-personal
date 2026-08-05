package app.studyhub.application;

import app.studyhub.api.ScheduleItemRequest;
import app.studyhub.domain.Event;
import app.studyhub.domain.ScheduleItem;
import app.studyhub.domain.User;
import app.studyhub.infrastructure.EventRepository;
import app.studyhub.infrastructure.ScheduleItemRepository;
import app.studyhub.infrastructure.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ScheduleService {
    private final ScheduleItemRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public ScheduleService(
        ScheduleItemRepository repository,
        EventRepository eventRepository,
        UserRepository userRepository
    ) {
        this.repository = repository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<ScheduleItem> list(String email) {
        return repository.findByUserEmail(email);
    }

    public ScheduleItem create(ScheduleItemRequest r, String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        ScheduleItem item = new ScheduleItem();
        item.setDayOfWeek(r.dayOfWeek());
        item.setStartTime(r.startTime());
        item.setEndTime(r.endTime());
        item.setCourseTitle(r.courseTitle());
        if (r.color() != null && !r.color().isBlank()) {
            item.setColor(r.color());
        }
        item.setUser(user);
        
        ScheduleItem saved = repository.save(item);
        syncCalendarEvents(email);
        return saved;
    }

    public ScheduleItem update(UUID id, ScheduleItemRequest r, String email) {
        ScheduleItem item = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Horario no encontrado"));
        if (!item.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso no autorizado");
        }
        item.setDayOfWeek(r.dayOfWeek());
        item.setStartTime(r.startTime());
        item.setEndTime(r.endTime());
        item.setCourseTitle(r.courseTitle());
        if (r.color() != null && !r.color().isBlank()) {
            item.setColor(r.color());
        }
        ScheduleItem saved = repository.save(item);
        syncCalendarEvents(email);
        return saved;
    }

    public void remove(UUID id, String email) {
        ScheduleItem item = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Horario no encontrado"));
        if (!item.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso no autorizado");
        }
        repository.delete(item);
        syncCalendarEvents(email);
    }

    public void syncCalendarEvents(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // 1. Delete all previous study events
        List<Event> existingStudyEvents = eventRepository.findByUserEmailOrderByDateTimeAsc(email).stream()
            .filter(e -> e.getTitle().startsWith("Estudio:"))
            .toList();
        eventRepository.deleteAll(existingStudyEvents);

        // 2. Load all schedule items
        List<ScheduleItem> items = repository.findByUserEmail(email);

        // 3. Generate events for 8 weeks starting on July 27, 2026
        LocalDate startDate = LocalDate.of(2026, 7, 27); // Lunes, 27 de Julio 2026
        
        for (int w = 0; w < 8; w++) {
            for (int d = 0; d < 5; d++) { // Lunes a Viernes
                LocalDate date = startDate.plusWeeks(w).plusDays(d);
                String dayName = getDayNameInSpanish(date.getDayOfWeek().getValue());

                for (ScheduleItem item : items) {
                    if (item.getDayOfWeek().equalsIgnoreCase(dayName)) {
                        Event e = new Event();
                        
                        String title = item.getCourseTitle();
                        if (!title.startsWith("Estudio:")) {
                            title = "Estudio: " + title;
                        }
                        
                        e.setTitle(title);
                        e.setColor(item.getColor());
                        e.setUser(user);
                        
                        // Parse time
                        try {
                            LocalTime time = LocalTime.parse(item.getStartTime());
                            e.setDateTime(date.atTime(time));
                        } catch (Exception ex) {
                            e.setDateTime(date.atTime(10, 0));
                        }

                        // Generate description
                        String desc = "Sesión de estudio de " + item.getCourseTitle();
                        String titleLower = title.toLowerCase();
                        if (titleLower.contains("data engineer")) {
                            desc = "Linux, SQL, Python, orquestación, Spark o modelado de pipelines.";
                        } else if (titleLower.contains("java")) {
                            desc = "Microservicios con Spring Boot, seguridad REST y testing unitario.";
                        } else if (titleLower.contains("data analyst") || titleLower.contains("analyst")) {
                            desc = "Análisis exploratorio, tableros interactivos, consultas SQL complejas o dashboards.";
                        } else if (titleLower.contains("javascript") || titleLower.contains("js")) {
                            desc = "Fundamentos y maquetación web, JS estructurado, DOM, APIs y React.";
                        } else if (titleLower.contains("inglés") || titleLower.contains("ingles")) {
                            desc = "Conversación, vocabulario técnico y listening activo.";
                        }
                        e.setDescription(desc);
                        
                        eventRepository.save(e);
                    }
                }
            }
        }
    }

    private String getDayNameInSpanish(int dayOfWeekVal) {
        return switch (dayOfWeekVal) {
            case 1 -> "Lunes";
            case 2 -> "Martes";
            case 3 -> "Miércoles";
            case 4 -> "Jueves";
            case 5 -> "Viernes";
            case 6 -> "Sábado";
            case 7 -> "Domingo";
            default -> "Lunes";
        };
    }
}
