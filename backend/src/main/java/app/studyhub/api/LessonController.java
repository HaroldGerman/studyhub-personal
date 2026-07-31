package app.studyhub.api;

import app.studyhub.application.LessonService;
import app.studyhub.domain.Lesson;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LessonController {
    private final LessonService service;

    public LessonController(LessonService service) {
        this.service = service;
    }

    @GetMapping("/courses/{courseId}/lessons")
    public List<Lesson> list(@PathVariable UUID courseId, Principal principal) {
        return service.list(courseId, principal.getName());
    }

    @PostMapping("/courses/{courseId}/lessons")
    @ResponseStatus(HttpStatus.CREATED)
    public Lesson create(@PathVariable UUID courseId, @Valid @RequestBody LessonRequest body, Principal principal) {
        return service.create(courseId, body, principal.getName());
    }

    @PutMapping("/lessons/{id}")
    public Lesson update(@PathVariable UUID id, @Valid @RequestBody LessonRequest body, Principal principal) {
        return service.update(id, body, principal.getName());
    }

    @DeleteMapping("/lessons/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, Principal principal) {
        service.remove(id, principal.getName());
    }

    @PostMapping("/lessons/{id}/toggle")
    public Lesson toggle(@PathVariable UUID id, Principal principal) {
        return service.toggle(id, principal.getName());
    }
}
