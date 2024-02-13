package com.cherrydev.cherrymarketbe.server.application.goods.service;

import com.cherrydev.cherrymarketbe.server.application.annotation.DistributedLock;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.CouldNotObtainLockException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.InsufficientStockException;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.INSUFFICIENT_STOCK;

@Slf4j(topic = "goodsInventory")
@Service
@RequiredArgsConstructor
public class GoodsInventoryService {

    private final EntityManager em;

    @DistributedLock(waitTime = 3, leaseTime = 10)
    @Transactional(propagation = Propagation.MANDATORY)
    @Retryable(retryFor = {CouldNotObtainLockException.class},
            backoff = @Backoff(delay = 100, maxDelay = 500, multiplier = 2))
    public void handleUpdateInventoryInternal(Goods goods, int requestedQuantity) {
        em.refresh(goods);
        if (goods.getInventory() < requestedQuantity) {
            throw new InsufficientStockException(INSUFFICIENT_STOCK, goods.getName());
        }

        goods.updateInventory(requestedQuantity);
        em.flush();
        log.info("재고 업데이트 완료! / 요청 수량 : {}, 반영 후 재고 : {}", requestedQuantity, goods.getInventory());
    }

}
