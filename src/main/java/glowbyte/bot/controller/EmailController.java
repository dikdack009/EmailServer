package glowbyte.bot.controller;

import glowbyte.bot.model.EmailInfo;
import glowbyte.bot.model.EmailResponse;
import glowbyte.bot.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private static final Logger LOG = LoggerFactory.getLogger(EmailController.class);
    @Autowired
    private final EmailService emailService;
    private final static String APPLICATION_FILE_LOCATION = "src/main/resources/";

    @PostMapping(path = "/send-email", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<EmailResponse> sendEmail(@ModelAttribute EmailInfo info, @RequestPart(required = false) MultipartFile file) {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(APPLICATION_FILE_LOCATION + fileName))) {
                byte[] bytes = file.getBytes();
                stream.write(bytes);

            } catch (IOException ex) {
                LOG.error("Error while opening file {}", ex.getMessage());
                return ResponseEntity.badRequest().body(new EmailResponse("Ошибка чтения файла"));

            }
        }
        try {
            emailService.sendMessage(info, file);
            return ResponseEntity.ok().body(new EmailResponse(null));
        } catch (MessagingException | IOException ex) {
            LOG.error("Error while sending email {}", ex.getMessage());
            return ResponseEntity.badRequest().body(new EmailResponse("Ошибка отправки сообщения"));
        }
    }
}
