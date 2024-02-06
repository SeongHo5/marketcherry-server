package com.cherrydev.cherrymarketbe.server.application.order.service;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.application.goods.service.GoodsService;
import com.cherrydev.cherrymarketbe.server.application.goods.service.GoodsValidator;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Cart;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.order.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.NOT_FOUND_CART;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final GoodsService goodsService;
    private final GoodsValidator goodsValidator;

    @Transactional
    public List<Cart> fetchCartItems(final AccountDetails accountDetails) {
        return cartRepository.findAllByAccount(accountDetails.getAccount());
    }

    @Transactional
    public void addToCart(final AccountDetails accountDetails, final String goodsCode) {
        Account account = accountDetails.getAccount();
        Goods goods = goodsService.fetchGoodsEntity(goodsCode);
        Cart cart = cartRepository.findByAccountAndGoods(account, goods)
                .orElseGet(() ->
                {
                    goodsValidator.validateGoodsBeforeAddToCart(goods);
                    return cartRepository.save(Cart.of(account, goods));
                });
        cart.increaseQuantity();
    }

    @Transactional
    public void deleteCartItem(
            final AccountDetails accountDetails,
            final Long cartId
    ) {
        Cart cart = fetchCartEntity(cartId, accountDetails.getAccount());
        int quantity = cart.getQuantity();

        if (quantity > 1) {
            cart.decreaseQuantity();
        } else {
            cartRepository.delete(cart);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void clearCartItems(final List<Long> cartIds) {
        cartRepository.deleteAllByIdIn(cartIds);
    }

    private Cart fetchCartEntity(final Long cartId, final Account account) {
        return cartRepository.findByIdAndAccount(cartId, account)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CART));
    }

}
