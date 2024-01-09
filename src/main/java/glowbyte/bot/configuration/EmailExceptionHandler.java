package glowbyte.bot.configuration;

import glowbyte.bot.model.EmailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import java.io.IOException;

@Component
public class EmailExceptionHandler {
    @ExceptionHandler({MessagingException.class, IOException.class})
    public @ResponseBody ResponseEntity<EmailResponse> handleException(MessagingException e) {
        return ResponseEntity.badRequest().body(new EmailResponse("Ошибка отправки сообщения"));
    }
}
