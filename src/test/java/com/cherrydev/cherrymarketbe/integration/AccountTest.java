package com.cherrydev.cherrymarketbe.integration;

import static com.cherrydev.cherrymarketbe.integration.TestHelper.convertToJSONString;
import static com.cherrydev.cherrymarketbe.integration.factory.AccountFactory.createSignUpRequestDtoA;
import static com.cherrydev.cherrymarketbe.integration.factory.AccountFactory.createSignUpRequestDtoB;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestSignUp;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.test.web.servlet.RequestBuilder;

final class AccountTest extends AbstractIntegrationTest {

  @TestFactory
  @DisplayName("회원 관련 API 테스트")
  Stream<DynamicTest> dynamicTestForRegister() throws JsonProcessingException {
    // Given

    // When & Then
    return Stream.of(
        testHelper.testApiEndpoint(
            "회원-회원가입_성공", registerAccount(createSignUpRequestDtoA()), status().is2xxSuccessful()),
        testHelper.testApiEndpoint(
            "회원-회원가입_실패_이미_가입된_이메일",
            registerAccount(createSignUpRequestDtoA()),
            status().is4xxClientError()),
        testHelper.testApiEndpoint(
            "회원-회원가입_실패_금지된_이름",
            registerAccount(createSignUpRequestDtoB()),
            status().is4xxClientError()));
  }

  private RequestBuilder registerAccount(RequestSignUp request) throws JsonProcessingException {
    return post("/api/accounts")
        .contentType(APPLICATION_JSON)
        .content(convertToJSONString(request));
  }
}
