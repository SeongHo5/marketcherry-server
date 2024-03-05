package com.cherrydev.cherrymarketbe.server.application.account.event;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AccountRegistrationEvent extends ApplicationEvent {

    public static final String SIGN_UP_REWARD_TYPE = "WELCOME";
    public static final String SIGN_UP_COUPON_CODE = "WELCO0ME";
    private static final int WELCOME_REWARD_AMOUNTS = 1000;

    private final Account account;
    private final int amounts;
    private final String rewardGrantType;
    private final String couponCode;


    public AccountRegistrationEvent(Object source, Account account) {
        super(source);
        this.account = account;
        this.amounts = WELCOME_REWARD_AMOUNTS;
        this.rewardGrantType = SIGN_UP_REWARD_TYPE;
        this.couponCode = SIGN_UP_COUPON_CODE;
    }

    public static AccountRegistrationEvent from(Object source, Account account) {
        return new AccountRegistrationEvent(source, account);
    }

}
