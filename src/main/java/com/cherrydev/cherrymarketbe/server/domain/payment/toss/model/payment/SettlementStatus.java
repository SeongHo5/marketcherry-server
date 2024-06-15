package com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.payment;

/**
 * 정산 상태입니다.
 * <li>INCOMPLETED: 정산이 완료되지 않은 상태입니다.</li>
 * <li>COMPLETED: 정산이 완료된 상태입니다.</li>
 */
public enum SettlementStatus {
    INCOMPLETED, COMPLETED
}
