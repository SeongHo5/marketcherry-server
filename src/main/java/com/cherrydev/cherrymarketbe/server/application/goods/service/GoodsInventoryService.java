package com.cherrydev.cherrymarketbe.server.application.goods.service;

import jakarta.persistence.EntityManager;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.cherrydev.cherrymarketbe.server.application.annotation.DistributedLock;
import com.cherrydev.cherrymarketbe.server.application.exception.CouldNotObtainLockException;
import com.cherrydev.cherrymarketbe.server.application.exception.InsufficientStockException;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;

import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.INSUFFICIENT_STOCK;

@Slf4j(topic = "goodsInventory")
@Service
@RequiredArgsConstructor
public class GoodsInventoryService {

    private final EntityManager em;

    /**
     * 재고 업데이트를 처리합니다.
     * @param goods 재고를 업데이트할 상품(영속성 컨텍스트에 존재해야 함)
     * @param requestedQuantity 요청 수량
     * @throws InsufficientStockException 재고 부족 시
     * @throws CouldNotObtainLockException Lock 획득 실패 시(이 경우 재시도됨)
     */
    @DistributedLock(keyName = "#goods.getCode()", waitTime = 3, leaseTime = 10)
    @Transactional(propagation = Propagation.MANDATORY)
    @Retryable(retryFor = {CouldNotObtainLockException.class},
            backoff = @Backoff(delay = 100, maxDelay = 500, multiplier = 2))
    public void processInventoryUpdate(Goods goods, int requestedQuantity) {
        em.refresh(goods);
        checkInventory(goods, requestedQuantity);
        goods.updateInventory(requestedQuantity);
        em.flush();
        log.info("재고 업데이트 완료! / 요청 수량 : {}, 반영 후 재고 : {}", requestedQuantity, goods.getInventory());
    }

    private void checkInventory(Goods goods, int requestedQuantity) {
        if (goods.getInventory() < requestedQuantity) {
            throw new InsufficientStockException(INSUFFICIENT_STOCK, goods.getName());
        }
    }

}
