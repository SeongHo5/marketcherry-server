package com.cherrydev.cherrymarketbe.server.application.admin.controller;

import static com.cherrydev.cherrymarketbe.server.application.common.constant.CommonConstant.PAGE_TOTAL_HEADER;

import com.cherrydev.cherrymarketbe.server.application.admin.service.AccountManagementService;
import com.cherrydev.cherrymarketbe.server.application.customer.service.CouponManagementService;
import com.cherrydev.cherrymarketbe.server.application.customer.service.RewardService;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.AccountSearchConditions;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.IssueCoupon;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.ModifyUserRole;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.ModifyUserStatus;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.response.AdminUserInfo;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.response.CouponInfo;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestAddReward;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
public class AccountManagementController {

  private final AccountManagementService accountManagementService;
  private final CouponManagementService couponManagementService;
  private final RewardService rewardService;

  /**
   * 계정 목록 전체 조회
   *
   * @param pageable 페이징 정보(page, size)
   */
  @GetMapping("/accounts")
  public ResponseEntity<Page<AdminUserInfo>> fetchGoodsByConditions(
      Pageable pageable,
      @RequestParam(value = "email", required = false) final String email,
      @RequestParam(value = "name", required = false) final String name,
      @RequestParam(value = "gender", required = false) final String gender,
      @RequestParam(value = "registerType", required = false) final String registerType,
      @RequestParam(value = "status", required = false) final String status,
      @RequestParam(value = "role", required = false) final String role,
      @RequestParam(value = "sort", required = false) final String sort) {
    AccountSearchConditions conditions =
        new AccountSearchConditions(email, name, gender, registerType, status, role, sort);
    Page<AdminUserInfo> response =
        accountManagementService.fetchGoodsByConditions(pageable, conditions);
    return ResponseEntity.ok()
        .header(PAGE_TOTAL_HEADER, String.valueOf(response.getTotalPages()))
        .body(response);
  }

  /** 계정 권한 변경 */
  @PatchMapping("/account/role")
  public ResponseEntity<Void> modifyAccountRole(@RequestBody final ModifyUserRole roleRequestDto) {
    accountManagementService.modifyAccountRole(roleRequestDto);
    return ResponseEntity.ok().build();
  }

  /** 계정 상태 변경 */
  @PatchMapping("/account/status")
  public ResponseEntity<Void> modifyAccountStatus(
      @RequestBody final ModifyUserStatus statusRequestDto) {
    accountManagementService.modifyAccountStatus(statusRequestDto);
    return ResponseEntity.ok().build();
  }

  /**
   * 관리자에 의해 포인트 지급
   *
   * @param addRewardRequestDto 지급할 포인트 정보
   */
  @PostMapping("/rewards")
  public ResponseEntity<Void> grantReward(@RequestBody final RequestAddReward addRewardRequestDto) {
    rewardService.grantReward(addRewardRequestDto);
    return ResponseEntity.ok().build();
  }

  /**
   * 관리자에 의해 쿠폰 발행
   *
   * @param issueCoupon 발행할 쿠폰 정보
   */
  @PostMapping("/coupons")
  public ResponseEntity<Void> issueCoupon(@RequestBody final IssueCoupon issueCoupon) {
    couponManagementService.issueCoupon(issueCoupon);
    return ResponseEntity.ok().build();
  }

  /**
   * 쿠폰 목록 전체 조회
   *
   * @param pageable 페이징 정보(page, size)
   * @return 쿠폰 목록
   */
  @GetMapping("/coupons")
  public ResponseEntity<Page<CouponInfo>> searchCoupons(Pageable pageable) {
    Page<CouponInfo> response = couponManagementService.getAllCoupons(pageable);
    return ResponseEntity.ok()
        .header(PAGE_TOTAL_HEADER, String.valueOf(response.getTotalPages()))
        .body(response);
  }
}
