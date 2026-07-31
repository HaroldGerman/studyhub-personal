package app.studyhub.infrastructure;

import app.studyhub.domain.Event;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,UUID>{
    List<Event> findByUserEmailOrderByDateTimeAsc(String email);
}
