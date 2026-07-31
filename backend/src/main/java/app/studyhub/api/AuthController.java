package app.studyhub.api;
import app.studyhub.domain.User;
import app.studyhub.infrastructure.UserRepository;
import app.studyhub.security.JwtService;
import app.studyhub.application.EmailService;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final EmailService emailService;

    public AuthController(UserRepository users, PasswordEncoder encoder, JwtService jwt, EmailService emailService) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        var user = users.findByEmail(request.email())
            .filter(u -> encoder.matches(request.password(), u.getPasswordHash()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));
        if (!user.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tu cuenta no ha sido verificada. Revisa tu correo");
        }
        return new TokenResponse(jwt.issue(user.getEmail()), "Bearer");
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        if (users.findByEmail(request.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPasswordHash(encoder.encode(request.password()));
        user.setEnabled(false); // Must verify email first

        // Generate 6-digit verification code
        int code = (int) (100000 + Math.random() * 900000);
        String codeStr = String.valueOf(code);
        user.setVerificationCode(codeStr);
        user.setVerificationCodeExpires(java.time.LocalDateTime.now().plusMinutes(15));

        users.save(user);

        // Send code via email service
        emailService.sendVerificationCode(user.getEmail(), user.getName(), codeStr);

        return new RegisterResponse(true, user.getEmail(), "Código de verificación enviado.");
    }

    @PostMapping("/verify")
    public TokenResponse verify(@Valid @RequestBody VerifyRequest request) {
        var user = users.findByEmail(request.email())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        if (user.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cuenta ya está activada");
        }
        
        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(request.code())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código de verificación incorrecto");
        }
        
        if (user.getVerificationCodeExpires().isBefore(java.time.LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El código de verificación ha expirado");
        }
        
        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpires(null);
        users.save(user);
        
        return new TokenResponse(jwt.issue(user.getEmail()), "Bearer");
    }

    @GetMapping("/me")
    public ProfileResponse me(Principal principal) {
        var user = users.findByEmail(principal.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return new ProfileResponse(user.getName(), user.getEmail());
    }

    @PutMapping("/profile")
    public ProfileResponse updateProfile(@Valid @RequestBody ProfileRequest request, Principal principal) {
        var user = users.findByEmail(principal.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        if (!user.getEmail().equalsIgnoreCase(request.email()) && users.findByEmail(request.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está en uso");
        }
        user.setName(request.name());
        user.setEmail(request.email());
        users.save(user);
        return new ProfileResponse(user.getName(), user.getEmail());
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void changePassword(@Valid @RequestBody PasswordRequest request, Principal principal) {
        var user = users.findByEmail(principal.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        if (!encoder.matches(request.oldPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contraseña anterior incorrecta");
        }
        user.setPasswordHash(encoder.encode(request.newPassword()));
        users.save(user);
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void forgot() {
        /* Integrar proveedor de correo en producción. */
    }
}
