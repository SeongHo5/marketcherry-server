package com.cherrydev.cherrymarketbe.server.application.common.service;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.AuthException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.server.application.common.constant.EmailConstant;
import com.cherrydev.cherrymarketbe.server.application.common.utils.CodeGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.server.application.common.constant.EmailConstant.*;
import static com.cherrydev.cherrymarketbe.server.application.common.service.template.EmailTemplate.*;
import static jakarta.mail.Message.RecipientType.TO;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final RedisService redisService;

    private void sendMail(
            final String recipient,
            final String subject,
            final String content
    ) {
        MimeMessage message = emailSender.createMimeMessage();

        try {
            message.addRecipient(TO, new InternetAddress(recipient));
            message.setSubject(subject, ENCODING_CHARSET);
            message.setText(content, ENCODING_CHARSET, MAILER_SUBTYPE);
            emailSender.send(message);
        } catch (MessagingException e) {
            throw new ServiceFailedException(FAIL_TO_CONSTRUCT_EMAIL);
        } catch (MailException e) {
            throw new ServiceFailedException(FAIL_TO_SEND_EMAIL);
        }
    }

    /**
     * 이미 발송된 코드가 있는지 확인하고, 없으면 인증 코드 생성 후 이메일 전송
     * <p>
     * <em>사용시 주의 사항</em>
     * <p>
     * ※ 이메일 발송에 약 7초 정도 소요됩니다.
     *
     * @param email 전송할 이메일
     */
    public void sendVerificationMail(final String email) {
        checkIfEmailIsWhiteListed(email);
        checkIfCodeAlreadySent(email, EmailConstant.PREFIX_VERIFY);

        String verificationCode = CodeGenerator.generateRandomCode(VERIFICATION_CODE_LENGTH);

        redisService.setDataExpire(EmailConstant.PREFIX_VERIFY + email, verificationCode, VERIFICATION_CODE_EXPIRE_TIME);
        sendMail(email, VERIFICATION_TITTLE, createVerificationMessage(verificationCode));
    }

    public void sendPasswordResetMail(final String email) {
        checkIfCodeAlreadySent(email, EmailConstant.PREFIX_PW_RESET);

        String verificationCode = CodeGenerator.generateRandomCode(VERIFICATION_CODE_LENGTH);

        redisService.setDataExpire(EmailConstant.PREFIX_PW_RESET + email, verificationCode, VERIFICATION_CODE_EXPIRE_TIME);
        sendMail(email, PW_RESET_TITTLE, createPasswordResetMessage(verificationCode));
    }

    public void sendNotificationMail(final String email) {
        sendMail(email, PW_RESET_INFO_TITTLE, createPasswordResetInfoMessage());
    }

    // ============ PRIVATE METHODS ============

    private void checkIfCodeAlreadySent(final String email, final String prefix) {
        if (redisService.hasKey(prefix + email)) {
            throw new AuthException(EMAIL_ALREADY_SENT);
        }
    }

    private void checkIfEmailIsWhiteListed(final String email) {
        if (redisService.hasKey(EmailConstant.PREFIX_VERIFIED + email)) {
            throw new AuthException(EMAIL_ALREADY_VERIFIED);
        }
    }
}
