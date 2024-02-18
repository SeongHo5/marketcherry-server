package com.cherrydev.cherrymarketbe.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DynamicTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;

@Component
public class TestHelper {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 지정된 API 엔드포인트에 대해 테스트를 수행하는 DynamicTest를 생성한다.
     *
     * @param testName 테스트의 Display Name으로 사용될 이름
     * @param requestBuilder API 엔드포인트에 보낼 요청, HTTP Method, URL, 매개변수 또는 본문이 포함되어야 합니다.
     * @param expectedStatus API 엔드포인트의 응답으로 예상되는 HTTP 상태
     * @return DynamicTest
     */
    public DynamicTest testApiEndpoint(String testName, RequestBuilder requestBuilder, ResultMatcher expectedStatus) {
        return DynamicTest.dynamicTest(testName,
                () -> mockMvc.perform(requestBuilder)
                        .andExpect(expectedStatus));
    }

    public static String convertToJSONString(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

}
