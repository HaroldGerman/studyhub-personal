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

        // Try Resend HTTP API first if API key is provided
        String resendApiKey = System.getenv("RESEND_API_KEY");
        if (resendApiKey != null && !resendApiKey.trim().isEmpty()) {
            try {
                java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
                String cleanName = name.replace("\"", "\\\"");
                String json = "{"
                    + "\"from\":\"StudyHub <onboarding@resend.dev>\","
                    + "\"to\":[\"" + to + "\"],"
                    + "\"subject\":\"Verifica tu cuenta - StudyHub\","
                    + "\"text\":\"Hola " + cleanName + ",\\n\\nGracias por registrarte en StudyHub. Tu código de verificación de 6 dígitos es:\\n\\n➡️   " + code + "   ⬅️\\n\\nEste código es válido por 15 minutos.\\n\\nSaludos,\\nEl equipo de StudyHub\""
                    + "}";
                java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create("https://api.resend.com/emails"))
                    .header("Authorization", "Bearer " + resendApiKey)
                    .header("Content-Type", "application/json")
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(json))
                    .build();
                java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    System.out.println("Email de verificación enviado exitosamente vía Resend HTTP API a: " + to);
                    return;
                } else {
                    System.err.println("Fallo al enviar correo vía Resend HTTP API: " + response.statusCode() + " - " + response.body());
                }
            } catch (Exception e) {
                System.err.println("Error enviando email vía Resend HTTP API: " + e.getMessage());
            }
        }

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
                System.err.println("Error enviando email real vía SMTP: " + e.getMessage());
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
