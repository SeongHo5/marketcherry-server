package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Failure {

    @JsonProperty("code")
    String code;

    @JsonProperty("message")
    String message;

}



