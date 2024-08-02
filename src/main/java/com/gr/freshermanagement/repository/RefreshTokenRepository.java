package com.gr.freshermanagement.repository;

import com.gr.freshermanagement.entity.Account;
import com.gr.freshermanagement.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String token);
    boolean existsByRefreshToken(String refreshToken);
    void deleteByAccount(Account account);

    List<RefreshToken> findByAccount(Account account);

    void deleteByRefreshToken(String refreshToken);
}
