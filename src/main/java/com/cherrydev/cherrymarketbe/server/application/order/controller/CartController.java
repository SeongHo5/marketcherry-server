package com.cherrydev.cherrymarketbe.server.application.order.controller;

import com.cherrydev.cherrymarketbe.server.application.order.service.CartService;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.CartByStorageType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
public class CartController {

  private final CartService cartService;

  /** 장바구니 리스트 - 주문 가능한 상품 */
  @GetMapping("/")
  public ResponseEntity<CartByStorageType> getAvailableCarts(
      @AuthenticationPrincipal final AccountDetails accountDetails) {
    return ResponseEntity.ok(CartByStorageType.of(cartService.fetchCartItems(accountDetails)));
  }

  /** 장바구니 상품 추가, 이미 있는 상품이면 수량 증가 */
  @PutMapping("/{goods_code}")
  public ResponseEntity<Void> addToCart(
      @AuthenticationPrincipal final AccountDetails accountDetails,
      @PathVariable("goods_code") final String goodsCode) {

    cartService.addToCart(accountDetails, goodsCode);
    return ResponseEntity.ok().build();
  }

  /** 장바구니 상품 삭제 */
  @DeleteMapping("/{cart_id}")
  public void deleteCartItem(
      @AuthenticationPrincipal final AccountDetails accountDetails,
      @PathVariable("cart_id") final Long cartId) {
    cartService.deleteCartItem(accountDetails, cartId);
  }
}
