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
import java.util.Optional;

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
        Optional<Cart> cart = cartRepository.findByAccountAndGoods(account, goods);
        // 장바구니에 상품이 존재하는 경우, 수량을 증가시킨다.
        if (cart.isPresent()) {
            cart.get().increaseQuantity();
            return;
        }
        // 장바구니에 상품이 없으면, 새로 추가한다.
        goodsValidator.validateGoodsBeforeAddToCart(goods);
        cartRepository.save(Cart.from(account, goods));
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

    protected List<Cart> filterAvailableCartItems(final List<Cart> cartItems) {
        return cartItems.stream()
                .filter(cart -> cart.getGoods().getSalesStatus().isOnSale()
                        && cart.getQuantity() <= cart.getGoods().getInventory())
                .toList();
    }

    private Cart fetchCartEntity(final Long cartId, final Account account) {
        return cartRepository.findByIdAndAccount(cartId, account)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CART));
    }

}
