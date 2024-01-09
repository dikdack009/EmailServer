package glowbyte.bot.service;

import glowbyte.bot.model.EmailInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmailContentFactory {
    private final SpringTemplateEngine templateEngine;
    @Value("${TEMPLATE_FILENAME}")
    private String TEMPLATE_FILENAME;

    public String getEmilContent(EmailInfo emailInfo, boolean fileAdded) {
        Context context = new Context();
        context.setVariable("incidentNumber", emailInfo.incidentNumber());
        context.setVariable("customerName", emailInfo.customerName());
        context.setVariable("name", emailInfo.name());
        context.setVariable("email", emailInfo.email());
        context.setVariable("phoneNumber", emailInfo.phoneNumber());
        context.setVariable("clusterName", emailInfo.clusterName());
        context.setVariable("incidentPriority", emailInfo.incidentPriority());
        context.setVariable("incidentDescription", emailInfo.incidentDescription());
        context.setVariable("fileAdded", fileAdded);
        return templateEngine.process(TEMPLATE_FILENAME, context);
    }
}
