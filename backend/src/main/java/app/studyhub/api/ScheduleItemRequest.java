package app.studyhub.api;

import jakarta.validation.constraints.NotBlank;

public record ScheduleItemRequest(
    @NotBlank String dayOfWeek,
    @NotBlank String startTime,
    @NotBlank String endTime,
    @NotBlank String courseTitle,
    String color
) {}
