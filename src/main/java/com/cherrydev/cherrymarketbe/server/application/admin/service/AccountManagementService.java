package com.cherrydev.cherrymarketbe.server.application.admin.service;

import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.CHANGE_ROLE_FORBIDDEN;
import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.INVALID_INPUT_VALUE;
import static com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType.LOCAL;

import com.cherrydev.cherrymarketbe.server.application.account.service.AccountQueryService;
import com.cherrydev.cherrymarketbe.server.application.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.AccountSearchConditions;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.ModifyUserRole;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.ModifyUserStatus;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.response.AdminUserInfo;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountManagementService {

  private final AccountQueryService accountQueryService;

  @Transactional(readOnly = true)
  public Page<AdminUserInfo> fetchGoodsByConditions(
      Pageable pageable, final AccountSearchConditions conditions) {
    return accountQueryService.findByConditions(pageable, conditions).map(AdminUserInfo::of);
  }

  @Transactional
  public void modifyAccountRole(final ModifyUserRole request) {
    Account account = accountQueryService.fetchAccountEntity(request.getEmail());

    checkAccountRegisterType(account);

    account.updateAccountRole(request.getNewRole());
  }

  @Transactional
  public void modifyAccountStatus(final ModifyUserStatus request) {
    Account account = accountQueryService.fetchAccountEntity(request.getEmail());
    LocalDate restrictionEndDate = LocalDate.parse(request.getRestrictedUntil());
    checkAccountRestrictionEndDate(restrictionEndDate);

    account.updateAccountStatus(request.getNewStatus());
    account.updateRestrictedUntil(restrictionEndDate);
  }

  // =============== PRIVATE METHODS =============== //

  private void checkAccountRegisterType(final Account account) {
    if (!account.getRegistType().equals(LOCAL)) {
      throw new ServiceFailedException(CHANGE_ROLE_FORBIDDEN);
    }
  }

  private void checkAccountRestrictionEndDate(final LocalDate restrictionEndDate) {
    if (restrictionEndDate == null) {
      throw new ServiceFailedException(INVALID_INPUT_VALUE);
    }
    if (restrictionEndDate.isBefore(LocalDate.now())) {
      throw new ServiceFailedException(INVALID_INPUT_VALUE);
    }
  }
}
