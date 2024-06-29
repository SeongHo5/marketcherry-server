package com.cherrydev.cherrymarketbe.server.application.account.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountDetailsServiceImpl implements UserDetailsService {

    private final AccountQueryService accountQueryService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AccountDetails(accountQueryService.fetchAccountEntity(username));
    }
}
