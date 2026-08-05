package app.studyhub.infrastructure;

import app.studyhub.domain.Lesson;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LessonRepository extends JpaRepository<Lesson,UUID>{
    @Query("SELECT l FROM Lesson l WHERE l.course.id = :courseId ORDER BY l.createdAt ASC")
    List<Lesson> findByCourseId(@Param("courseId") UUID courseId);

    @Query("SELECT l FROM Lesson l WHERE l.course.id = :courseId AND l.course.user.email = :email ORDER BY l.createdAt ASC")
    List<Lesson> findByCourseIdAndCourseUserEmail(@Param("courseId") UUID courseId, @Param("email") String email);
}
