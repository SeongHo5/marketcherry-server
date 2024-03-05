package com.cherrydev.cherrymarketbe.server.application.account.service;

import com.cherrydev.cherrymarketbe.server.application.account.event.AccountRegistrationEvent;
import com.cherrydev.cherrymarketbe.server.application.common.utils.CodeGenerator;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestModifyAccountInfo;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestSignUp;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountInfo;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Agreement;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.oauth.OAuthAccountInfo;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.account.AccountRepository;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.account.AgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType.LOCAL;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AgreementRepository agreementRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final AccountValidator accountValidator;
    @Transactional
    public void createAccount(final RequestSignUp requestSignUp) {
        String requestedEmail = requestSignUp.getEmail();
        String requestedUsername = requestSignUp.getName();

        accountValidator.checkUsernameIsProhibited(requestedUsername);
        accountValidator.checkEmailIsDuplicated(requestedEmail);

        String encodedPassword = passwordEncoder.encode(requestSignUp.getPassword());

        Account account = Account.from(requestSignUp, encodedPassword, LOCAL);
        accountRepository.save(account);

        Agreement agreement = Agreement.of(requestSignUp);
        agreementRepository.save(agreement);

        publishWelcomeEvent(account);
    }

    @Transactional
    public void createAccountByOAuth(final OAuthAccountInfo oAuthAccountInfo, final String provider) {
        String email = oAuthAccountInfo.getEmail();
        String name = oAuthAccountInfo.getName();
        String encodedPassword = passwordEncoder.encode(CodeGenerator.generateRandomCode(10));

        accountValidator.checkUsernameIsProhibited(name);
        accountValidator.checkEmailIsDuplicated(email);

        Account account = Account.from(oAuthAccountInfo, encodedPassword, RegisterType.valueOf(provider));

        accountRepository.save(account);
        publishWelcomeEvent(account);
    }

    @Transactional(readOnly = true)
    public AccountInfo getAccountInfo(final AccountDetails accountDetails) {
        Account account = accountDetails.getAccount();
        return AccountInfo.of(account);
    }

    @Transactional
    public AccountInfo modifyAccountInfo(
            final AccountDetails accountDetails,
            final RequestModifyAccountInfo requestDto
    ) {
        Account account = accountDetails.getAccount();

        if (requestDto.password() != null) {
            String encodedPassword = passwordEncoder.encode(requestDto.password());
            account.updatePassword(encodedPassword);
        }

        if (requestDto.contact() != null) {
            account.updateContact(requestDto.contact());
        }

        if (requestDto.birthdate() != null) {
            LocalDate birthdate = LocalDate.parse(requestDto.birthdate());
            account.updateBirthdate(birthdate);
        }

        return AccountInfo.of(account);
    }

    @Transactional
    public void deleteAccount(final AccountDetails accountDetails) {
        accountRepository.delete(accountDetails.getAccount());
    }

    // =============== PRIVATE METHODS =============== //

    private void publishWelcomeEvent(Account account) {
        AccountRegistrationEvent event = new AccountRegistrationEvent(this, account);
        eventPublisher.publishEvent(event);
    }

}
