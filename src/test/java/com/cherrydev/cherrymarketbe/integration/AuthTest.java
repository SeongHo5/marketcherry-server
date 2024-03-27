package com.cherrydev.cherrymarketbe.integration;

import com.cherrydev.cherrymarketbe.server.application.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.request.RequestSignIn;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.JwtResponse;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.RequestJwt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.RequestBuilder;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.stream.Stream;

import static com.cherrydev.cherrymarketbe.integration.TestHelper.convertToJSONString;
import static com.cherrydev.cherrymarketbe.integration.factory.AuthFactory.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
public class AuthTest extends IntegrationTest {

    @Container
    private static final RedisContainer redis = new RedisContainer(
            RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG)).withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", redis::getFirstMappedPort);
    }

    @Container
    private static final GenericContainer<?> mailhog =
            new GenericContainer<>("mailhog/mailhog").withExposedPorts(1025, 8025);

    @DynamicPropertySource
    static void setMailhogProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", mailhog::getHost);
        registry.add("spring.mail.port", mailhog::getFirstMappedPort);
    }

    @Autowired
    private JwtProvider jwtProvider;

    @TestFactory
    @DisplayName("사용자 인증 - 로그인 테스트")
    Stream<DynamicTest> dynamicTestForAuth() throws JsonProcessingException {
        // Given
        RequestSignIn requestSignInA = createSignInRequestDtoA();
        RequestSignIn requestSignInB = createSignInRequestDtoB();
        RequestSignIn requestSignInC = createSignInRequestDtoC();
        RequestSignIn requestSignInD = createSignInRequestDtoD();
        RequestSignIn requestSignInE = createSignInRequestDtoE();
        RequestSignIn requestSignInF = createSignInRequestDtoF();
        JwtResponse jwtToken = jwtProvider.createJwtToken("boram17@example.org");
        String wrongAccessToken = "wrongToken";
        RequestJwt requestJwtA = createJwtRequestDto(jwtToken);
        RequestJwt requestJwtB = createJwtRequestDto(wrongAccessToken, "wrongRefreshToken");

        // When & Then
        return Stream.of(
                testHelper.testApiEndpoint("사용자_인증-로그인_성공", signIn(requestSignInA), status().is2xxSuccessful()),
                testHelper.testApiEndpoint("사용자_인증-로그인_실패_비밀번호_오류", signIn(requestSignInB), status().is4xxClientError()),
                testHelper.testApiEndpoint("사용자_인증-로그인_실패_이메일_오류", signIn(requestSignInC), status().is4xxClientError()),
                testHelper.testApiEndpoint("사용자_인증-로그인_실패_계정_미활성화", signIn(requestSignInD), status().is4xxClientError()),
                testHelper.testApiEndpoint("사용자_인증-로그인_실패_탈퇴한_사용자", signIn(requestSignInE), status().is4xxClientError()),
                testHelper.testApiEndpoint("사용자_인증-로그인_실패_계정_존재하지_않음", signIn(requestSignInF), status().is4xxClientError()),
                testHelper.testApiEndpoint("사용자_인증-토큰_재발급_성공", reIssue(requestJwtA), status().is2xxSuccessful()),
                testHelper.testApiEndpoint("사용자_인증-토큰_재발급_실패_토큰_오류", reIssue(requestJwtB), status().is4xxClientError()),
                testHelper.testApiEndpoint("사용자_인증-로그아웃_성공", signOut(jwtToken.accessToken()), status().is2xxSuccessful()),
                testHelper.testApiEndpoint("사용자_인증-로그아웃_실패_토큰_오류", signOut(wrongAccessToken), status().is4xxClientError())
        );
    }

    private RequestBuilder signIn(RequestSignIn request) throws JsonProcessingException {
        return post("/api/auth/login")
                .contentType(APPLICATION_JSON)
                .content(convertToJSONString(request));
    }

    private RequestBuilder signOut(String accessToken) {
        return delete("/api/auth/" + accessToken);
    }

    private RequestBuilder reIssue(RequestJwt requestJwt) throws JsonProcessingException {
        return post("/api/auth/re-issue")
                .contentType(APPLICATION_JSON)
                .content(convertToJSONString(requestJwt));
    }

    @TestFactory
    @DisplayName("사용자 인증 - 이메일 인증 테스트")
    Stream<DynamicTest> dynamicTestForEmailVerification() {
        // Given
        String email = "test@example.com";
        String wrongEmail = "wrong@wrong.net";
        String verificationCodeA = "A1B2C3";

        // When & Then
        return Stream.of(
                testHelper.testApiEndpoint("사용자_인증-본인인증_메일_전송_성공", sendVerificationEmail(email), status().is2xxSuccessful()),
                testHelper.testApiEndpoint("사용자_인증-발송-실패_이미_발송됨", sendVerificationEmail(email), status().is4xxClientError()),
                testHelper.testApiEndpoint("사용자_인증-본인인증_성공", verifyEmail(email, verificationCodeA), status().is2xxSuccessful()),
                testHelper.testApiEndpoint("사용자_인증-본인인증_실패_이미_인증됨", verifyEmail(email, verificationCodeA), status().is2xxSuccessful()),
                testHelper.testApiEndpoint("사용자_인증-본인인증_확인_실패_잘못된_코드", verifyEmail(email, "111111"), status().is4xxClientError()),
                testHelper.testApiEndpoint("사용자_인증-비밀번호_재설정_메일_전송_성공", sendPasswordResetEmail(email), status().is2xxSuccessful()),
                testHelper.testApiEndpoint("사용자_인증-비밀번호_재설정_메일_전송_실패_이미_발송됨", sendPasswordResetEmail(email), status().is4xxClientError()),
                testHelper.testApiEndpoint("사용자_인증-비밀번호_재설정_성공", resetPassword(email, verificationCodeA), status().is2xxSuccessful()),
                testHelper.testApiEndpoint("사용자_인증-비밀번호_재설정_실패_잘못된_코드", resetPassword(email, "111111"), status().is4xxClientError()),
                testHelper.testApiEndpoint("사용자_인증-비밀번호_재설정_실패_잘못된_이메일", resetPassword(wrongEmail, verificationCodeA), status().is4xxClientError())
        );
    }

    private RequestBuilder sendVerificationEmail(String email) {
        return post("/api/auth/email-verification/send-mail")
                .queryParam("email", email);
    }

    private RequestBuilder sendPasswordResetEmail(String email) {
        return post("/api/auth/password-reset/send-mail")
                .queryParam("email", email);
    }

    private RequestBuilder verifyEmail(String email, String code) {
        return post("/api/auth/email-verification/verify")
                .queryParam("email", email)
                .queryParam("code", code);
    }

    private RequestBuilder resetPassword(String email, String code) {
        return post("/api/auth/password-reset/verify")
                .queryParam("email", email)
                .queryParam("code", code);
    }

}
