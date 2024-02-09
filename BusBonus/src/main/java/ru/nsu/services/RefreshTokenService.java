package ru.nsu.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.exceptions.AccountNotFoundException;
import ru.nsu.exceptions.TokenRefreshException;
import ru.nsu.model.RefreshToken;
import ru.nsu.model.user.Account;
import ru.nsu.repository.RefreshTokenRepository;
import ru.nsu.repository.user.AccountRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${busbonus.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    private final AccountRepository accountRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               AccountRepository accountRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.accountRepository = accountRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long accountId) {
        RefreshToken refreshToken = new RefreshToken();

        Account accountForRefreshToken = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);
        refreshToken.setAccount(accountForRefreshToken);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Время жизни токена прошло. Необходимо сделать еще один запрос на авторизацию");
        }

        return token;
    }

    @Transactional
    public int deleteByAccountId(Long accountId) {
        var account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);
        if (refreshTokenRepository.findByAccount(account).isPresent()) {
            return refreshTokenRepository.deleteByAccount(account);
        }
        return 0;
    }

    public boolean findByAccountId(Long accountId) {
        var account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);
        return refreshTokenRepository.findByAccount(account).isPresent();
    }

    @Transactional
    public void deleteAllByAccountId(Long accountId) {
        refreshTokenRepository.deleteAllByAccountId(accountId);
    }

    @Transactional
    public void deleteAllByBusBonusId(Long accountId) {
        refreshTokenRepository.deleteAllByAccountId(accountId);
    }
}
