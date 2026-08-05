package app.studyhub.api;

import app.studyhub.application.ScheduleService;
import app.studyhub.domain.ScheduleItem;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {
    private final ScheduleService service;

    public ScheduleController(ScheduleService service) {
        this.service = service;
    }

    @GetMapping
    public List<ScheduleItem> list(Principal principal) {
        return service.list(principal.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleItem create(@Valid @RequestBody ScheduleItemRequest body, Principal principal) {
        return service.create(body, principal.getName());
    }

    @PutMapping("/{id}")
    public ScheduleItem update(@PathVariable UUID id, @Valid @RequestBody ScheduleItemRequest body, Principal principal) {
        return service.update(id, body, principal.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, Principal principal) {
        service.remove(id, principal.getName());
    }
}
