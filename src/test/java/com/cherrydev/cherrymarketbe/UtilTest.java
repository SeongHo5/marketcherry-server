package com.cherrydev.cherrymarketbe;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDate;

import static com.cherrydev.cherrymarketbe.server.application.auth.constant.AuthConstant.VERIFICATION_CODE_LENGTH;
import static com.cherrydev.cherrymarketbe.server.application.common.utils.CodeGenerator.generateRandomCode;
import static com.cherrydev.cherrymarketbe.server.application.common.utils.CodeGenerator.generateRandomPassword;
import static com.cherrydev.cherrymarketbe.server.application.common.utils.TimeFormatter.localDateToString;
import static com.cherrydev.cherrymarketbe.server.application.common.utils.TimeFormatter.timeStampToString;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class UtilTest {

    @Test
    void testCodeGenerator() {
        // Given
        String case1 = generateRandomCode(VERIFICATION_CODE_LENGTH);
        String case2 = generateRandomPassword(VERIFICATION_CODE_LENGTH);

        // When & Then
        assertThat(case1.length()).isEqualTo(VERIFICATION_CODE_LENGTH);
        assertThat(case2.length()).isEqualTo(VERIFICATION_CODE_LENGTH);
    }

    @Test
    void testTimeFormatter() {
        // Given
        LocalDate case1 = LocalDate.of(2021, 10, 10);
        Timestamp case2 = Timestamp.valueOf("2021-10-10 10:10:10");

        // When
        String result1 = localDateToString(case1);
        String result2 = timeStampToString(case2);

        // Then
        assertThat(result1).isEqualTo(case1.toString());
        assertThat(result2).isEqualTo("2021-10-10 10:10:10");
    }


}
