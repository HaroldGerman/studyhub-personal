package app.studyhub.api; import jakarta.validation.constraints.*; public record LessonRequest(@NotBlank String title,Boolean completed){}
