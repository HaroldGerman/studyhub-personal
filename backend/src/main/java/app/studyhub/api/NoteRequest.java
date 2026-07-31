package app.studyhub.api;
import jakarta.validation.constraints.*;
import java.util.UUID;

public record NoteRequest(
    @NotBlank String title,
    String body,
    UUID courseId,
    UUID lessonId,
    String scratchpad
) {}
