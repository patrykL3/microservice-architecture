package pl.patryklubik.notifications.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.patryklubik.notifications.dto.EmailDto;
import pl.patryklubik.notifications.service.EmailSender;

import javax.mail.MessagingException;
import javax.validation.Valid;


/**
 * Create by Patryk Łubik on 20.08.2021.
 */

@RestController
@Slf4j
public class EmailController {

    private final EmailSender emailSender;

    public EmailController(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(@RequestBody @Valid EmailDto emailDto) {
        try {
            emailSender.sendEmail(emailDto);
        } catch (MessagingException e) {
            log.error("Wiadomość do " + emailDto + " się nie wysłała ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wiadomość do " + emailDto + " się nie wysłała!");
        }
        return ResponseEntity.ok("Wysłano email do: " + emailDto.getTo());
    }
}