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
        "        * { font-family: Tahoma; margin: 0; padding: 0; background-color: #ffffff; }\n" +
        "        .container { padding: 10px; background-color: #ffffff; }\n" +
        "        .imagen { text-align: center; margin-bottom: 7px; }\n" +
        "        .imagen img { display: block; margin: 0 auto; max-width: 120px; height: auto; }\n" +
        "        .titulo { text-align: center; margin-bottom: 15px; color: #000000; }\n" +
        "        .header { border-bottom: 2px solid #005ba8; color: #000000; padding: 10px 0; text-align: center; padding-bottom: 10px; }\n" +
        "        .content { margin: 10px 0; text-align: center; color: #005ba8; }\n" +
        "        .content p { margin-left: 20px; }\n" +
        "        .footer { border-top: 2px solid #005ba8; color: #000000; text-align: center; padding: 10px 0; }\n" +
        "        button { margin-top: 10px; margin-bottom: 10px; padding-left: 20px; padding-right: 20px; color: white; width: auto; height: auto; padding-top: 10px; padding-bottom: 10px; border: 3px solid #005ba8; border-radius: 30px; transition: all 0.2s; cursor: pointer; background: #005ba8; font-size: 1.3em; font-weight: 550; }\n" +
        "        button:hover { background: #005ba8; color: white; font-size: 1.4em; }\n" +
        "        @media (max-width: 768px) { \n" +
        "            button { font-size: 1.3em; padding: 8px 18px; }\n" +
        "        }\n" +
        "        @media (max-width: 480px) {\n" +
        "            button { font-size: 1.15em; padding: 6px 16px; }\n" +
        "        }\n" +
        "    </style>\n" +
        "</head>\n" +
        "<body>\n" +
        "    <div class=\"container\">\n" +
        "        <div class=\"header\">\n" +
        "            <div class=\"imagen\" style=\"text-align: center;\">\n" +
        "                <img src=\"https://www.clinicasantaanasa.com/images/sliders/1486298957_1106801310_LogoCSA.jpg\" \n" +
        "                     alt=\"Logo Clinica Santa Ana\" style=\"display: block; margin: 0 auto;\">\n" +
        "            </div>\n" +
        "            <h2>INVENTARIO PC</h2>\n" +
        "            <h2 style=\"font-weight: lighter;\">Sistema de Gestión de Inventarios de Equipos de Computo de la Clínica Santa Ana</h2>\n" +
        "        </div>\n" +
        "        <div class=\"content\">\n" +
        "            <div class=\"titulo\">\n" +
        "                <h2 style=\"font-weight: lighter; font-size: 15px;\">" + mensaje + "</h2>\n" +
        "                <a href=\"" + mensaje2 + "\" target=\"_blank\">\n" +
        "                    <button>Restablecer Contraseña</button>\n" +
        "                </a>\n" +
        "               <h2 style=\"font-weight: lighter; font-size: 14px;\">Tenga en cuenta que la nueva contraseña debe ser de contener: <br> </br>  Al menos 8 caracteres, una letra mayúscula, una letra minúscula, un número y un carácter especial ( @!$%&*#()_+-=)</h2>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "        <div class=\"footer\">\n" +
        "            <p>Este es un mensaje automático, <strong>por favor NO responder.</strong></p>\n" +
        "            <p>&copy; 2024 Todos los derechos reservados.</p>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "</body>\n" +
        "</html>\n";
    }
    
    
    
}
