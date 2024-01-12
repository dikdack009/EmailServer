package glowbyte.bot.service;

import glowbyte.bot.model.EmailInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final EmailContentFactory emailContentFactory;
    @Value("${RECIPIENT_MAIL}")
    private String RECIPIENT_MAIL;

    public void sendMessage(EmailInfo emailInfo, MultipartFile file) throws MessagingException, IOException {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            Multipart multipart = new MimeMultipart();
            helper.setTo(RECIPIENT_MAIL);
            helper.setSubject("Новая заявка на сопровождение/инцидент");

            setTextPart(multipart, emailInfo, file);
            if (!file.isEmpty()) {
                setFilePart(multipart, file);
            }
            message.setContent(multipart);
            message.setFrom(new InternetAddress("no_reply@glowbyteconsulting.com", "NoReply-requestBot"));
            emailSender.send(message);

        } catch (MessagingException | IOException exception) {
            log.error("Возникла ошибка при отправке письма");
            log.error(exception.getMessage());
            throw exception;
        } finally {
            if (!file.isEmpty()) {
                Files.delete(Paths.get(file.getOriginalFilename()));
            }
        }
    }

    private void setTextPart(Multipart multipart, EmailInfo emailInfo, MultipartFile file) throws MessagingException {
        try {
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(emailContentFactory.getEmilContent(emailInfo, !file.isEmpty()), "text/html; charset = utf-8");
            multipart.addBodyPart(htmlPart);
        } catch (MessagingException exception) {
            log.error("Возникла ошибка при формировании текста письма");
            throw exception;
        }
    }

    private void setFilePart(Multipart multipart, MultipartFile file) throws MessagingException, IOException {
        try {
            Path path = Paths.get(file.getOriginalFilename());
            File temp = Files.write(path, file.getBytes()).toFile();
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(temp);
            multipart.addBodyPart(attachmentPart);
        } catch (MessagingException | IOException exception) {
            log.error("Возникла ошибка при добавлении файла к письму");
            throw exception;
        }
    }
}
