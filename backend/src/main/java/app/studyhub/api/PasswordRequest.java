package app.studyhub.api; import jakarta.validation.constraints.*; public record PasswordRequest(@NotBlank String oldPassword,@NotBlank @Size(min=6) String newPassword){}
