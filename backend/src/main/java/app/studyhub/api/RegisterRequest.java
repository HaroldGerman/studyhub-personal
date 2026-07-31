package app.studyhub.api; import jakarta.validation.constraints.*; public record RegisterRequest(@NotBlank String name,@Email @NotBlank String email,@NotBlank @Size(min=6) String password){}
