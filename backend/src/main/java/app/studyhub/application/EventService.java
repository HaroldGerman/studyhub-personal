package app.studyhub.application;

import app.studyhub.api.EventRequest;
import app.studyhub.domain.Event;
import app.studyhub.domain.User;
import app.studyhub.infrastructure.EventRepository;
import app.studyhub.infrastructure.UserRepository;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class EventService {
    private final EventRepository repository;
    private final UserRepository userRepository;

    public EventService(EventRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Event> list(String email) {
        return repository.findByUserEmailOrderByDateTimeAsc(email);
    }

    public Event create(EventRequest r, String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        Event event = new Event();
        event.setTitle(r.title());
        event.setDescription(r.description());
        event.setDateTime(r.dateTime());
        if (r.color() != null && !r.color().isBlank()) {
            event.setColor(r.color());
        }
        event.setUser(user);
        return repository.save(event);
    }

    public Event update(UUID id, EventRequest r, String email) {
        Event event = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado"));
        if (!event.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso no autorizado");
        }
        event.setTitle(r.title());
        event.setDescription(r.description());
        event.setDateTime(r.dateTime());
        if (r.color() != null && !r.color().isBlank()) {
            event.setColor(r.color());
        }
        return repository.save(event);
    }

    public void remove(UUID id, String email) {
        Event event = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado"));
        if (!event.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso no autorizado");
        }
        repository.delete(event);
    }
}
