package app.studyhub.infrastructure;

import app.studyhub.domain.Lesson;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson,UUID>{
    List<Lesson> findByCourseIdOrderByCreatedAtAsc(UUID courseId);
    List<Lesson> findByCourseIdAndCourseUserEmailOrderByCreatedAtAsc(UUID courseId, String email);
}
