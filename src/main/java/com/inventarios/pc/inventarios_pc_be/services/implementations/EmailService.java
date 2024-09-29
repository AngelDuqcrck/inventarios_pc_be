package com.inventarios.pc.inventarios_pc_be.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmailService {
    
    @Autowired
    JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String email;

        /**
     * Enviar correo electrónico
     * @param emailTo Dirección de correo electrónico del destinatario.
     * @param subject Asunto del correo.
     * @param mensaje Contenido del mensaje.
     * @param mensaje2 Contenido adicional del mensaje.
     * @throws RuntimeException si ocurre un error durante el envío del correo.
     */
    @Async
    public void sendEmail(String emailTo, String subject, String mensaje, String mensaje2) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setFrom(email);
            helper.setTo(emailTo);
            helper.setSubject(subject);
            helper.setText(buildHtmlContent(mensaje, mensaje2), true);

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando correo electrónico: " + e.getMessage());
        }
    }


    private String buildHtmlContent(String mensaje, String mensaje2) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Email</title>\n" +
                "    <style>\n" +
                "        body { font-family: Tahoma; margin: 0; padding: 0; }\n" +
                "        .container { padding: 20px; background-color: #f4f4f4; }\n" +
                "        .imagen { margin-top: 10px; display: flex; justify-content: center; align-items: center; margin-bottom: 20px; }\n" +
                "        .imagen img { max-width: 300px; height: auto; }\n" +
                "        .titulo { text-align: center; }\n" +
                "        .header { background-color: #005ba8; color: white; padding: 10px 0; text-align: center; }\n" +
                "        .content { margin: 20px 0; }\n" +
                "        .content p { margin-left: 20px; }\n" +
                "        .footer { background-color: #005ba8; color: white; text-align: center; padding: 10px 0; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <div class=\"imagen\">\n" +
                "                <img src=\"https://www.clinicasantaanasa.com/images/perfil/2073864225_logoCSAFondoBlanco.png\" alt=\"Logo Clinica Santa Ana\">\n" +
                "            </div>\n" +
                "            <h1>INVENTARIOS PC</h1>\n" +
                "            <h2>Sistema de Gestión de Inventarios de Equipos de Computo de la Clínica Santa Ana</h2>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <div class=\"titulo\">\n" +
                "                <h2>" + mensaje + "</h2>\n" +
                "                <p>" + mensaje2 + "</p>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>&copy; 2024 Todos los derechos reservados.</p>\n" +
                "            <p>Este es un mensaje automático, por favor no responder.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
    }
    
    
}
