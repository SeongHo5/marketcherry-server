package com.cherrydev.cherrymarketbe.server.domain.account.dto.response;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.NOT_FOUND_ACCOUNT;

/**
 * {@link Account} 정보를 담고 있는 {@link UserDetails} 구현체 클래스
 * <p>
 * {@link Account} 정보를 담고 있으며, {@link Account}의 {@link UserRole}에 따라 권한을 부여한다.
 */
@Getter
public class AccountDetails implements UserDetails, Serializable {

    private final Account account;

    private final String email;

    public AccountDetails(Account account) {
        if (account == null) {
            throw new NotFoundException(NOT_FOUND_ACCOUNT);
        }
        this.account = account;
        this.email = account.getEmail();
    }

    /**
     * {@link Account} 정보의 {@link UserRole}에 따라 권한을 부여하고, 컬렉션에 담아 반환한다.
     * @return authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole userRole = account.getUserRole();
        String authority = userRole.getAuthority();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
