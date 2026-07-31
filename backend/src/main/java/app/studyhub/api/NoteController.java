package app.studyhub.api;

import app.studyhub.application.NoteService;
import app.studyhub.domain.Note;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Note> list(Principal principal) {
        return service.list(principal.getName());
    }

    @GetMapping("/lesson/{lessonId}")
    public Note getByLesson(@PathVariable UUID lessonId, Principal principal) {
        return service.getByLesson(lessonId, principal.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Note create(@Valid @RequestBody NoteRequest body, Principal principal) {
        return service.create(body, principal.getName());
    }

    @PutMapping("/{id}")
    public Note update(@PathVariable UUID id, @Valid @RequestBody NoteRequest body, Principal principal) {
        return service.update(id, body, principal.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, Principal principal) {
        service.remove(id, principal.getName());
    }
}
