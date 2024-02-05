package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Checkout {
    /**
     * 결제창이 열리는 주소입니다.
     */
    @JsonProperty("url")
    private String url;
}
