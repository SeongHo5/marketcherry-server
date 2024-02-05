package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

/**
 * 정산 상태입니다. 정산이 아직 되지 않았다면 INCOMPLETED, 정산이 완료됐다면 COMPLETED 값이 들어옵니다.
 */
public enum SettlementStatus {
    INCOMPLETED, COMPLETED
}
