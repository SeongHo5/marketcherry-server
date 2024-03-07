package com.cherrydev.cherrymarketbe;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestSignUp;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static com.cherrydev.cherrymarketbe.TestHelper.convertToJSONString;
import static com.cherrydev.cherrymarketbe.factory.AccountFactory.createSignUpRequestDtoA;
import static com.cherrydev.cherrymarketbe.factory.AccountFactory.createSignUpRequestDtoB;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Rollback
@Transactional
@SpringBootTest
@DisabledInAotMode
@AutoConfigureMockMvc
class AccountTest {

    @Autowired
    private TestHelper testHelper;

    @TestFactory
    @DisplayName("회원 관련 API 테스트")
    Stream<DynamicTest> dynamicTestForRegister() throws JsonProcessingException {
        // Given

        // When & Then
        return Stream.of(
                testHelper.testApiEndpoint("회원-회원가입_성공", registerAccount(createSignUpRequestDtoA()), status().is2xxSuccessful()),
                testHelper.testApiEndpoint("회원-회원가입_실패_이미_가입된_이메일", registerAccount(createSignUpRequestDtoA()), status().is4xxClientError()),
                testHelper.testApiEndpoint("회원-회원가입_실패_금지된_이름", registerAccount(createSignUpRequestDtoB()), status().is4xxClientError())
        );
    }

    private RequestBuilder registerAccount(RequestSignUp request) throws JsonProcessingException {
        return post("/api/accounts")
                .contentType(APPLICATION_JSON)
                .content(convertToJSONString(request));
    }
}
