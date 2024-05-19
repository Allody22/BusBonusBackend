package ru.nsu.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;
import ru.nsu.exceptions.AccountNotFoundException;
import ru.nsu.exceptions.TokenRefreshException;
import ru.nsu.model.RefreshToken;
import ru.nsu.model.user.Account;
import ru.nsu.repository.RefreshTokenRepository;
import ru.nsu.repository.user.AccountRepository;
import ru.nsu.services.interfaces.IRefreshTokenService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class RefreshTokenService implements IRefreshTokenService {

    @Value("${busbonus.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    private final AccountRepository accountRepository;

    @Value("${busbonus.app.jwtRefreshCookieName}")
    private String jwtRefreshCookie;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               AccountRepository accountRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.accountRepository = accountRepository;
    }

    public List<RefreshToken> findAllByAccountId(Long accountId) {
        return refreshTokenRepository.findAllByAccount_Id(accountId);
    }

    @Transactional
    public void processLogout(HttpServletRequest request) {
        String refreshToken = getJwtRefreshFromCookies(request);
        if (refreshToken != null) {
            RefreshToken token = findByRefreshToken(refreshToken);
            String fingerPrint = token.getFingerPrint();
            Long accountId = token.getAccount().getId();
            deleteAllRefreshTokenByFingerPrintAndAccountId(fingerPrint, accountId);
        }
    }

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Такого токена нет в базе данных!"));
    }

    public ResponseCookie deleteRefreshJwtCookie() {
        long maxAgeSeconds = 0;
        return generateCookie(jwtRefreshCookie, "helloWorld", "/api/test", maxAgeSeconds);
    }

    public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
        long maxAgeSeconds = refreshTokenDurationMs / 1000; // Преобразование в секунды
        return generateCookie(jwtRefreshCookie, refreshToken, "/api/test", maxAgeSeconds);
    }

    private ResponseCookie generateCookie(String name, String value, String path, long maxAgeSeconds) {
        return ResponseCookie.from(name, value)
                .path(path)
                .maxAge(maxAgeSeconds)
                //Http или https проколы, куки не будут доступны для клиентских скриптов, например, JavaScript.
                // Это мера предотвращения атак, таких как кража куки через XSS (межсайтовый скриптинг).
                .httpOnly(true)
                //Только https протокол
//                .secure(true)
                .build();
    }

    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtRefreshCookie);
    }

    public String getFingerprintFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, "fingerprint");
    }

    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    @Cacheable(value = "refreshTCache", key = "#token")
    @Transactional
    public RefreshToken findByTokenInCache(String token) {
        return findByRefreshToken(token);
    }


    @CachePut(cacheNames = "refreshTCache", key = "#result.token")
    @Transactional
    public RefreshToken createRefreshToken(Long accountId, String fingerPrint) {
        RefreshToken refreshToken = new RefreshToken();

        Account accountForRefreshToken = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);
        refreshToken.setAccount(accountForRefreshToken);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setFingerPrint(fingerPrint);

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @CacheEvict(value = "refreshTCache", key = "#refreshToken.token")
    @Transactional
    public void deleteRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(findByRefreshToken(refreshToken.getToken()));
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
}
