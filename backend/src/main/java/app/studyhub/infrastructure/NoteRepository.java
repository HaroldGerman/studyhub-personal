package app.studyhub.infrastructure;

import app.studyhub.domain.Note;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note,UUID>{
    List<Note> findByUserEmailOrderByLastModifiedDesc(String email);
    Optional<Note> findByLessonIdAndUserEmail(UUID lessonId, String email);
}
