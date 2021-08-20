package pl.patryklubik.notifications.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pl.patryklubik.notifications.dto.NotificationInfoDto;


/**
 * Create by Patryk Łubik on 20.08.2021.
 */

@Service
@Slf4j
public class RabbitMqListener {

    private final EmailSender emailSender;

    public RabbitMqListener(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @RabbitListener(queues = "enroll_finish")
    public void handleFinishEnroll(NotificationInfoDto notificationInfo) {
        emailSender.sendEmails(notificationInfo);
    }
}
