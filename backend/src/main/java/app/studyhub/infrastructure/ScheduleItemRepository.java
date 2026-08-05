package app.studyhub.infrastructure;

import app.studyhub.domain.ScheduleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ScheduleItemRepository extends JpaRepository<ScheduleItem, UUID> {
    List<ScheduleItem> findByUserEmail(String email);
    void deleteByUserEmail(String email);
}
