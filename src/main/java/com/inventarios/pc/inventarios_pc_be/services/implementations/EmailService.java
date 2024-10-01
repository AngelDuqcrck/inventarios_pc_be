package com.inventarios.pc.inventarios_pc_be.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
//Servicio realizado para el envio de correos
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
            helper.setText(buildHtmlContent(mensaje, mensaje2 ), true);

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando correo electrónico: " + e.getMessage());
        }
    }

    //Plantilla de Correo Electronico
    private String buildHtmlContent(String mensaje, String mensaje2 ) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Email</title>\n" +
                "    <style>\n" +
                "        *{ font-family: Tahoma; margin: 0; padding: 0; }\n" +
                "        .container { padding: 20px; background-color: #f4f4f4; }\n" +
                "        .imagen { text-align: center; margin-bottom: 20px; }\n" +
                "        .imagen img { display: block; margin: 0 auto; max-width: 300px; height: auto; }\n" +
                "        .titulo { text-align: center; margin-bottom: 15px; color: #005ba8; }\n" +
                "        .header { background-color: #005ba8; color: white; padding: 10px 0; text-align: center; }\n" +
                "        .content { margin: 20px 0; text-align: center; color: #005ba8; }\n" +
                "        .content p { margin-left: 20px; }\n" +
                "        .footer { background-color: #005ba8; color: white; text-align: center; padding: 10px 0; }\n" +
                "        button { margin-top: 20px; margin-bottom: 10px;  padding-left: 20px; padding-right: 20px; color: #005ba8; width: auto; height: auto; padding-top: 10px; padding-bottom: 10px; border: 3px solid #005ba8; border-radius: 30px; transition: all 0.2s; cursor: pointer; background: white; font-size: 1.2em; font-weight: 550; }\n" +
                "        button:hover { background: #005ba8; color: white; font-size: 1.3em;  }\n" +
                "        @media (max-width: 768px) { \n" +
                "            button { font-size: 1.1em; padding: 8px 18px; }\n" +
                "        }\n" +
                "        @media (max-width: 480px) {\n" +
                "            button { font-size: 1em; padding: 6px 16px; }\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <div class=\"imagen\" style=\"text-align: center;\">\n" +
                "                <img src=\"https://www.clinicasantaanasa.com/images/perfil/2073864225_logoCSAFondoBlanco.png\" \n" +
                "                     alt=\"Logo Clinica Santa Ana\" style=\"display: block; margin: 0 auto;\">\n" +
                "            </div>\n" +
                "            <h1>INVENTARIOS PC</h1>\n" +
                "            <h1>Sistema de Gestión de Inventarios de Equipos de Computo de la Clínica Santa Ana</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <div class=\"titulo\">\n" +
                "                <h2>" + mensaje + "</h2>\n" +
                "            <a href="+ mensaje2 + " target=\"_blank\">\n" +
                "                <button>Retablecer Contraseña</button>\n" +
                "            </a>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <h2>Este es un mensaje automático, por favor no responder.</h2>\n" +
                "            <h3>&copy; 2024 Todos los derechos reservados.</h3>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
    }
    
    
    
}
