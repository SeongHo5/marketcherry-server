package com.cherrydev.cherrymarketbe.server.infrastructure.repository.order;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Cart;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<List<Cart>> findAllByAccount(Account account);

    Optional<Cart> findByIdAndAccount(Long id, Account account);

    Optional<Cart> findByAccountAndGoods(Account account, Goods goods);

    void deleteAllByIdIn(List<Long> cartIds);

}
