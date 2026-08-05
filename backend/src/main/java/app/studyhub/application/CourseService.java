package app.studyhub.application;

import app.studyhub.api.CourseRequest;
import app.studyhub.api.CourseResponse;
import app.studyhub.domain.Course;
import app.studyhub.domain.User;
import app.studyhub.domain.Lesson;
import app.studyhub.domain.CourseStatus;
import app.studyhub.infrastructure.CourseRepository;
import app.studyhub.infrastructure.UserRepository;
import app.studyhub.infrastructure.LessonRepository;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class CourseService {
    private final CourseRepository repository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    public CourseService(CourseRepository repository, UserRepository userRepository, LessonRepository lessonRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> list(String email) {
        return repository.findByUserEmail(email).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public CourseResponse create(CourseRequest r, String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        Course c = new Course();
        mapRequestToEntity(r, c);
        c.setUser(user);
        c = repository.save(c);
        return toResponse(c);
    }

    public CourseResponse update(UUID id, CourseRequest r, String email) {
        Course c = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));
        if (!c.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso no autorizado a este curso");
        }
        mapRequestToEntity(r, c);
        c = repository.save(c);
        return toResponse(c);
    }

    public void remove(UUID id, String email) {
        Course c = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));
        if (!c.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso no autorizado a este curso");
        }
        // Remove associated lessons first (cascade delete)
        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByCreatedAtAsc(c.getId());
        lessonRepository.deleteAll(lessons);
        
        repository.delete(c);
    }

    public CourseResponse toResponse(Course c) {
        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByCreatedAtAsc(c.getId());
        int total = lessons.size();
        int completed = (int) lessons.stream().filter(Lesson::isCompleted).count();
        int progress = total > 0 ? (completed * 100) / total : 0;
        return new CourseResponse(
            c.getId(), c.getTitle(), c.getCode(), c.getDescription(), c.getProfessor(),
            c.getUniversity(), c.getPlatform(), c.getStartDate(), c.getEndDate(),
            c.getStatus(), c.getColor(), c.getIcon(), total, completed, progress
        );
    }

    private void mapRequestToEntity(CourseRequest r, Course c) {
        c.setTitle(r.title());
        c.setCode(r.code());
        c.setDescription(r.description());
        c.setProfessor(r.professor());
        c.setUniversity(r.university());
        c.setPlatform(r.platform());
        c.setStartDate(r.startDate());
        c.setEndDate(r.endDate());
        if (r.status() != null) {
            c.setStatus(r.status());
        }
        if (r.color() != null && !r.color().isBlank()) {
            c.setColor(r.color());
        }
        if (r.icon() != null && !r.icon().isBlank()) {
            c.setIcon(r.icon());
        }
    }
}
