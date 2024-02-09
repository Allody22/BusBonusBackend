package ru.nsu.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nsu.model.RefreshToken;
import ru.nsu.model.user.Account;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByAccount(Account account);

    @Modifying
    @Query(value = "DELETE FROM refresh_token WHERE account_id = :accountId", nativeQuery = true)
    void deleteAllByAccountId(@Param("accountId") Long accountId);

    @Modifying
    int deleteByAccount(Account account);
}
