package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.account.SignoutRequest;
import com.gr.freshermanagement.dto.request.account.TokenRefreshRequest;
import com.gr.freshermanagement.dto.response.account.AuthenticationResponse;
import com.gr.freshermanagement.entity.Account;
import com.gr.freshermanagement.entity.RefreshToken;
import com.gr.freshermanagement.exception.account.TokenRefreshException;
import com.gr.freshermanagement.exception.base.NotFoundException;
import com.gr.freshermanagement.repository.AccountRepository;
import com.gr.freshermanagement.repository.RefreshTokenRepository;
import com.gr.freshermanagement.security.CustomUserDetailsService;
import com.gr.freshermanagement.security.JWTUtility;
import com.gr.freshermanagement.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountRepository accountRepository;
    private final JWTUtility jwtUtility;
    private final CustomUserDetailsService userDetailsService;
    private final CacheManager cacheManager;


    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getRefreshToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Transactional
    @Override
    public void logout(SignoutRequest signoutRequest) {
        Cache cache = cacheManager.getCache("accessTokens");

        if (!refreshTokenRepository.existsByRefreshToken(signoutRequest.getRefreshToken())) {
            throw new TokenRefreshException(signoutRequest.getRefreshToken(), "Refresh token was not exists");
        }
        if (cache != null) {
            cache.evictIfPresent(signoutRequest.getRefreshToken());
        }
        // delete refresh-token in db
        refreshTokenRepository.deleteByRefreshToken(signoutRequest.getRefreshToken());
    }

    @Override
    public AuthenticationResponse refreshToken(TokenRefreshRequest request) {
        Cache cache = cacheManager.getCache("accessTokens");

        String requestRefreshToken = request.getRefreshToken();

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(requestRefreshToken)
                .map(this::verifyExpiration)
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(refreshToken.getAccount().getUsername());
        String token = jwtUtility.generateToken(userDetails);

        if (cache != null) {
            cache.put(token, requestRefreshToken);
        }
        return new AuthenticationResponse(token, requestRefreshToken);
    }


}
