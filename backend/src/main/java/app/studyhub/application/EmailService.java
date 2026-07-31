package app.studyhub.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String to, String name, String code) {
        String subject = "Verifica tu cuenta - StudyHub";
        String body = "Hola " + name + ",\n\n" +
                      "Gracias por registrarte en StudyHub. Tu código de verificación de 6 dígitos es:\n\n" +
                      "➡️   " + code + "   ⬅️\n\n" +
                      "Este código es válido por 15 minutos.\n\n" +
                      "Si no solicitaste esta cuenta, por favor ignora este correo.\n\n" +
                      "Saludos,\nEl equipo de StudyHub";

        if (mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("StudyHub <noreply@studyhub.local>");
                message.setTo(to);
                message.setSubject(subject);
                message.setText(body);
                mailSender.send(message);
                System.out.println("Email de verificación enviado a: " + to);
                return;
            } catch (Exception e) {
                System.err.println("Error enviando email real: " + e.getMessage());
            }
        }

        // Fallback: print to console so they can copy it from logs!
        System.out.println("\n==================================================");
        System.out.println("📧 SIMULACIÓN DE ENVÍO DE CORREO A: " + to);
        System.out.println("Código generado: " + code);
        System.out.println("Cuerpo del mensaje:\n" + body);
        System.out.println("==================================================\n");
    }
}
