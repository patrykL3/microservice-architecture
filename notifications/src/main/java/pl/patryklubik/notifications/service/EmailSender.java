package pl.patryklubik.notifications.service;

import pl.patryklubik.notifications.dto.EmailDto;
import pl.patryklubik.notifications.dto.NotificationInfoDto;

import javax.mail.MessagingException;


/**
 * Create by Patryk Łubik on 20.08.2021.
 */

public interface EmailSender {

    void sendEmails(NotificationInfoDto notificationInfo);

    void sendEmail(EmailDto emailDto) throws MessagingException;
}
