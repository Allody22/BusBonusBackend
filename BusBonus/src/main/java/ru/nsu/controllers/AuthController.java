package ru.nsu.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.nsu.configuration.security.jwt.JwtUtils;
import ru.nsu.exceptions.TokenRefreshException;
import ru.nsu.model.RefreshToken;
import ru.nsu.model.user.Account;
import ru.nsu.payload.request.AuthRequest;
import ru.nsu.payload.request.TokenRefreshRequest;
import ru.nsu.payload.response.JwtAuthResponse;
import ru.nsu.payload.response.MessageResponse;
import ru.nsu.payload.response.TokenRefreshResponse;
import ru.nsu.services.RefreshTokenService;
import ru.nsu.services.UserDetailsImpl;
import ru.nsu.services.interfaces.IAccountService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    private final IAccountService accountService;

    @PostMapping("/auth/refresh")
    @Transactional
    public ResponseEntity<?> refreshTokenPost(@Valid @RequestBody TokenRefreshRequest request) {
        if (!"refresh_token".equals(request.getGrant_type())) {
            return ResponseEntity.badRequest().body("Invalid grant_type");
        }
        String oldRefreshToken = request.getRefresh_token();

        RefreshToken refreshToken = refreshTokenService.findByToken(oldRefreshToken)
                .orElseThrow(() -> new TokenRefreshException(oldRefreshToken, "Refresh token is not in database!"));

        refreshTokenService.verifyExpiration(refreshToken);
        Account account = refreshToken.getAccount();

        String token = jwtUtils.generateTokenFromUsername(account.getPhone());

        long expiresIn = refreshToken.getExpiryDate().getEpochSecond() - Instant.now().getEpochSecond();

        return ResponseEntity.ok(new TokenRefreshResponse(token, oldRefreshToken, expiresIn));
    }


    @GetMapping("/auth/refresh")
    @Transactional
    public ResponseEntity<?> refreshTokenGet(@NotBlank @RequestParam(value = "refresh_token") String refreshTokenValue,
                                             @RequestParam(value = "grant_type") String grantType) {

        if (!"refresh_token".equals(grantType)) {
            return ResponseEntity.badRequest().body("Invalid grant_type");
        }

        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenValue)
                .orElseThrow(() -> new TokenRefreshException(refreshTokenValue, "Refresh token is not in database!"));

        refreshTokenService.verifyExpiration(refreshToken);
        Account account = refreshToken.getAccount();

        String token = jwtUtils.generateTokenFromUsername(account.getPhone());

        long expiresIn = refreshToken.getExpiryDate().getEpochSecond() - Instant.now().getEpochSecond();

        return ResponseEntity.ok(new TokenRefreshResponse(token, refreshTokenValue, expiresIn));
    }

    @PostMapping("/auth")
    @Transactional
    public ResponseEntity<?> auth(@Valid @RequestBody AuthRequest authRequest) {
        String userPhone = authRequest.getLogin();

        if (!accountService.checkAccExistenceByPhone(userPhone)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка! Пожалуйста, проверьте введённые данные."));
        }

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userPhone, authRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String access_token = jwtUtils.generateJwtToken(userDetails);

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
            long expires_in = refreshToken.getExpiryDate().getEpochSecond() - Instant.now().getEpochSecond();

            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse(access_token, refreshToken.getToken(), "Bearer", expires_in);

            return ResponseEntity.ok(jwtAuthResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка! Пожалуйста, проверьте введённые данные."));
        }
    }
}
