package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model;


import lombok.Value;

import com.fasterxml.jackson.annotation.JsonProperty;

@Value
public class Failure {

    @JsonProperty("code")
    String code;

    @JsonProperty("message")
    String message;

}
