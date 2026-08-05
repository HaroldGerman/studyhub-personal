package app.studyhub.application;

import app.studyhub.api.LessonRequest;
import app.studyhub.domain.Course;
import app.studyhub.domain.Lesson;
import app.studyhub.domain.CourseStatus;
import app.studyhub.infrastructure.CourseRepository;
import app.studyhub.infrastructure.LessonRepository;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class LessonService {
    private final LessonRepository repository;
    private final CourseRepository courseRepository;

    public LessonService(LessonRepository repository, CourseRepository courseRepository) {
        this.repository = repository;
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public List<Lesson> list(UUID courseId, String email) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));
        if (!course.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso no autorizado a este curso");
        }
        return repository.findByCourseIdOrderByCreatedAtAsc(courseId);
    }

    public Lesson create(UUID courseId, LessonRequest r, String email) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));
        if (!course.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso no autorizado a este curso");
        }
        Lesson lesson = new Lesson();
        lesson.setTitle(r.title());
        if (r.completed() != null) {
            lesson.setCompleted(r.completed());
        }
        lesson.setCourse(course);
        lesson = repository.save(lesson);
        updateCourseStatus(course);
        return lesson;
    }

    public Lesson update(UUID lessonId, LessonRequest r, String email) {
        Lesson lesson = repository.findById(lessonId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lección no encontrada"));
        Course course = lesson.getCourse();
        if (!course.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso no autorizado");
        }
        lesson.setTitle(r.title());
        if (r.completed() != null) {
            lesson.setCompleted(r.completed());
        }
        lesson = repository.save(lesson);
        updateCourseStatus(course);
        return lesson;
    }

    public void remove(UUID lessonId, String email) {
        Lesson lesson = repository.findById(lessonId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lección no encontrada"));
        Course course = lesson.getCourse();
        if (!course.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso no autorizado");
        }
        repository.delete(lesson);
        updateCourseStatus(course);
    }

    public Lesson toggle(UUID lessonId, String email) {
        Lesson lesson = repository.findById(lessonId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lección no encontrada"));
        Course course = lesson.getCourse();
        if (!course.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso no autorizado");
        }
        lesson.setCompleted(!lesson.isCompleted());
        lesson = repository.save(lesson);
        updateCourseStatus(course);
        return lesson;
    }

    private void updateCourseStatus(Course course) {
        List<Lesson> lessons = repository.findByCourseIdOrderByCreatedAtAsc(course.getId());
        if (lessons.isEmpty()) {
            course.setStatus(CourseStatus.NOT_STARTED);
        } else {
            long completed = lessons.stream().filter(Lesson::isCompleted).count();
            if (completed == lessons.size()) {
                course.setStatus(CourseStatus.COMPLETED);
            } else if (completed > 0 || course.getStatus() == CourseStatus.NOT_STARTED) {
                course.setStatus(CourseStatus.IN_PROGRESS);
            }
        }
        courseRepository.save(course);
    }
}
