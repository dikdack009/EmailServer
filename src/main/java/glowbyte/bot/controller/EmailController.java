package glowbyte.bot.controller;

import glowbyte.bot.model.EmailInfo;
import glowbyte.bot.model.EmailResponse;
import glowbyte.bot.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private final static String APPLICATION_FILE_LOCATION = "src/main/resources/applicationFile.pdf";

    @PostMapping(path = "/send-email", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<EmailResponse> sendEmail(@ModelAttribute EmailInfo info, @RequestPart MultipartFile file) {

        try(BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(APPLICATION_FILE_LOCATION))) {
            if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                stream.write(bytes);
                stream.close();
            }
            emailService.sendMessage(info, !file.isEmpty());
            return ResponseEntity.ok().body(new EmailResponse(null));

        } catch (IOException ex) {
            LOG.error("Error while opening file {}", ex.getMessage());
            return ResponseEntity.badRequest().body(new EmailResponse("Ошибка чтения файла"));

        } catch (MessagingException ex) {
            LOG.error("Error while sending email {}", ex.getMessage());
            return ResponseEntity.badRequest().body(new EmailResponse("Ошибка отправки сообщения"));
        }
    }
}
