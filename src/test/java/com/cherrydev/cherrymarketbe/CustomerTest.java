package com.cherrydev.cherrymarketbe;

import com.cherrydev.cherrymarketbe.server.application.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestAddAddress;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestModifyAddress;
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
import static com.cherrydev.cherrymarketbe.factory.CustomerFactory.*;
import static com.cherrydev.cherrymarketbe.server.application.auth.constant.AuthConstant.BEARER_PREFIX;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Rollback
@Transactional
@SpringBootTest
@DisabledInAotMode
@AutoConfigureMockMvc
class CustomerTest {

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private JwtProvider jwtProvider;

    @TestFactory
    @DisplayName("고객 배송지 관련 API 테스트")
    Stream<DynamicTest> dynamicTestForCustomer() throws JsonProcessingException {
        // Given
        String accessToken = jwtProvider.createJwtToken("agim@example.org").accessToken();
        RequestAddAddress requestAddAddressA = createAddAddressRequestDtoA();
        RequestAddAddress requestAddAddressB = createAddAddressRequestDtoB();
        RequestModifyAddress requestModifyAddressA = createModifyAddressRequestDtoA();

        // When & Then
        return Stream.of(
                testHelper.testApiEndpoint("고객-배송지_목록_조회", getMyAddress(accessToken), status().is2xxSuccessful()),
                testHelper.testApiEndpoint("고객-배송지_추가_성공", addAddress(requestAddAddressA, accessToken), status().is2xxSuccessful()),
                testHelper.testApiEndpoint("고객-배송지_추가_실패_최대_개수_초과", addAddress(requestAddAddressA, accessToken), status().is4xxClientError()),
                testHelper.testApiEndpoint("고객-배송지_추가_실패_기본_배송지_존재", addAddress(requestAddAddressB, accessToken), status().is4xxClientError()),
                testHelper.testApiEndpoint("고객-배송지_삭제_성공", deleteAddress(1L, accessToken), status().is2xxSuccessful()),
                testHelper.testApiEndpoint("고객-배송지_수정_성공", modifyAddress(requestModifyAddressA, accessToken), status().is2xxSuccessful())
        );
    }

    private RequestBuilder getMyAddress(String accessToken) {
        return post("/api/customer/address/lists")
                .header(AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(APPLICATION_JSON);
    }

    private RequestBuilder addAddress(RequestAddAddress request, String accessToken) throws JsonProcessingException {
        return post("/api/customer/address/")
                .header(AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(APPLICATION_JSON)
                .content(convertToJSONString(request));
    }

    private RequestBuilder modifyAddress(RequestModifyAddress request, String accessToken) throws JsonProcessingException {
        return post("/api/customer/address/")
                .header(AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(APPLICATION_JSON)
                .content(convertToJSONString(request));
    }

    private RequestBuilder deleteAddress(Long addressId, String accessToken) throws JsonProcessingException {
        return delete("/api/customer/address/")
                .header(AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(APPLICATION_JSON)
                .content(convertToJSONString(addressId));
    }

    @TestFactory
    @DisplayName("고객 리워드 & 쿠폰 관련 API 테스트")
    Stream<DynamicTest> dynamicTestForCustomerReward() {
        // Given
        String accessToken = jwtProvider.createJwtToken("agim@example.org").accessToken();
        String couponCode = "FIRORD00";

        return Stream.of(
                testHelper.testApiEndpoint("고객-리워드_정보_조회", getMyRewardSummary(accessToken), status().is2xxSuccessful()),
                testHelper.testApiEndpoint("고객-쿠폰_등록_성공", registerCoupon(couponCode, accessToken), status().is2xxSuccessful()),
                testHelper.testApiEndpoint("고객-쿠폰_등록_실패_이미_보유", registerCoupon(couponCode, accessToken), status().is4xxClientError()),
                testHelper.testApiEndpoint("고객-쿠폰_등록_실패_없는_코드", registerCoupon("NONE1515", accessToken), status().is4xxClientError()),
                testHelper.testApiEndpoint("고객-쿠폰_목록_조회", getCouponList(accessToken), status().is2xxSuccessful())
        );
    }

    private RequestBuilder getCouponList(String accessToken) {
        return post("/api/customer/coupons")
                .header(AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(APPLICATION_JSON);
    }

    private RequestBuilder getMyRewardSummary(String accessToken) {
        return post("/api/customer/rewards")
                .header(AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(APPLICATION_JSON);
    }

    private RequestBuilder registerCoupon(String couponCode, String accessToken) {
        return post("/api/customer/" + couponCode + "/register")
                .header(AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(APPLICATION_JSON);
    }

}
