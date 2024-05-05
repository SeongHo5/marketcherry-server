package com.cherrydev.cherrymarketbe.server.application.common.service;

import com.cherrydev.cherrymarketbe.server.application.exception.ServiceFailedException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.FAIL_TO_CONSTRUCT_EMAIL;
import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.FAIL_TO_SEND_EMAIL;
import static jakarta.mail.Message.RecipientType.TO;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final String ENCODING_CHARSET = "UTF-8";
    private static final String MAILER_SUBTYPE = "HTML";

    private final JavaMailSender mailSender;

    public void sendMail(
            final String recipient,
            final String subject,
            final String content
    ) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.addRecipient(TO, new InternetAddress(recipient));
            message.setSubject(subject, ENCODING_CHARSET);
            message.setText(content, ENCODING_CHARSET, MAILER_SUBTYPE);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new ServiceFailedException(FAIL_TO_CONSTRUCT_EMAIL);
        } catch (MailException e) {
            throw new ServiceFailedException(FAIL_TO_SEND_EMAIL);
        }
    }

}
