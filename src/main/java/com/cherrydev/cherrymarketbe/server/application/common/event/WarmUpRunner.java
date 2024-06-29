package com.cherrydev.cherrymarketbe.server.application.common.event;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.cherrydev.cherrymarketbe.server.domain.auth.dto.request.RequestSignIn;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.SignInResponse;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsInfo;

import static com.cherrydev.cherrymarketbe.server.application.common.log.CherryLogger.logWarmUpEnd;
import static com.cherrydev.cherrymarketbe.server.application.common.log.CherryLogger.logWarmUpStart;

@Slf4j
@Component
@RequiredArgsConstructor
public class WarmUpRunner {

    public static final int WARM_UP_CALL_COUNT = 30;
    public static final String BASE_URL = "http://localhost:8080/api";
    private static final RestTemplate restTemplate = new RestTemplate();

    @Value("${warmup.id}")
    private String warmUpAuthId;

    @Value("${warmup.pw}")
    private String warmUpAuthPassword;

    @EventListener(ApplicationReadyEvent.class)
    public void warmUp() {
        logWarmUpStart();
        for (int i = 0; i < WARM_UP_CALL_COUNT; i++) {
            invokeAuthApi();
            invokeGoodsSearchApi();
        }
        logWarmUpEnd();
    }

    private void invokeGoodsSearchApi() {
        String url = BASE_URL + "/goods";
        String onDiscountCondition = "?on_discount=true";
        String sortPriceDesc = "?sort=DESC";
        restTemplate.getForObject(url, GoodsInfo.class);
        restTemplate.getForObject(url + onDiscountCondition, GoodsInfo.class);
        restTemplate.getForObject(url + sortPriceDesc, GoodsInfo.class);
    }

    private void invokeAuthApi() {
        RequestSignIn requestSignIn = new RequestSignIn(warmUpAuthId, warmUpAuthPassword);
        ResponseEntity<SignInResponse> response = restTemplate.postForEntity("/auth/login", requestSignIn, SignInResponse.class);
        restTemplate.postForEntity("/auth/re-issue", response.getBody(), SignInResponse.class);
        restTemplate.delete("/auth/" + Objects.requireNonNull(response.getBody()).getAccessToken());
    }

}
