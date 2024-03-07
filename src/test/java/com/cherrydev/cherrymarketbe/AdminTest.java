package com.cherrydev.cherrymarketbe;

import com.cherrydev.cherrymarketbe.factory.AdminFactory;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.IssueCoupon;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.ModifyUserRole;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.ModifyUserStatus;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.JwtResponse;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestAddReward;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static com.cherrydev.cherrymarketbe.TestHelper.convertToJSONString;
import static com.cherrydev.cherrymarketbe.server.application.auth.constant.AuthConstant.BEARER_PREFIX;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Rollback
@Transactional
@SpringBootTest
@DisabledInAotMode
@AutoConfigureMockMvc
class AdminTest {

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    private TestHelper testHelper;

    private JwtResponse jwtResponse;

    @TestFactory
    @DisplayName("관리자 계정 관리 테스트")
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    Stream<DynamicTest> dynamicTestForAccountManagement() throws JsonProcessingException {
        // Given
        jwtResponse = jwtProvider.createJwtToken("admin@devcherry.com");
        ModifyUserRole modifyUserRole = AdminFactory.createModifyUserRoleRequestDtoA();
        ModifyUserStatus modifyUserStatusA = AdminFactory.createModifyUserStatusByAdminDtoA();
        ModifyUserStatus modifyUserStatusB = AdminFactory.createModifyUserStatusByAdminDtoB();
        ModifyUserStatus modifyUserStatusC = AdminFactory.createModifyUserStatusByAdminDtoC();

        // When & Then
        return Stream.of(
                testHelper.testApiEndpoint("관리자-계정_조회_성공", getAccountInfo(), status().isOk()),
                testHelper.testApiEndpoint("관리자-계정_권한_변경_성공", modifyAccountRole(modifyUserRole), status().isOk()),
                testHelper.testApiEndpoint("관리자-계정_상태_변경_성공", modifyAccountStatus(modifyUserStatusA), status().isOk()),
                testHelper.testApiEndpoint("관리자-계정_상태_변경_실패_날짜_누락", modifyAccountStatus(modifyUserStatusB), status().is4xxClientError()),
                testHelper.testApiEndpoint("관리자-계정_상태_변경_실패_날짜_오류", modifyAccountStatus(modifyUserStatusC), status().is4xxClientError())
        );
    }

    private RequestBuilder getAccountInfo() {
        return RestDocumentationRequestBuilders.get("/api/admin/accounts")
                .header(AUTHORIZATION, BEARER_PREFIX + jwtResponse.accessToken())
                .contentType(APPLICATION_JSON);
    }

    private RequestBuilder modifyAccountRole(ModifyUserRole modifyUserRole) throws JsonProcessingException {
        return patch("/api/admin/account/role")
                .header(AUTHORIZATION, BEARER_PREFIX + jwtResponse.accessToken())
                .contentType(APPLICATION_JSON)
                .content(convertToJSONString(modifyUserRole));
    }

    private RequestBuilder modifyAccountStatus(ModifyUserStatus modifyUserStatus) throws JsonProcessingException {
        return patch("/api/admin/account/status")
                .header(AUTHORIZATION, BEARER_PREFIX + jwtResponse.accessToken())
                .contentType(APPLICATION_JSON)
                .content(convertToJSONString(modifyUserStatus));
    }

    @TestFactory
    @DisplayName("관리자 포인트 & 쿠폰 관리 테스트")
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    Stream<DynamicTest> dynamicTestForRewardAndCouponManagement() throws JsonProcessingException {
        // Given
        jwtResponse = jwtProvider.createJwtToken("admin@devcherry.com");
        RequestAddReward requestAddRewardA = AdminFactory.createAddRewardRequestDtoA();
        RequestAddReward requestAddRewardB = AdminFactory.createAddRewardRequestDtoB();
        IssueCoupon issueCouponA = AdminFactory.createIssueCouponRequestDtoA();
        IssueCoupon issueCouponB = AdminFactory.createIssueCouponRequestDtoB();

        // When & Then
        return Stream.of(
                testHelper.testApiEndpoint("관리자-포인트_지급_성공", grantReward(requestAddRewardA), status().isOk()),
                testHelper.testApiEndpoint("관리자-포인트_지급_실패_없는_계정", grantReward(requestAddRewardB), status().is4xxClientError()),
                testHelper.testApiEndpoint("관리자-쿠폰_발급_성공", issueCoupon(issueCouponA), status().isOk()),
                testHelper.testApiEndpoint("관리자-쿠폰_발급_실패_코드_중복", issueCoupon(issueCouponB), status().is4xxClientError())
        );
    }

    private RequestBuilder grantReward(RequestAddReward requestAddReward) throws JsonProcessingException {
        return post("/api/admin/rewards")
                .header(AUTHORIZATION, BEARER_PREFIX + jwtResponse.accessToken())
                .contentType(APPLICATION_JSON)
                .content(convertToJSONString(requestAddReward));
    }

    private RequestBuilder issueCoupon(IssueCoupon issueCoupon) throws JsonProcessingException {
        return post("/api/admin/coupons")
                .header(AUTHORIZATION, BEARER_PREFIX + jwtResponse.accessToken())
                .contentType(APPLICATION_JSON)
                .content(convertToJSONString(issueCoupon));
    }

}
