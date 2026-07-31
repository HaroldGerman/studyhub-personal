package app.studyhub.api;

import app.studyhub.domain.CourseStatus;
import java.time.LocalDate;
import java.util.UUID;

public record CourseResponse(
    UUID id,
    String title,
    String code,
    String description,
    String professor,
    String university,
    String platform,
    LocalDate startDate,
    LocalDate endDate,
    CourseStatus status,
    String color,
    String icon,
    int lessons,
    int completed,
    int progress
) {}
