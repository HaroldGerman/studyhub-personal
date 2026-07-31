package app.studyhub.api;

public record RegisterResponse(
    boolean verificationRequired,
    String email,
    String message
) {}
