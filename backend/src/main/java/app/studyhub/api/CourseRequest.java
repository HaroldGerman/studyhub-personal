package app.studyhub.api; import jakarta.validation.constraints.NotBlank; public record CourseRequest(@NotBlank String title,String professor){}
