package com.cherrydev.cherrymarketbe.server.application.auth.event.listner;

import com.cherrydev.cherrymarketbe.server.application.auth.event.PasswordResetEvent;
import com.cherrydev.cherrymarketbe.server.application.common.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthEventListener {

    private final EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPasswordReset(PasswordResetEvent event) {
        log.info("Reset password for {}", event.getEmail());
        emailService.sendNotificationMail(event.getEmail());
    }

}
