package com.cherrydev.cherrymarketbe.server.domain.payment.toss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentCancelForm(@NotNull String cancelReason, Number cancelAmount) {}
