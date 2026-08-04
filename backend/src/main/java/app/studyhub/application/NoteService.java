package app.studyhub.application;

import app.studyhub.api.NoteRequest;
import app.studyhub.domain.Note;
import app.studyhub.domain.User;
import app.studyhub.domain.Lesson;
import app.studyhub.infrastructure.NoteRepository;
import app.studyhub.infrastructure.UserRepository;
import app.studyhub.infrastructure.LessonRepository;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class NoteService {
    private final NoteRepository repository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    public NoteService(NoteRepository repository, UserRepository userRepository, LessonRepository lessonRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
    }

    @Transactional(readOnly = true)
    public List<Note> list(String email) {
        return repository.findByUserEmailOrderByLastModifiedDesc(email);
    }

    public Note getByLesson(UUID lessonId, String email) {
        List<Note> notes = repository.findByLessonIdAndUserEmail(lessonId, email);
        if (!notes.isEmpty()) {
            return notes.get(0);
        }
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lección no encontrada"));
        
        Note note = new Note();
        note.setTitle("Apuntes: " + lesson.getTitle());
        note.setBody("");
        note.setLessonId(lessonId);
        note.setCourseId(lesson.getCourse().getId());
        note.setUser(user);
        note.setLastModified(LocalDateTime.now());
        return repository.save(note);
    }

    public Note create(NoteRequest r, String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        Note note = new Note();
        note.setTitle(r.title());
        note.setBody(r.body() != null ? r.body() : "");
        note.setScratchpad(r.scratchpad() != null ? r.scratchpad() : "");
        note.setCourseId(r.courseId());
        note.setLessonId(r.lessonId());
        note.setUser(user);
        note.setLastModified(LocalDateTime.now());
        return repository.save(note);
    }

    public Note update(UUID id, NoteRequest r, String email) {
        Note note = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no encontrada"));
        if (!note.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso no autorizado");
        }
        note.setTitle(r.title());
        note.setBody(r.body() != null ? r.body() : "");
        note.setScratchpad(r.scratchpad() != null ? r.scratchpad() : "");
        note.setCourseId(r.courseId());
        note.setLessonId(r.lessonId());
        note.setLastModified(LocalDateTime.now());
        return repository.save(note);
    }

    public void remove(UUID id, String email) {
        Note note = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no encontrada"));
        if (!note.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso no autorizado");
        }
        repository.delete(note);
    }
}
