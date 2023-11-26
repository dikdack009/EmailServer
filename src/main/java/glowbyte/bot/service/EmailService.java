package glowbyte.bot.service;

import glowbyte.bot.model.EmailInfo;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;

@Service
public class EmailService {
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final static String EMAIL_TO = "vadim.murov@glowbyteconsulting.com";
    private final static String TEMPLATE_LOCATION = "html/messageTemplate.html";
    private final static String APPLICATION_FILE_LOCATION = "src/main/resources/applicationFile.pdf";

    @Autowired
    public EmailService(JavaMailSender emailSender, SpringTemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    public void sendMessage(EmailInfo emailInfo, boolean fileAdded) throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Multipart mp = new MimeMultipart();
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(getEmilContent(emailInfo, fileAdded), "text/html; charset = utf-8");
        mp.addBodyPart(htmlPart);

        if (fileAdded) {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(APPLICATION_FILE_LOCATION));
            mp.addBodyPart(attachmentPart);
        }

        helper.setTo(EMAIL_TO);
        helper.setSubject("Новая заявка на сопровождение/инцидент");
        message.setContent(mp);
        message.setFrom(new InternetAddress("no_reply@example.com", "NoReply-Ebobot"));
        message.setSender(new NewsAddress("", "noreply@ebobot.ru"));

        emailSender.send(message);
    }

    private String getEmilContent(EmailInfo emailInfo, boolean fileAdded) {
//        Context context = new Context();
//        context.setVariable("incidentNumber", emailInfo.getIncidentNumber());
//        context.setVariable("name", emailInfo.getName());
//        context.setVariable("email", emailInfo.getEmail());
//        context.setVariable("phoneNumber", emailInfo.getPhoneNumber());
//        context.setVariable("clusterName", emailInfo.getClusterName());
//        context.setVariable("incidentPriority", emailInfo.getIncidentPriority());
//        context.setVariable("incidentDescription", emailInfo.getIncidentDescription());
//        context.setVariable("fileAdded", fileAdded);
//        return templateEngine.process(TEMPLATE_LOCATION, context);
        return "";
    }
}
