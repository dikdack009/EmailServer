package glowbyte.bot.controller;

import glowbyte.bot.model.EmailInfo;
import glowbyte.bot.model.EmailResponse;
import glowbyte.bot.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class EmailController { // TODO: 27.11.2023 прикрутить сваггер
    private final EmailService emailService;
    @PostMapping(path = "/send-email", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<EmailResponse> sendEmail(@ModelAttribute EmailInfo info, @RequestPart(required = false) MultipartFile file) throws MessagingException, IOException {
        emailService.sendMessage(info, file);
        return ResponseEntity.ok().body(new EmailResponse(null));

    }
}
