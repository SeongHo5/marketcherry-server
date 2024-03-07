package com.cherrydev.cherrymarketbe.integration;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.server.application.common.service.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Testcontainers
@SpringBootTest
class EmailServiceTest {

    @Container
    private static final GenericContainer<?> mailhog =
        new GenericContainer<>("mailhog/mailhog")
            .withExposedPorts(1025, 8025);

    @DynamicPropertySource
    static void setMailhogProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", mailhog::getHost);
        registry.add("spring.mail.port", mailhog::getFirstMappedPort);
    }

    @Autowired
    private EmailService emailService;

    @Test
    @DisplayName("메일 발송에 성공해야 한다.")
    void shouldSendMail() {
        // Given
        String recipient = "test@test.com";
        String subject = "Test Subject";
        String content = "Test Content";

        // When
        assertDoesNotThrow(() -> emailService.sendMail(recipient, subject, content));
    }

    @Test
    @DisplayName("주소가 잘못된 경우 메일 발송에 실패해야 한다.")
    void shouldFailToSendMailWhenRecipientIsInvalid() {
        // Given
        String recipient = "test";
        String subject = "Test Subject";
        String content = "Test Content";

        // When
        assertThrows(ServiceFailedException.class, () -> emailService.sendMail(recipient, subject, content));
    }

}
