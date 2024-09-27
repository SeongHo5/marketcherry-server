package com.cherrydev.cherrymarketbe.server.application.order.service;

import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.NOT_FOUND_CART;

import com.cherrydev.cherrymarketbe.server.application.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.application.goods.service.GoodsService;
import com.cherrydev.cherrymarketbe.server.application.goods.service.GoodsValidator;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Cart;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.order.CartRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CartRepository cartRepository;
  private final GoodsService goodsService;
  private final GoodsValidator goodsValidator;

  @Transactional
  public List<Cart> fetchCartItems(final AccountDetails accountDetails) {
    return this.cartRepository.findAllByAccount(accountDetails.getAccount());
  }

  @Transactional
  public void addToCart(final AccountDetails accountDetails, final String goodsCode) {
    Account account = accountDetails.getAccount();
    Goods goods = goodsService.fetchGoodsEntity(goodsCode);
    this.cartRepository
        .findByAccountAndGoods(account, goods)
        .ifPresentOrElse(
            Cart::increaseQuantity,
            () -> {
              this.goodsValidator.validateGoodsBeforeAddToCart(goods);
              this.cartRepository.save(Cart.from(account, goods));
            });
  }

  @Transactional
  public void deleteCartItem(final AccountDetails accountDetails, final Long cartId) {
    Cart cart = fetchCartEntity(cartId, accountDetails.getAccount());
    int quantity = cart.getQuantity();

    if (quantity > 1) {
      cart.decreaseQuantity();
    } else {
      this.cartRepository.delete(cart);
    }
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public void clearCartItems(final List<Long> cartIds) {
    this.cartRepository.deleteAllByIdIn(cartIds);
  }

  protected List<Cart> filterAvailableCartItems(final List<Cart> cartItems) {
    return cartItems.stream()
        .filter(
            cart ->
                cart.getGoods().getSalesStatus().isOnSale()
                    && cart.getQuantity() <= cart.getGoods().getInventory())
        .toList();
  }

  private Cart fetchCartEntity(final Long cartId, final Account account) {
    return this.cartRepository
        .findByIdAndAccount(cartId, account)
        .orElseThrow(() -> new NotFoundException(NOT_FOUND_CART));
  }
}
