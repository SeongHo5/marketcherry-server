package com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto;

import jakarta.validation.constraints.NotNull;

import lombok.Builder;

import com.fasterxml.jackson.annotation.JsonInclude;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentCancelForm(

        @NotNull String cancelReason,

        Number cancelAmount

) {
}
