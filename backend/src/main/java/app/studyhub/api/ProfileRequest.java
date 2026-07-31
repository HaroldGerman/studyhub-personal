package app.studyhub.api; import jakarta.validation.constraints.*; public record ProfileRequest(@NotBlank String name,@Email @NotBlank String email){}
